package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.EdgeEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.GraphEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.VertexEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TagRepository;
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
    TagRepository tagRepository;

    /**
     * Constructor.
     *
     * @param tagRepository tag repository
     */
    public GraphTransformer(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Graph toDto(GraphEntity entity) {
        Graph graph = new Graph();

        graph.setId(entity.getId());
        graph.setLabel(entity.getLabel());
        graph.setEdges(new EdgeTransformer().toDtoList(entity.getEdges()));
        graph.setVertices(new VertexTransformer().toDtoList(entity.getVertices()));
        graph.setTags(new TagTransformer().toDtoList(entity.getTags()));

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
        entity.setLabel(dto.getLabel());

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
            VertexEntity vertex1 = edgeEntity.getV1();
            edgeEntity.setV1(vertexMap.get(vertex1.getX()).get(vertex1.getY()));

            VertexEntity vertex2 = edgeEntity.getV2();
            edgeEntity.setV2(vertexMap.get(vertex2.getX()).get(vertex2.getY()));
        }
        entity.setEdges(edgeList);

        // tags
        entity.setTags(new TagTransformer().toEntityList(dto.getTags()));

        return entity;
    }
}
