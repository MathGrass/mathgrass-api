package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.GraphApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.GraphEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.GraphRepository;
import de.tudresden.inf.st.mathgrassserver.model.Graph;
import de.tudresden.inf.st.mathgrassserver.transform.GraphTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GraphApiImpl extends AbsApi implements GraphApi {

    @Autowired
    GraphRepository graphRepository;


    @Override
    public ResponseEntity<Void> createGraph(Graph body) {

        GraphEntity entity = new GraphTransformer().toEntity(body);
        this.graphRepository.save(entity);
        return ok();
    }


    @Override
    public ResponseEntity<Graph> getGraphById(Long graphId) {

        GraphEntity graphEntity = graphRepository.getReferenceById(graphId);
        Graph graph = new GraphTransformer().toDto(graphEntity);
        return ok(graph);
    }

    @Override
    public ResponseEntity<Void> updateGraph(Long id, Graph graph) {
        //guards
        checkExistence(id,graphRepository);

        GraphEntity newEntity = new GraphTransformer().toEntity(graph);
        newEntity.setId(id);

        this.graphRepository.save(newEntity);
        return ok();
    }

}
