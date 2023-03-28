package de.tudresden.inf.st.mathgrass.api.evaluator;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PullImageResultCallback;
import de.tudresden.inf.st.mathgrass.api.evaluator.executor.Executor;
import de.tudresden.inf.st.mathgrass.api.graph.Edge;
import de.tudresden.inf.st.mathgrass.api.graph.Graph;
import de.tudresden.inf.st.mathgrass.api.graph.Vertex;
import de.tudresden.inf.st.mathgrass.api.task.Task;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import de.tudresden.inf.st.mathgrass.api.task.question.FormQuestion;
import de.tudresden.inf.st.mathgrass.api.task.question.answer.DynamicAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles(profiles = "dev")
class TaskManagerTest {
    public static final long TASK_ID = 11;
    @Autowired
    private TaskManager taskManager;
    public static final String MINIMAL_IMAGE = "alpine:latest";
    @MockBean
    private TaskRepository taskRepository;
    @Autowired
    private DockerClient dockerClient;
    private Executor executor;


    @BeforeEach
    void initTaskrepo() {
        // create graph entity
        Graph graph = new Graph();

        // create vertices
        Vertex vertex1 = new Vertex();
        vertex1.setLabel("1");
        vertex1.setX(10);
        vertex1.setY(10);

        Vertex vertex2 = new Vertex();
        vertex2.setLabel("2");
        vertex2.setX(50);
        vertex2.setY(50);

        Vertex vertex3 = new Vertex();
        vertex3.setLabel("3");
        vertex3.setX(50);
        vertex3.setY(30);

        Vertex vertex4 = new Vertex();
        vertex4.setLabel("4");
        vertex4.setX(70);
        vertex4.setY(10);

        // create edges
        Edge edge1 = new Edge();
        edge1.setSourceVertex(vertex1);
        edge1.setTargetVertex(vertex2);

        Edge edge2 = new Edge();
        edge2.setSourceVertex(vertex2);
        edge2.setTargetVertex(vertex3);

        Edge edge3 = new Edge();
        edge3.setSourceVertex(vertex3);
        edge3.setTargetVertex(vertex4);

        Edge edge4 = new Edge();
        edge4.setSourceVertex(vertex4);
        edge4.setTargetVertex(vertex1);

        // add vertices and edges to graph
        graph.setVertices(List.of(vertex1, vertex2, vertex3, vertex4));
        graph.setEdges(List.of(edge1, edge2, edge3, edge4));

        // create task
        Task demoTask1 = new Task();
        demoTask1.setGraph(graph);
        demoTask1.setLabel("Task with evaluation in Sage");

        FormQuestion question = new FormQuestion();
        question.setQuestionText("How many edges are there in the graph?");

        DynamicAnswer dynamicAnswer = new DynamicAnswer();
        executor = new Executor();
        executor.setContainerImage(MINIMAL_IMAGE);
        executor.setCustomEntrypoint("echo");
        dynamicAnswer.setExecutor(executor);

        question.setAnswer(dynamicAnswer);

        demoTask1.setQuestion(question);
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(demoTask1));
    }

    @Test
    void runTaskSmokeTest() throws IOException, InterruptedException {
        // pull alpine image
        try (PullImageCmd pullImageCmd = dockerClient.pullImageCmd(executor.getContainerImage())) {
            PullImageResultCallback pullImageResultCallback = new PullImageResultCallback();
            pullImageCmd.exec(pullImageResultCallback);
            pullImageResultCallback.awaitCompletion();
        }
        boolean result = taskManager.runTaskSynchronously(TASK_ID, "", executor);
        assertTrue(result);
    }

}