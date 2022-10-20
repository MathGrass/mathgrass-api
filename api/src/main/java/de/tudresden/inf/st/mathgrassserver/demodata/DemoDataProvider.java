package de.tudresden.inf.st.mathgrassserver.demodata;

import de.tudresden.inf.st.mathgrassserver.database.entity.*;
import de.tudresden.inf.st.mathgrassserver.database.repository.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Profile("demodata")
@Component
public class DemoDataProvider {
    private final GraphRepository graphRepo;
    private final TaskRepository taskRepo;
    private final TagRepository tagRepo;
    private final TaskTemplateRepository taskTemplateRepo;
    private final  TaskSolverRepository taskSolverRepo;


    public DemoDataProvider(GraphRepository graphRepo, TaskRepository taskRepo, TagRepository tagRepo, TaskTemplateRepository taskTemplateRepo, TaskSolverRepository taskSolverRepo) {
        this.graphRepo = graphRepo;
        this.taskRepo = taskRepo;
        this.tagRepo = tagRepo;
        this.taskTemplateRepo = taskTemplateRepo;
        this.taskSolverRepo = taskSolverRepo;
    }

    @PostConstruct
    private void initGraphs() {
        if (!taskRepo.findAll().isEmpty()) {
            return;
        }

        addDynamicGraphTask();
        addSimpleGraph();

    }

    private void addDynamicGraphTask() {
        GraphEntity graph1 = new GraphEntity();
        graph1.setLabel("label1");
        TagEntity e1 = new TagEntity();
        e1.setLabel("tag1");
        tagRepo.save(e1);
        graph1.setTags(List.of(e1));

        VertexEntity vertex1 = new VertexEntity();
        vertex1.setLabel("1");
        vertex1.setX(10);
        vertex1.setY(10);

        VertexEntity vertex2 = new VertexEntity();
        vertex2.setLabel("2");
        vertex2.setX(50);
        vertex2.setY(50);

        VertexEntity vertex3 = new VertexEntity();
        vertex3.setLabel("3");
        vertex3.setX(50);
        vertex3.setY(30);

        VertexEntity vertex4 = new VertexEntity();
        vertex4.setLabel("4");
        vertex4.setX(70);
        vertex4.setY(10);

        EdgeEntity edge1 = new EdgeEntity();
        edge1.setV1(vertex1);
        edge1.setV2(vertex2);

        EdgeEntity edge2 = new EdgeEntity();
        edge2.setV1(vertex2);
        edge2.setV2(vertex3);

        EdgeEntity edge3 = new EdgeEntity();
        edge3.setV1(vertex3);
        edge3.setV2(vertex4);

        EdgeEntity edge4 = new EdgeEntity();
        edge4.setV1(vertex4);
        edge4.setV2(vertex1);

        graph1.setVertices(List.of(vertex1, vertex2, vertex3, vertex4));
        graph1.setEdges(List.of(edge1, edge2, edge3, edge4));
        graphRepo.save(graph1);

        TaskEntity demoTask1 = new TaskEntity();
        demoTask1.setGraph(graph1);
        demoTask1.setLabel("Task with evaluation in Sage");

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

        TaskTemplateEntity taskTemplateEntity = new TaskTemplateEntity();
        taskTemplateEntity.setLabel("task template label");
        taskTemplateEntity.setTaskSolver(taskSolver);
        taskTemplateEntity.setQuestion("How many edges does the graph have? (evaluation via Sage)");
        taskTemplateRepo.save(taskTemplateEntity);
        demoTask1.setTaskTemplate(taskTemplateEntity);

        taskRepo.save(demoTask1);
    }

    private void addSimpleGraph() {
        GraphEntity graph1 = new GraphEntity();
        graph1.setLabel("label1");
        TagEntity e1 = new TagEntity();
        e1.setLabel("tag1");
        tagRepo.save(e1);
        graph1.setTags(List.of(e1));

        VertexEntity vertex1 = new VertexEntity();
        final String LABEL_SOURCE = "1";
        vertex1.setLabel(LABEL_SOURCE);
        vertex1.setX(20);
        vertex1.setY(20);

        VertexEntity vertex2 = new VertexEntity();
        vertex2.setLabel("2");
        vertex2.setX(60);
        vertex2.setY(60);

        EdgeEntity edge1 = new EdgeEntity();
        edge1.setV1(vertex1);
        edge1.setV2(vertex2);

        graph1.setVertices(List.of(vertex1, vertex2));
        graph1.setEdges(List.of(edge1));
        graphRepo.save(graph1);

        TaskEntity demoTask1 = new TaskEntity();
        demoTask1.setGraph(graph1);
        demoTask1.setLabel("Task with simple evaluation");
        demoTask1.setQuestion("What's the label of the source vertex?");
        demoTask1.setAnswer(LABEL_SOURCE);

        taskRepo.save(demoTask1);
    }
}
