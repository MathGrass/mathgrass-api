package de.tudresden.inf.st.mathgrass.api.api;

import de.tudresden.inf.st.mathgrass.api.database.entity.GraphEntity;
import de.tudresden.inf.st.mathgrass.api.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrass.api.database.entity.TaskSolverEntity;
import de.tudresden.inf.st.mathgrass.api.database.entity.TaskTemplateEntity;
import de.tudresden.inf.st.mathgrass.api.database.repository.*;
import de.tudresden.inf.st.mathgrass.api.model.*;
import de.tudresden.inf.st.mathgrass.api.database.repository.*;
import de.tudresden.inf.st.mathgrass.api.model.*;
import de.tudresden.inf.st.mathgrass.api.transform.GraphTransformer;
import de.tudresden.inf.st.mathgrass.api.transform.TaskSolverTransformer;
import de.tudresden.inf.st.mathgrass.api.transform.TaskTemplateTransformer;
import de.tudresden.inf.st.mathgrass.api.transform.TaskTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestHelper {
    
    


    public TestHelper setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        return this;

    }

    public TestHelper setGraphRepository(GraphRepository graphRepository) {
        this.graphRepository = graphRepository;
        return this;
    }

    public TestHelper setTaskTemplateRepository(TaskTemplateRepository taskTemplateRepository) {
        this.taskTemplateRepository = taskTemplateRepository;
        return this;
    }

    public TestHelper setTaskSolverRepository(TaskSolverRepository taskSolverRepository) {
        this.taskSolverRepository = taskSolverRepository;
        return this;
    }

    public TestHelper setTagRepository(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
        return this;
    }

    GraphRepository graphRepository;

    
    TaskTemplateRepository taskTemplateRepository;

    
    TaskSolverRepository taskSolverRepository;

    
    LabelRepository labelRepository;

    TaskRepository taskRepository;
    
    

    public static Graph getExampleGraph() {
        Graph graph = new Graph();
        Vertex v1 = new Vertex();
        v1.setX(10);
        v1.setY(20);

        Vertex v2 = new Vertex();
        v2.setX(10);
        v2.setY(1);

        Vertex v3 = new Vertex();
        v3.setX(5);
        v3.setY(2);

        Vertex v4 = new Vertex();
        v4.setX(20);
        v4.setY(7);

        Vertex v5 = new Vertex();
        v5.setX(16);
        v5.setY(20);

        Vertex v6 = new Vertex();
        v6.setX(35);
        v6.setY(25);


        graph.setVertices(new ArrayList<>(Arrays.asList(v1,v2,v3,v4,v5,v6)));


        Edge edge1 = new Edge();
        edge1.setFirstVertex(v1);
        edge1.setSecondVertex(v2);

        Edge edge2 = new Edge();
        edge2.setFirstVertex(v1);
        edge2.setSecondVertex(v3);

        Edge edge3 = new Edge();
        edge3.setFirstVertex(v3);
        edge3.setSecondVertex(v4);

        Edge edge4 = new Edge();
        edge4.setFirstVertex(v2);
        edge4.setSecondVertex(v5);

        Edge edge5 = new Edge();
        edge5.setFirstVertex(v2);
        edge5.setSecondVertex(v6);

        graph.setEdges(new ArrayList<>(List.of(edge1,edge2,edge3,edge4,edge5)));

        return graph;
    }

    public static TaskSolver getExampleTaskSolver() {
        String script = "print(True)";
        TaskSolver taskSolver = new TaskSolver();
        taskSolver.setExecutionDescriptor(script);
        taskSolver.setLabel("Solver of the month");
        return taskSolver;
    }


    public TaskTemplate prepareTaskTemplate() {
        TaskSolver taskSolver = getExampleTaskSolver();
        TaskSolverEntity solverEntity = taskSolverRepository.save(new TaskSolverTransformer().toEntity(taskSolver));

        TaskTemplate taskTemplate = new TaskTemplate();
        taskTemplate.setQuestion("Zähle alle Kanten");
        taskTemplate.setHints(new ArrayList<>());
        taskTemplate.setLabel("Kantenzähler");
        taskTemplate.setLabels(new ArrayList<>());
        taskTemplate.setTaskSolver(solverEntity.getId());

        return taskTemplate;
    }


    public static Label createLabel() {
        Label label = new Label();
        label.setLabel("TestTag");
        return label;
    }


    public TaskCollection prepareTaskCollection() {
        Task task1 = prepareExampleDynamicTask();
        task1.setLabel("Task 1");
        Task task2 = prepareExampleDynamicTask();
        task2.setLabel("Task 2");
        TaskEntity taskEntity1 = taskRepository.save(new TaskTransformer(taskSolverRepository,graphRepository, labelRepository,taskTemplateRepository).toEntity(task1));
        task1.setId(taskEntity1.getId());
        TaskEntity taskEntity2 = taskRepository.save(new TaskTransformer(taskSolverRepository,graphRepository, labelRepository,taskTemplateRepository).toEntity(task2));
        task2.setId(taskEntity2.getId());

        taskEntity1 = taskRepository.save(new TaskTransformer(taskSolverRepository,graphRepository, labelRepository,taskTemplateRepository).toEntity(task1));
        task1.setId(taskEntity1.getId());
        taskEntity2 = taskRepository.save(new TaskTransformer(taskSolverRepository,graphRepository, labelRepository,taskTemplateRepository).toEntity(task2));
        task2.setId(taskEntity2.getId());


        TaskCollection taskCollection = new TaskCollection();
        taskCollection.setTasks(Arrays.asList(task1.getId(),task2.getId()));
        taskCollection.setLabel("Test Collection");
        return taskCollection;
    }

    public static TaskTopic getExampleTaskTopic() {
        TaskTopic taskTopic = new TaskTopic();
        taskTopic.setLabel("Test Topic");

        return taskTopic;
    }

    private Task getTaskWithSavedGraph() {
        Graph graph = getExampleGraph();
        GraphEntity graphEntity = new GraphTransformer(labelRepository).toEntity(graph);
        graphEntity = graphRepository.save(graphEntity);
        graph.setId(graphEntity.getId());

        TaskHint taskHint1 = new TaskHint();
        taskHint1.setLabel("Erster Tipp");
        taskHint1.setContent("Nicht die Knoten zählen");

        TaskHint taskHint2 = new TaskHint();
        taskHint2.setLabel("Zweiter Tipp");
        taskHint2.setContent("Es sind 6.");


        Task task = new Task();
        task.setGraph(graph);
        task.setHints(Arrays.asList(taskHint1,taskHint2));
        task.setLabel("Kanten zählen");
        task.setQuestion(new Question().question("Wie viele Kanten gibt es?"));
        task.setAnswer("5");

        return task;
    }

    public Task prepareExampleDynamicTask() {
        // create and save task solver
        Task task = getTaskWithSavedGraph();
        task.setQuestion(null);
        task.setAnswer(null);

        //create template
        TaskTemplate taskTemplate = prepareTaskTemplate();
        TaskTemplateEntity taskTemplateEntity = taskTemplateRepository.save(new TaskTemplateTransformer(taskSolverRepository).toEntity(taskTemplate));
        taskTemplateEntity = taskTemplateRepository.save(taskTemplateEntity);

        //create task
        task.setTemplate(new TaskTemplateTransformer(taskSolverRepository).toDto(taskTemplateEntity));

        return task;

    }

    public Task prepareExampleStaticTask() {
        // create and save task solver
        Task task = getTaskWithSavedGraph();
        return task;
    }



}
