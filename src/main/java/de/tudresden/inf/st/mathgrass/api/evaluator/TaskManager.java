package de.tudresden.inf.st.mathgrass.api.evaluator;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.core.DockerClientBuilder;
import de.tudresden.inf.st.mathgrass.api.evaluator.executor.Executor;

public class TaskManager {

    private TaskManager() {

    }

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
     * @param taskId ID of task
     * @param answer answer given via input
     */
    public static boolean runTaskSynchronously(long taskId, String answer, Executor executor) {
        try (PullImageCmd pullImageCmd = getDockerClient().pullImageCmd(executor.getContainerImage())) {
            try (CreateContainerCmd createContainerCmd =
                         getDockerClient().createContainerCmd(executor.getContainerImage())) {
                CreateContainerResponse container = createContainerCmd.exec();
                try (StartContainerCmd startContainerCmd = getDockerClient().startContainerCmd(container.getId())) {
                    startContainerCmd.exec();
                    WaitContainerResultCallback callback = new WaitContainerResultCallback();
                    try (WaitContainerCmd waitContainerCmd = getDockerClient().waitContainerCmd(container.getId())) {
                        waitContainerCmd.exec(callback);
                        var sc = callback.awaitStatusCode();
                        // prototyping: exit code == 0 implies answer is correct
                        return Integer.valueOf(0).equals(sc);
                    }
                }
            }

        }
    }
}
