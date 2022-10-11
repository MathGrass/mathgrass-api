package de.tudresden.inf.st.mathgrassserver.demodata;

import de.tudresden.inf.st.mathgrassserver.database.entity.*;
import de.tudresden.inf.st.mathgrassserver.database.repository.GraphRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TagRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskCollectionRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskRepository;
import de.tudresden.inf.st.mathgrassserver.model.Edge;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Profile("demodata")
@Component
public class DemoDataProvider {
    private GraphRepository graphRepo;
    private TaskRepository taskRepo;
    private TagRepository tagRepo;
    private TaskCollectionRepository taskCollectionRepo;

    public DemoDataProvider(GraphRepository graphRepo, TaskRepository taskRepo, TagRepository tagRepo, TaskCollectionRepository taskCollectionRepo) {
        this.graphRepo = graphRepo;
        this.taskRepo = taskRepo;
        this.tagRepo = tagRepo;
        this.taskCollectionRepo = taskCollectionRepo;
    }

    @PostConstruct
    private List<Long> initGraphs(){
        List<Long> graphIds = new ArrayList<>();

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
        graphIds.add(graph1.getId());

        return graphIds;
    }
}
