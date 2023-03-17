package de.tudresden.inf.st.mathgrass.api.graph;

import de.tudresden.inf.st.mathgrass.api.apiModel.GraphApi;
import de.tudresden.inf.st.mathgrass.api.common.AbstractApiElement;
import de.tudresden.inf.st.mathgrass.api.model.GraphDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * This class can create/update/load/save {@link GraphDTO}s.
 */
@RestController
public class GraphApiImpl extends AbstractApiElement implements GraphApi {
    /**
     * GraphDTO repository.
     */
    final GraphRepository graphRepository;

    /**
     * Constructor.
     *
     * @param graphRepository graph repository
     */
    public GraphApiImpl(GraphRepository graphRepository) {
        this.graphRepository = graphRepository;
    }

    /**
     * Create a graph and return its ID.
     *
     * @param body graph
     * @return Response with GraphDTO ID
     */
    @Override
    public ResponseEntity<Long> createGraph(GraphDTO body) {
        return ok(this.save(body,-1));
    }

    /**
     * Get a graph with given ID.
     *
     * @param graphId ID of graph
     * @return Response with graph
     */
    @Override
    public ResponseEntity<GraphDTO> getGraphById(Long graphId) {
        Optional<Graph> optGraphEntity = graphRepository.findById(graphId);

        if (optGraphEntity.isPresent()) {
            GraphDTO graph = new GraphTransformer().toDto(optGraphEntity.get());
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
    public ResponseEntity<Void> updateGraph(Long id, GraphDTO graph) {
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
    private long save(GraphDTO graph, long id) {
        // create graph entity
        Graph entity = new GraphTransformer().toEntity(graph);

        // set ID if this is an update
        if (id != -1) {
            entity.setId(id);
        }

        // save
        this.graphRepository.save(entity);

        return entity.getId();
    }
}
