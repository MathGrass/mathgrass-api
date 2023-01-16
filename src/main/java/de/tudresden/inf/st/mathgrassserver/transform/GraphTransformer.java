package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.EdgeEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.GraphEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.VertexEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.LabelRepository;
import de.tudresden.inf.st.mathgrassserver.model.Graph;

import java.util.HashMap;
import java.util.List;

/**
 * This class can convert {@link Graph} to {@link GraphEntity} and vice versa.
 */
public class GraphTransformer extends ModelTransformer<Graph, GraphEntity> {
    /**
     * Tag repository.
     */
    LabelRepository labelRepository;

    /**
     * Constructor.
     *
     * @param labelRepository tag repository
     */
    public GraphTransformer(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Graph toDto(GraphEntity entity) {
        Graph graph = new Graph();

        graph.setId(entity.getId());
        graph.setEdges(new EdgeTransformer().toDtoList(entity.getEdges()));
        graph.setVertices(new VertexTransformer().toDtoList(entity.getVertices()));
        graph.setLabels(new TagTransformer().toDtoList(entity.getTags()));

        return graph;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphEntity toEntity(Graph dto) {
        //TODO: check consistency (are all vertices of edges in the list of vertices)
        GraphEntity entity = new GraphEntity();

        entity.setId(dto.getId());

        // vertices
        entity.setVertices(new VertexTransformer().toEntityList(dto.getVertices()));
        HashMap<Integer,HashMap<Integer,VertexEntity>> vertexMap = new HashMap<>();
        for (VertexEntity vertex : entity.getVertices()) {
            if (vertexMap.containsKey(vertex.getX())) {
                if (vertexMap.get(vertex.getX()).containsKey(vertex.getY())) {
                    throw new IllegalArgumentException("error creating graph - double vertex");
                }
                vertexMap.get(vertex.getX()).put(vertex.getY(),vertex);
            }
            else {
                HashMap<Integer,VertexEntity> innerMap = new HashMap<>();
                innerMap.put(vertex.getY(),vertex);
                vertexMap.put(vertex.getX(),innerMap);
            }
        }

        // edges
        List<EdgeEntity> edgeList = new EdgeTransformer().toEntityList(dto.getEdges());
        for (EdgeEntity edgeEntity : edgeList) {
            VertexEntity vertex1 = edgeEntity.getSourceVertex();
            edgeEntity.setSourceVertex(vertexMap.get(vertex1.getX()).get(vertex1.getY()));

            VertexEntity vertex2 = edgeEntity.getTargetVertex();
            edgeEntity.setTargetVertex(vertexMap.get(vertex2.getX()).get(vertex2.getY()));
        }
        entity.setEdges(edgeList);

        // tags
        entity.setTags(new TagTransformer().toEntityList(dto.getLabels()));

        return entity;
    }
}
