package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.GraphApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.GraphEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.GraphRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.LabelRepository;
import de.tudresden.inf.st.mathgrassserver.model.Graph;
import de.tudresden.inf.st.mathgrassserver.transform.GraphTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * This class can create/update/load/save {@link Graph}s.
 */
@RestController
public class GraphApiImpl extends AbstractApiElement implements GraphApi {
    /**
     * Graph repository.
     */
    final GraphRepository graphRepository;
    /**
     * Tag repository.
     */
    final LabelRepository labelRepository;

    /**
     * Constructor.
     *
     * @param graphRepository graph repository
     * @param labelRepository tag repository
     */
    public GraphApiImpl(GraphRepository graphRepository, LabelRepository labelRepository) {
        this.graphRepository = graphRepository;
        this.labelRepository = labelRepository;
    }

    /**
     * Create a graph and return its ID.
     *
     * @param body graph
     * @return Response with Graph ID
     */
    @Override
    public ResponseEntity<Long> createGraph(Graph body) {
        return ok(this.save(body,-1));
    }

    /**
     * Get a graph with given ID.
     *
     * @param graphId ID of graph
     * @return Response with graph
     */
    @Override
    public ResponseEntity<Graph> getGraphById(Long graphId) {
        Optional<GraphEntity> optGraphEntity = graphRepository.findById(graphId);

        if (optGraphEntity.isPresent()) {
            Graph graph = new GraphTransformer(this.labelRepository).toDto(optGraphEntity.get());
            return ok(graph);
        } else {
            return notFound();
        }
    }

    /**
     * Update the graph entity in the database of a graph with given ID.
     *
     * @param id ID of graph to update
     * @param graph new graph to replace old graph with
     * @return Response
     */
    @Override
    public ResponseEntity<Void> updateGraph(Long id, Graph graph) {
        try {
            // make sure graph exists
            checkExistence(id, graphRepository);
            // update graph
            this.save(graph, id);

            return ok();
        } catch (ResponseStatusException e) {
            return notFound();
        }
    }

    /**
     * Save a graph to the database.
     *
     * @param graph graph to save
     * @param id ID of graph, if graph didn't exist before use ID -1
     * @return ID of graph
     */
    private long save(Graph graph, long id) {
        // create graph entity
        GraphEntity entity = new GraphTransformer(labelRepository).toEntity(graph);

        // set ID if this is an update
        if (id != -1) {
            entity.setId(id);
        }

        // save
        this.graphRepository.save(entity);

        return entity.getId();
    }
}
