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


    public boolean runTaskSynchronously(long taskId, String answer, Executor executor) throws IOException {
        Optional<Task> graphOpt = taskRepository.findById(taskId);
        String tempGraphPath = "./temp/" + UUID.randomUUID().toString() + "/" + UUID.randomUUID().toString();
        tempGraphPath = "./temp/graph.json";
        Path tempGraphPath2 = Path.of(tempGraphPath).toAbsolutePath();
        String absoluteTempGraphPath = tempGraphPath2.toString();

        if (graphOpt.isPresent()) {
            Graph graph = graphOpt.get().getGraph();
            ObjectMapper objectMapper = new ObjectMapper();
            GraphTransformer transformer = new GraphTransformer(labelRepository);
            GraphDTO graphDTO = transformer.toDto(graph);
            objectMapper.writeValue(new File(tempGraphPath), graphDTO);
        }
        try (PullImageCmd pullImageCmd = dockerClient.pullImageCmd(executor.getContainerImage())) {
        }
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
                    }
                    // prototyping: exit code == 0 implies answer is correct
                    Files.deleteIfExists(Path.of(tempGraphPath));
                    return Integer.valueOf(0).equals(sc);
                }
            }
        }


    }

}

