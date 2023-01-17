package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.*;
import de.tudresden.inf.st.mathgrassserver.model.*;
import de.tudresden.inf.st.mathgrassserver.transform.TaskTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EvaluatorApiImplTest {

    @Autowired
    TaskApiImpl taskApiImpl;

    @Autowired
    EvaluatorApiImpl evaluatorApiImpl;

    @Autowired
    GraphApiImpl graphApiImpl;

    @Autowired
    TaskSolverApiImpl taskSolverApiImpl;

    @Autowired
    TaskTemplateApiImpl taskTemplateApiImpl;

    TestHelper testHelper;

    @BeforeEach
    void setUp() {
        testHelper = new TestHelper();
        testHelper.setGraphRepository(graphRepository);
        testHelper.setTaskSolverRepository(taskSolverRepository);
        testHelper.setTaskTemplateRepository(taskTemplateRepository);
        testHelper.setTagRepository(labelRepository);
    }

    @Test
    void testRunTask() {
        assertThat(taskApiImpl).isNotNull();


        // create graph
        Graph graph = new Graph();
        Vertex v1 = new Vertex();
        v1.setX(10);
        v1.setY(20);

        Vertex v2 = new Vertex();
        v2.setX(10);
        v2.setY(1);
        graph.setVertices(new ArrayList<>(Arrays.asList(v1,v2)));


        Edge edge = new Edge();
        edge.setFirstVertex(v1);
        edge.setSecondVertex(v2);
        graph.setEdges(new ArrayList<>(List.of(edge)));

        long graphId = graphApiImpl.createGraph(graph).getBody();
        graph.setId(graphId);

        //create task solver
        String script = "print(True)";
        TaskSolver taskSolver = new TaskSolver();
        taskSolver.setExecutionDescriptor(script);
        taskSolver.setLabel("Solver of the month");

        long taskSolverId = taskSolverApiImpl.createTaskSolver(taskSolver).getBody();
        taskSolver.setId(taskSolverId);

        // create task template
        TaskTemplate taskTemplate = new TaskTemplate();
        taskTemplate.setTaskSolver(taskSolverId);
        taskTemplate.setQuestion("Count all edges!");
        taskTemplate.setHints(new ArrayList<>());
        taskTemplate.setLabel("Edge Counter");
        taskTemplate.setLabels(new ArrayList<>());

        long taskTemplateId = taskTemplateApiImpl.createTaskTemplate(taskTemplate).getBody();
        taskTemplate.setId(taskTemplateId);



        // create task
        Task task = new Task();
        task.setGraph(graph);
        task.setTemplate(taskTemplate);
        task.setLabel("Aufgabe 1");
        task.setHints(new ArrayList<>());
        task.setFeedback(new ArrayList<>());
        long taskId = taskApiImpl.createTask(task).getBody();

        // call task evaluator
        evaluatorApiImpl.runTask(taskId,"3");

    }

    @Autowired
    TaskSolverRepository taskSolverRepository;

    @Autowired
    GraphRepository graphRepository;

    @Autowired
    LabelRepository labelRepository;

    @Autowired
    TaskTemplateRepository taskTemplateRepository;

    @Autowired
    TaskRepository taskRepository;

    @Test
    public void runTask() {
        Task task = testHelper.prepareExampleDynamicTask();
        TaskEntity taskEntity = new TaskTransformer(taskSolverRepository,graphRepository, labelRepository,taskTemplateRepository).toEntity(task);
        long id = taskRepository.save(taskEntity).getId();

        evaluatorApiImpl.runTask(id, "3");

    }


}
