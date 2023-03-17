package de.tudresden.inf.st.mathgrass.api.demodata;

import de.tudresden.inf.st.mathgrass.api.evaluator.executor.Executor;
import de.tudresden.inf.st.mathgrass.api.evaluator.executor.SourceFile;
import de.tudresden.inf.st.mathgrass.api.evaluator.sage.SageEvaluator;
import de.tudresden.inf.st.mathgrass.api.graph.Edge;
import de.tudresden.inf.st.mathgrass.api.graph.Graph;
import de.tudresden.inf.st.mathgrass.api.graph.GraphRepository;
import de.tudresden.inf.st.mathgrass.api.graph.Vertex;
import de.tudresden.inf.st.mathgrass.api.task.Task;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import de.tudresden.inf.st.mathgrass.api.task.question.FormQuestion;
import de.tudresden.inf.st.mathgrass.api.task.question.answer.DynamicAnswer;
import de.tudresden.inf.st.mathgrass.api.task.question.answer.StaticAnswer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * This class fills up the repositories with generated demo data.
 */
@Profile("demodata")
@Component
public class DemoDataProvider {

    private final GraphRepository graphRepo;
    private final TaskRepository taskRepo;

    /**
     * Constructor.
     *
     * @param graphRepo graph repository
     * @param taskRepo  task repository
     */
    public DemoDataProvider(GraphRepository graphRepo,
                            TaskRepository taskRepo) {
        this.graphRepo = graphRepo;
        this.taskRepo = taskRepo;
    }

    /**
     * Initialize graphs.
     */
    @PostConstruct
    private void initGraphs() {
        // if task repository already contains elements don't do anything
        if (!taskRepo.findAll().isEmpty()) {
            return;
        }

        // create tasks
        createDynamicTask();
        createStaticTask();
    }

    /**
     * Generate a dynamic graph task.
     */
    private void createDynamicTask() {
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
        Executor executor = new Executor();
        executor.setContainerImage(SageEvaluator.SAGE_IMAGE_COMPLETE_TAG);
        SourceFile sourceFile = new SourceFile();
        String executionDescriptor = """
                from sage.all import *
                                
                def instructor_evaluation(graph: Graph, user_answer):
                    if str(len(graph.edges())) == user_answer:
                        return True
                    else:
                        return False
                """;
        sourceFile.setContents(executionDescriptor);
        sourceFile.setPath("/sage-evaluation/instructor_evaluation.py");
        executor.setCustomEntrypoint("sage /sage-evaluation/main.py");
        executor.setSourceFiles(List.of(sourceFile));
        executor.setGraphPath("/sage-evaluation/graph.json");
        dynamicAnswer.setExecutor(executor);

        question.setAnswer(dynamicAnswer);

        demoTask1.setQuestion(question);

        taskRepo.save(demoTask1);
    }

    /**
     * Generate a simple graph.
     */
    private void createStaticTask() {
        // create graph
        Graph graph = new Graph();

        // create vertices
        Vertex vertex1 = new Vertex();
        final String LABEL_SOURCE = "1";
        vertex1.setLabel(LABEL_SOURCE);
        vertex1.setX(20);
        vertex1.setY(20);

        Vertex vertex2 = new Vertex();
        vertex2.setLabel("2");
        vertex2.setX(60);
        vertex2.setY(60);

        // create edges
        Edge edge1 = new Edge();
        edge1.setSourceVertex(vertex1);
        edge1.setTargetVertex(vertex2);

        // add vertices and edges to graph
        graph.setVertices(List.of(vertex1, vertex2));
        graph.setEdges(List.of(edge1));
        graphRepo.save(graph);

        // create task
        Task demoTask1 = new Task();
        demoTask1.setGraph(graph);
        demoTask1.setLabel("Task with simple evaluation");

        FormQuestion question = new FormQuestion();
        question.setQuestionText("What's the label of the source vertex?");

        StaticAnswer answer = new StaticAnswer();
        answer.setAnswer("1");
        question.setAnswer(answer);

        demoTask1.setQuestion(question);

        taskRepo.save(demoTask1);
    }
}
