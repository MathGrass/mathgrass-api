package de.tudresden.inf.st.mathgrass.api.evaluator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;
import de.tudresden.inf.st.mathgrass.api.evaluator.executor.Executor;
import de.tudresden.inf.st.mathgrass.api.graph.Graph;
import de.tudresden.inf.st.mathgrass.api.graph.GraphTransformer;
import de.tudresden.inf.st.mathgrass.api.label.LabelRepository;
import de.tudresden.inf.st.mathgrass.api.model.GraphDTO;
import de.tudresden.inf.st.mathgrass.api.task.Task;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskManager {
    private final DockerClient dockerClient;
    private final TaskRepository taskRepository;
    private final LabelRepository labelRepository;

    public TaskManager(TaskRepository taskRepository, LabelRepository labelRepository) {
        dockerClient = DockerClientBuilder.getInstance().build();
        this.taskRepository = taskRepository;
        this.labelRepository = labelRepository;
    }


    public boolean runTaskSynchronously(long taskId, String answer, Executor executor) throws IOException,
            InterruptedException {
        Optional<Task> graphOpt = taskRepository.findById(taskId);

        Path tempPath =
                Path.of("." + File.separator + "temp" + File.separator + UUID.randomUUID() + ".json");
        String absoluteTempGraphPath = tempPath.toAbsolutePath().toString();

        createTempGraphFile(graphOpt, absoluteTempGraphPath);
        pullImage(executor);
        return createRunAndRemoveContainer(answer, executor, tempPath, absoluteTempGraphPath);


    }

    private void createTempGraphFile(Optional<Task> graphOpt, String absoluteTempGraphPath) throws IOException {
        if (graphOpt.isPresent()) {
            Graph graph = graphOpt.get().getGraph();
            ObjectMapper objectMapper = new ObjectMapper();
            GraphTransformer transformer = new GraphTransformer(labelRepository);
            GraphDTO graphDTO = transformer.toDto(graph);
            File resultFile = new File(absoluteTempGraphPath);
            resultFile.getParentFile().mkdirs();
            objectMapper.writeValue(resultFile, graphDTO);
        }
    }

    private boolean createRunAndRemoveContainer(String answer, Executor executor, Path tempPath,
                                                String absoluteTempGraphPath) throws IOException {
        String containerCmd = "sage /sage-evaluation/main.py \"" + answer + "\"";
        Bind graphTempFileBind = new Bind(absoluteTempGraphPath, new Volume("/sage-evaluation/graph.json"));
        // bind temp file to  /sage-evaluation/graph.json
        HostConfig hostConfig = new HostConfig();
        hostConfig.withBinds(graphTempFileBind);
        try (CreateContainerCmd createContainerCmd =
                     dockerClient.createContainerCmd(executor.getContainerImage()).withCmd(containerCmd).withHostConfig(hostConfig)) {
            CreateContainerResponse container = createContainerCmd.exec();
            try (StartContainerCmd startContainerCmd = dockerClient.startContainerCmd(container.getId())) {
                startContainerCmd.exec();
                WaitContainerResultCallback callback = new WaitContainerResultCallback();
                try (WaitContainerCmd waitContainerCmd = dockerClient.waitContainerCmd(container.getId())) {
                    waitContainerCmd.exec(callback);
                    var sc = callback.awaitStatusCode();
                    try (RemoveContainerCmd removeContainerCmd =
                                 dockerClient.removeContainerCmd(container.getId())) {
                        removeContainerCmd.exec();
                    }
                    // prototyping: exit code == 0 implies answer is correct
                    Files.deleteIfExists(tempPath);
                    return Integer.valueOf(0).equals(sc);
                }
            }
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

