package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.GraphApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.GraphEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.GraphRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TagRepository;
import de.tudresden.inf.st.mathgrassserver.model.Graph;
import de.tudresden.inf.st.mathgrassserver.transform.GraphTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GraphApiImpl extends AbstractApiElement implements GraphApi {

    final GraphRepository graphRepository;
    final TagRepository tagRepository;

    public GraphApiImpl(GraphRepository graphRepository, TagRepository tagRepository) {
        this.graphRepository = graphRepository;
        this.tagRepository = tagRepository;
    }


    @Override
    public ResponseEntity<Long> createGraph(Graph body) {
        return ok(this.save(body,-1));
    }


    @Override
    public ResponseEntity<Graph> getGraphById(Long graphId) {
        GraphEntity graphEntity = graphRepository.findById(graphId).get();
        Graph graph = new GraphTransformer(this.tagRepository).toDto(graphEntity);
        return ok(graph);
    }

    @Override
    public ResponseEntity<Void> updateGraph(Long id, Graph graph) {
        //guards
        checkExistence(id,graphRepository);
        this.save(graph,id);
        return ok();
    }


    private long save(Graph graph, long id) {
        GraphEntity entity = new GraphTransformer(this.tagRepository).toEntity(graph);



        //set id if this is an update
        if (id!=-1) {
            entity.setId(id);
        }

        //save
        this.graphRepository.save(entity);

        return entity.getId();
    }

}
