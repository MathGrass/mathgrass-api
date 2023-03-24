package de.tudresden.inf.st.mathgrass.api.evaluator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import de.tudresden.inf.st.mathgrass.api.evaluator.executor.Executor;
import de.tudresden.inf.st.mathgrass.api.evaluator.executor.SourceFile;
import de.tudresden.inf.st.mathgrass.api.graph.Graph;
import de.tudresden.inf.st.mathgrass.api.graph.GraphTransformer;
import de.tudresden.inf.st.mathgrass.api.model.GraphDTO;
import de.tudresden.inf.st.mathgrass.api.task.Task;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskManager {
    private final DockerClient dockerClient;
    private final TaskRepository taskRepository;
    private static final Logger logger = LogManager.getLogger(TaskManager.class);


    public TaskManager(TaskRepository taskRepository, DockerClient dockerClient) {
        this.taskRepository = taskRepository;
        this.dockerClient = dockerClient;
    }

    /**
     * Runs a task synchronously. Returns true if the evaluation was successful (i.e., student answer is correct),
     * false otherwise
     */
    public boolean runTaskSynchronously(long taskId, String answer, Executor executor) throws IOException {
        Optional<Task> graphOpt = taskRepository.findById(taskId);
        if (graphOpt.isEmpty()) {
            throw new IllegalArgumentException("Task must be present and its Graph must not be null");
        }

        Graph graph = graphOpt.get().getGraph();

        // set up temp files
        List<Path> tempPaths = new ArrayList<>();
        List<Bind> binds = new ArrayList<>();
        String tempFolder = "." + File.separator + "temp" + File.separator;
        // copy Graph
        Path tempGraphPath = Path.of(tempFolder + UUID.randomUUID()).toAbsolutePath();
        createTempGraphFile(graph, tempGraphPath);
        tempPaths.add(tempGraphPath);
        // bind graph
        Bind graphTempFileBind = new Bind(tempGraphPath.toString(), new Volume(executor.getGraphPath()), AccessMode.ro);
        binds.add(graphTempFileBind);

        // copy source files
        for (SourceFile sourceFile : executor.getSourceFiles()) {
            Path sourceFilePath = Path.of(tempFolder + UUID.randomUUID()).toAbsolutePath();
            Files.write(sourceFilePath, sourceFile.getContents().getBytes());
            Bind sourceFileFind = new Bind(sourceFilePath.toString(), new Volume(sourceFile.getPath()), AccessMode.ro);
            tempPaths.add(sourceFilePath);
            binds.add(sourceFileFind);
        }

        // define host config with binds
        HostConfig hostConfig = new HostConfig();
        hostConfig.withBinds(binds);

        //pullImage(executor);

        boolean result = createRunAndRemoveContainer(answer, executor, hostConfig);
        // remove temp files in separate thread
        new Thread(() -> removeTempFiles(tempPaths)).start();
        return result;
    }

    private void createTempGraphFile(Graph graph, Path tempGraphPath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        GraphTransformer transformer = new GraphTransformer();
        GraphDTO graphDTO = transformer.toDto(graph);
        File resultFile = new File(tempGraphPath.toAbsolutePath().toFile().toURI());
        resultFile.getParentFile().mkdirs();
        objectMapper.writeValue(resultFile, graphDTO);
    }

    private void removeTempFiles(List<Path> paths) {
        paths.forEach(path -> {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private boolean createRunAndRemoveContainer(String answer, Executor executor, HostConfig hostConfig) {
        // append student answer as argument after entrypoint
        String customEntrypoint = executor.getCustomEntrypoint();
        String containerCmd = answer == null || answer.equals("") ? customEntrypoint : customEntrypoint + " " + answer;

        try (CreateContainerCmd createContainerCmd = containerCmd == null || containerCmd.equals("") ?
                dockerClient.createContainerCmd(executor.getContainerImage()).withHostConfig(hostConfig) :
                dockerClient.createContainerCmd(executor.getContainerImage()).withCmd(containerCmd).withHostConfig(hostConfig)) {
            CreateContainerResponse container = createContainerCmd.exec();
            String containerId = container.getId();
            // returns true if evaluation was successful, false if not
            // might need some rework to catch errors
            return startAndWaitForContainer(containerId);
        }
    }

    private boolean startAndWaitForContainer(String containerId) {
        try (StartContainerCmd startContainerCmd = dockerClient.startContainerCmd(containerId)) {
            startContainerCmd.exec();
            return waitForContainerResultAndRemoveContainer(containerId);
        }
    }

    private boolean waitForContainerResultAndRemoveContainer(String containerId) {
        WaitContainerResultCallback callback = new WaitContainerResultCallback();
        try (WaitContainerCmd waitContainerCmd = dockerClient.waitContainerCmd(containerId)) {
            waitContainerCmd.exec(callback);
            var containerStatusCodeOnExit = callback.awaitStatusCode();
            logContainerOutput(containerId);
            // prototyping: exit code == 0 implies answer is correct
            // remove container in separate thread
            new Thread(() -> removeContainer(containerId)).start();
            return Integer.valueOf(0).equals(containerStatusCodeOnExit);
        }
    }

    private void logContainerOutput(String containerId) {
        LogContainerCmd logContainerCmd = dockerClient.logContainerCmd(containerId);
        logContainerCmd.withStdOut(true).withStdErr(true);
        try {
            logContainerCmd.exec(new LogContainerResultCallback() {
                @Override
                public void onNext(Frame item) {

                }
            }).awaitCompletion();
        } catch (InterruptedException e) {
            logger.error("Interrupted while fetching logs from container");
        }
    }

    private void removeContainer(String containerId) {
        try (RemoveContainerCmd removeContainerCmd = dockerClient.removeContainerCmd(containerId)) {
            removeContainerCmd.exec();
        }
    }

    private void pullImage(Executor executor) throws InterruptedException {
        try (PullImageCmd pullImageCmd = dockerClient.pullImageCmd(executor.getContainerImage())) {
            PullImageResultCallback pullImageResultCallback = new PullImageResultCallback();
            pullImageCmd.exec(pullImageResultCallback);
            pullImageResultCallback.awaitCompletion();
        }
    }

}

