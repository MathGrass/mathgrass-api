package de.tudresden.inf.st.mathgrass.api.evaluator;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.core.DockerClientBuilder;
import de.tudresden.inf.st.mathgrass.api.evaluator.executor.Executor;

import java.util.Map;

/**
 * This class creates {@link EvaluationRequestMessage} instances and uses the {@link MessageBrokerConn} to send
 * evaluation requests to evaluators.
 */
public class TaskManager {

    private static DockerClient dockerClient = null;

    private static DockerClient getDockerClient() {
        if (dockerClient == null) {
            return DockerClientBuilder.getInstance().build();
        }
        return dockerClient;
    }

    /**
     * Create an evaluation request message and send to evaluator.
     *
     * @param taskId    ID of task
     * @param answer    answer given via input
     */
    public static boolean runTask(long taskId, String answer, Executor executor) {
        try (PullImageCmd pullImageCmd = getDockerClient().pullImageCmd(executor.getContainerImage());) {
            try (CreateContainerCmd createContainerCmd =
                         getDockerClient().createContainerCmd(executor.getContainerImage());) {
                CreateContainerResponse container = createContainerCmd.exec();
                try (StartContainerCmd startContainerCmd = getDockerClient().startContainerCmd(container.getId())) {
                    startContainerCmd.exec();
                    try (InspectContainerCmd inspectContainerCmd =
                                 getDockerClient().inspectContainerCmd(container.getId())) {
                        InspectContainerResponse resp = inspectContainerCmd.exec();
                        return Long.valueOf(0).equals(resp.getState().getExitCodeLong());
                    }

                }
            }
        }

    }
}
