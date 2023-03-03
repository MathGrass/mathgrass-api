package de.tudresden.inf.st.mathgrass.api.demodata;

import de.tudresden.inf.st.mathgrass.api.label.LabelRepository;
import de.tudresden.inf.st.mathgrass.api.feedback.TaskSolverEntity;
import de.tudresden.inf.st.mathgrass.api.feedback.TaskSolverRepository;
import de.tudresden.inf.st.mathgrass.api.graph.Edge;
import de.tudresden.inf.st.mathgrass.api.label.Label;
import de.tudresden.inf.st.mathgrass.api.graph.Graph;
import de.tudresden.inf.st.mathgrass.api.graph.GraphRepository;
import de.tudresden.inf.st.mathgrass.api.graph.Vertex;
import de.tudresden.inf.st.mathgrass.api.task.hint.Hint;
import de.tudresden.inf.st.mathgrass.api.transform.QuestionEntity;
import de.tudresden.inf.st.mathgrass.api.transform.TaskEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * This class fills up the repositories with generated demo data.
 */
@Profile("demodata")
@Component
public class DemoDataProvider {
    /**
     * Graph repository.
     */
    private final GraphRepository graphRepo;

    /**
     * Task repository.
     */
    private final TaskRepository taskRepo;

    /**
     * Tag repository.
     */
    private final LabelRepository tagRepo;

    /**
     * Task template repository.
     */

    /**
     * Task solver repository.
     */
    private final TaskSolverRepository taskSolverRepo;

    /**
     * Constructor.
     *
     * @param graphRepo graph repository
     * @param taskRepo task repository
     * @param tagRepo tag repository
     * @param taskSolverRepo task solver repository
     */
    public DemoDataProvider(GraphRepository graphRepo, TaskRepository taskRepo, LabelRepository tagRepo, TaskSolverRepository taskSolverRepo) {
        this.graphRepo = graphRepo;
        this.taskRepo = taskRepo;
        this.tagRepo = tagRepo;
        this.taskSolverRepo = taskSolverRepo;
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
        Label e1 = new Label();
        e1.setValue("tag1");
        tagRepo.save(e1);
        graph.setTags(List.of(e1));

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
        TaskEntity demoTask1 = new TaskEntity();
        demoTask1.setGraph(graph);
        demoTask1.setLabel("Task with evaluation in Sage");

        // create task solver
        TaskSolverEntity taskSolver = new TaskSolverEntity();
        taskSolver.setLabel("task solver label");
        taskSolver.setExecutionDescriptor("""
                from sage.all import *
                                
                if __name__ == '__main__':
                    graph = Graph()
                    graph.add_vertex("1")
                    graph.add_vertex("2")
                                
                    graph.add_edge("1", "2")
                                
                    print(len(graph.edges()))
                """);
        taskSolverRepo.save(taskSolver);
        taskSolver.setExecutionDescriptor("""
                import sage.all as sage
                                
                                
                def count_edges(g: sage.Graph):
                    return len(g.edges())
                                
                                
                if __name__ == '__main__':
                    g = sage.Graph()
                    g.add_vertex("1")
                    g.add_vertex("2")
                    g.add_vertex("3")
                    g.add_vertex("4")
                                
                    g.add_edge("1", "2")
                    g.add_edge("2", "3")
                    g.add_edge("3", "4")
                                
                    print("How many edges does the graph have?")
                    print(count_edges(g))
                                
                """);


        Hint e11 = new Hint();
        e11.setContent("Asd");
        e11.setLabel("Asd");

        taskRepo.save(demoTask1);
    }

    /**
     * Generate a simple graph.
     */
    private void createStaticTask() {
        // create graph
        Graph graph = new Graph();
        Label e1 = new Label();
        e1.setValue("tag1");
        tagRepo.save(e1);
        graph.setTags(List.of(e1));

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
        TaskEntity demoTask1 = new TaskEntity();
        demoTask1.setGraph(graph);
        demoTask1.setLabel("Task with simple evaluation");

        QuestionEntity question = new QuestionEntity();
        question.setQuestion("What's the label of the source vertex?");
        question.setSimpleAnswer("1");
        question.setDynamicQuestion(false);
        demoTask1.setQuestion(question);
        demoTask1.setAnswer(LABEL_SOURCE);

        taskRepo.save(demoTask1);
    }
}
