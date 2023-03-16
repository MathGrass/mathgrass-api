package de.tudresden.inf.st.mathgrass.api.evaluator;

import de.tudresden.inf.st.mathgrass.api.evaluator.executor.Executor;
import de.tudresden.inf.st.mathgrass.api.graph.Edge;
import de.tudresden.inf.st.mathgrass.api.graph.Graph;
import de.tudresden.inf.st.mathgrass.api.graph.Vertex;
import de.tudresden.inf.st.mathgrass.api.label.Label;
import de.tudresden.inf.st.mathgrass.api.task.Task;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskManagerTest {
    public static final long TASK_ID = 11;
    @Autowired
    private TaskManager taskManager;
    @MockBean
    private TaskRepository taskRepository;

    @BeforeEach
    void initTaskrepo() {
        Graph graph = new Graph();
        graph.setId(1L);
        graph.setLabels(List.of("testManagerTest"));
        Label e1 = new Label();
        e1.setValue("testManagerTest");
        graph.setTags(List.of(e1));

        // create vertices
        Vertex vertex1 = new Vertex();
        vertex1.setId(1L);
        vertex1.setLabel("1");
        vertex1.setX(10);
        vertex1.setY(10);

        Vertex vertex2 = new Vertex();
        vertex2.setId(2L);
        vertex2.setLabel("2");
        vertex2.setX(50);
        vertex2.setY(50);

        Vertex vertex3 = new Vertex();
        vertex3.setId(3L);
        vertex3.setLabel("3");
        vertex3.setX(50);
        vertex3.setY(30);

        Vertex vertex4 = new Vertex();
        vertex4.setId(4L);
        vertex4.setLabel("4");
        vertex4.setX(70);
        vertex4.setY(10);

        // create edges
        Edge edge1 = new Edge();
        edge1.setId(10L);
        edge1.setSourceVertex(vertex1);
        edge1.setTargetVertex(vertex2);

        Edge edge2 = new Edge();
        edge1.setId(11L);
        edge2.setSourceVertex(vertex2);
        edge2.setTargetVertex(vertex3);

        Edge edge3 = new Edge();
        edge1.setId(12L);
        edge3.setSourceVertex(vertex3);
        edge3.setTargetVertex(vertex4);

        Edge edge4 = new Edge();
        edge1.setId(13L);
        edge4.setSourceVertex(vertex4);
        edge4.setTargetVertex(vertex1);

        // add vertices and edges to graph
        graph.setVertices(List.of(vertex1, vertex2, vertex3, vertex4));
        graph.setEdges(List.of(edge1, edge2, edge3, edge4));

        Task task = new Task();
        task.setId(123L);
        task.setLabel("this is my label");
        task.setGraph(graph);
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
    }

    @Test
    void runTaskSmokeTest() throws IOException, InterruptedException {
        Executor executor = new Executor();
        executor.setContainerImage("sage-evaluator");
        boolean result = false;
        result = taskManager.runTaskSynchronously(TASK_ID, "4", executor);
        assertTrue(result);

    }

}