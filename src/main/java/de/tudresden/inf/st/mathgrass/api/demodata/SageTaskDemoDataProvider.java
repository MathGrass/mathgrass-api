package de.tudresden.inf.st.mathgrass.api.demodata;

import de.tudresden.inf.st.mathgrass.api.evaluator.executor.Executor;
import de.tudresden.inf.st.mathgrass.api.evaluator.evaluators.sage.SageEvaluatorPlugin;
import de.tudresden.inf.st.mathgrass.api.graph.Edge;
import de.tudresden.inf.st.mathgrass.api.graph.Graph;
import de.tudresden.inf.st.mathgrass.api.graph.GraphRepository;
import de.tudresden.inf.st.mathgrass.api.graph.Vertex;
import de.tudresden.inf.st.mathgrass.api.task.Task;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import de.tudresden.inf.st.mathgrass.api.task.hint.Hint;
import de.tudresden.inf.st.mathgrass.api.task.question.FormQuestion;
import de.tudresden.inf.st.mathgrass.api.task.question.answer.DynamicAnswer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * This class fills up the repositories with generated demo data.
 */
@Profile("demodata & sage-evaluator")
@Component
public class SageTaskDemoDataProvider {

    private final GraphRepository graphRepo;
    private final TaskRepository taskRepo;
    private final SageEvaluatorPlugin sagePlugin;

    /**
     * Constructor.
     *
     * @param graphRepo graph repository
     * @param taskRepo  task repository
     */
    public SageTaskDemoDataProvider(GraphRepository graphRepo, TaskRepository taskRepo, SageEvaluatorPlugin sagePlugin) {
        this.graphRepo = graphRepo;
        this.taskRepo = taskRepo;
        this.sagePlugin = sagePlugin;
    }


    @PostConstruct
    private void initTasks() {
        createSageTask();
        createLongSageTask();
    }

    /**
     * Generate a dynamic graph task with the Sage plugin
     */
    private void createSageTask() {
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
        graphRepo.save(graph);

        // create task
        Task demoTask1 = new Task();
        demoTask1.setGraph(graph);
        demoTask1.setLabel("Task with evaluation in Sage");

        FormQuestion question = new FormQuestion();
        question.setQuestionText("How many edges are there in the graph?");

        DynamicAnswer dynamicAnswer = new DynamicAnswer();
        String instructorExecutionDescriptor = """
                from sage.all import *
                                
                def instructor_evaluation(graph: Graph, user_answer):
                    if str(len(graph.edges())) == user_answer:
                        return True
                    else:
                        return False
                """;
        Executor executor = sagePlugin.initializeSageExecutor(instructorExecutionDescriptor, Collections.emptyList());
        dynamicAnswer.setExecutor(executor);

        Hint textHint = new Hint();
        textHint.setContent("This is hint #1.");

        Hint textHint2 = new Hint();
        textHint2.setContent("This is hint #2.");

        Hint textHint3 = new Hint();
        textHint3.setContent("This is hint #3.");

        question.setAnswer(dynamicAnswer);

        demoTask1.setQuestion(question);
        demoTask1.setHints(List.of(textHint, textHint2, textHint3));

        taskRepo.save(demoTask1);
    }

    /**
     * Generate a dynamic graph task with the Sage plugin, which takes 5 seconds to solve.
     */
    private void createLongSageTask() {
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

        // create edges
        Edge edge1 = new Edge();
        edge1.setSourceVertex(vertex1);
        edge1.setTargetVertex(vertex2);

        // add vertices and edges to graph
        graph.setVertices(List.of(vertex1, vertex2));
        graph.setEdges(List.of(edge1));
        graphRepo.save(graph);

        // create task
        Task demoTask = new Task();
        demoTask.setGraph(graph);
        demoTask.setLabel("Slow Sage Task");

        FormQuestion question = new FormQuestion();
        question.setQuestionText("What is the answer to the ultimate question of life, the universe, and everything? (No wrong answers)");

        DynamicAnswer dynamicAnswer = new DynamicAnswer();
        String instructorExecutionDescriptor = """
                from sage.all import *
                import time
                                
                def instructor_evaluation(graph: Graph, user_answer):
                    start_time = time.time()
                    while time.time() < start_time + 5:
                        pass
                    
                    return True
                """;
        Executor executor = sagePlugin.initializeSageExecutor(instructorExecutionDescriptor, Collections.emptyList());
        dynamicAnswer.setExecutor(executor);

        question.setAnswer(dynamicAnswer);

        demoTask.setQuestion(question);

        taskRepo.save(demoTask);
    }
}
