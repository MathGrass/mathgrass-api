package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.database.entity.GraphEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.GraphRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TagRepository;
import de.tudresden.inf.st.mathgrassserver.model.Graph;
import de.tudresden.inf.st.mathgrassserver.transform.GraphTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static de.tudresden.inf.st.mathgrassserver.api.TestHelper.getExampleGraph;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GraphApiImplTest {

    TestHelper testHelper;

    @Autowired
    GraphApiImpl graphApiImpl;

    @Autowired
    GraphRepository graphRepository;

    @Autowired
    TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        assertThat(graphApiImpl).isNotNull();
        testHelper = new TestHelper()
                .setTagRepository(tagRepository)
        .setTagRepository(tagRepository);
    }

    @Test
    void createGraph() {
        Graph graph = getExampleGraph();
        long id = graphApiImpl.createGraph(graph).getBody();
        assertThat(id).isNotNull();

        GraphEntity graphEntity = graphRepository.findById(id).orElse(null);
        assertThat(graphEntity).isNotNull();

        assertEquals(graph.getLabel(), graphEntity.getLabel());
    }

    @Test
    void getGraphById() {
        long idNotFound = 4;
        try {
            graphRepository.deleteById(idNotFound);
        } catch (Exception e) {}
        try {
            var x = graphApiImpl.getGraphById(idNotFound);
            assertEquals(404,x.getStatusCode().value());
        }
        catch (Exception e) {

        }


        Graph graph = getExampleGraph();
        GraphEntity entity = graphRepository.save(new GraphTransformer(tagRepository).toEntity(graph));

        //get
        Graph check = graphApiImpl.getGraphById(entity.getId()).getBody();

        //check
        assertThat(check).isNotNull();
        assertEquals(graph.getLabel(), check.getLabel());



    }

    @Test
    void updateGraph() {
        //create
        Graph graph = getExampleGraph();
        GraphEntity entity = graphRepository.save(new GraphTransformer(tagRepository).toEntity(graph));

        //update
        graphApiImpl.updateGraph(entity.getId(),graph);

        //check
        GraphEntity check = graphRepository.findById(entity.getId()).orElse(null);
        assertThat(check).isNotNull();
        assertEquals(graph.getLabel(),check.getLabel());

    }
}