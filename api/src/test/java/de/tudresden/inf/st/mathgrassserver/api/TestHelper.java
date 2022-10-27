package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.database.entity.GraphEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskSolverEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskTemplateEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.*;
import de.tudresden.inf.st.mathgrassserver.model.*;
import de.tudresden.inf.st.mathgrassserver.transform.GraphTransformer;
import de.tudresden.inf.st.mathgrassserver.transform.TaskSolverTransformer;
import de.tudresden.inf.st.mathgrassserver.transform.TaskTemplateTransformer;
import de.tudresden.inf.st.mathgrassserver.transform.TaskTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

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

    public TestHelper setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
        return this;
    }

    GraphRepository graphRepository;

    
    TaskTemplateRepository taskTemplateRepository;

    
    TaskSolverRepository taskSolverRepository;

    
    TagRepository tagRepository;

    TaskRepository taskRepository;
    
    

    public static Graph getExampleGraph() {
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
        taskTemplate.setQuestion("Count all edges!");
        taskTemplate.setHints(new ArrayList<>());
        taskTemplate.setLabel("Edge Counter");
        taskTemplate.setTags(new ArrayList<>());
        taskTemplate.setTaskSolver(solverEntity.getId());

        return taskTemplate;
    }


    public static Tag createTag() {
        Tag tag = new Tag();
        tag.setLabel("TestTag");
        return tag;
    }


    public TaskCollection prepareTaskCollection() {
        Task task1 = prepareExampleDynamicTask();
        task1.setLabel("Task 1");
        Task task2 = prepareExampleDynamicTask();
        task2.setLabel("Task 2");
        TaskEntity taskEntity1 = taskRepository.save(new TaskTransformer(taskSolverRepository,graphRepository,tagRepository,taskTemplateRepository).toEntity(task1));
        task1.setId(taskEntity1.getId());
        TaskEntity taskEntity2 = taskRepository.save(new TaskTransformer(taskSolverRepository,graphRepository,tagRepository,taskTemplateRepository).toEntity(task2));
        task2.setId(taskEntity2.getId());

        taskEntity1 = taskRepository.save(new TaskTransformer(taskSolverRepository,graphRepository,tagRepository,taskTemplateRepository).toEntity(task1));
        task1.setId(taskEntity1.getId());
        taskEntity2 = taskRepository.save(new TaskTransformer(taskSolverRepository,graphRepository,tagRepository,taskTemplateRepository).toEntity(task2));
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
        GraphEntity graphEntity = new GraphTransformer(tagRepository).toEntity(graph);
        graphEntity = graphRepository.save(graphEntity);
        graph.setId(graphEntity.getId());


        Task task = new Task();
        task.setGraph(graph);
        task.setLabel("Test Task");
        task.setQuestion("Count all edges!");

        return task;
    }

    public Task prepareExampleDynamicTask() {
        // create and save task solver
        Task task = getTaskWithSavedGraph();

        //create template
        TaskTemplate taskTemplate = prepareTaskTemplate();
        TaskTemplateEntity taskTemplateEntity = taskTemplateRepository.save(new TaskTemplateTransformer(taskSolverRepository).toEntity(taskTemplate));

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
