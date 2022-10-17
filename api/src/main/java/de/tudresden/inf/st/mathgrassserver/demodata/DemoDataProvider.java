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
    public static final String DEMO_TASK_LABEL = "Demo Task";
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
        if (taskRepo.findAll().stream().anyMatch(taskEntity -> taskEntity.getLabel().equals(DEMO_TASK_LABEL))) {
            return;
        }

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
        vertex2.setLabel("1");
        vertex2.setX(50);
        vertex2.setY(50);

        EdgeEntity edge1 = new EdgeEntity();
        edge1.setV1(vertex1);
        edge1.setV2(vertex2);

        graph1.setVertices(List.of(vertex1, vertex2));
        graph1.setEdges(List.of(edge1));
        graphRepo.save(graph1);

        TaskEntity demoTask1 = new TaskEntity();
        demoTask1.setGraph(graph1);
        demoTask1.setLabel(DEMO_TASK_LABEL);
        demoTask1.setQuestion("How many edges does the graph have? (question in task");

        TaskSolverEntity taskSolver = new TaskSolverEntity();
        taskSolver.setLabel("task solver label");
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
        taskTemplateEntity.setQuestion("Howw many edges does the graph have? (question in task template)");
        taskTemplateRepo.save(taskTemplateEntity);
        demoTask1.setTaskTemplate(taskTemplateEntity);

        taskRepo.save(demoTask1);

    }
}
