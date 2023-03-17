package de.tudresden.inf.st.mathgrass.api.graph;

import de.tudresden.inf.st.mathgrass.api.model.GraphDTO;
import de.tudresden.inf.st.mathgrass.api.model.LabelDTO;
import de.tudresden.inf.st.mathgrass.api.transform.ModelTransformer;

import java.util.HashMap;
import java.util.List;

/**
 * This class can convert {@link GraphDTO} to {@link Graph} and vice versa.
 */
public class GraphTransformer extends ModelTransformer<GraphDTO, Graph> {

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphDTO toDto(Graph entity) {
        GraphDTO graph = new GraphDTO();

        graph.setId(entity.getId());
        graph.setEdges(new EdgeTransformer().toDtoList(entity.getEdges()));
        graph.setVertices(new VertexTransformer().toDtoList(entity.getVertices()));

        List<LabelDTO> labelList =
                entity.getLabels().stream().map(l -> new LabelDTO().label(l)).toList();
        graph.setLabels(labelList);

        return graph;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Graph toEntity(GraphDTO dto) {
        //TODO: check consistency (are all vertices of edges in the list of vertices)
        Graph entity = new Graph();

        entity.setId(dto.getId());

        // vertices
        entity.setVertices(new VertexTransformer().toEntityList(dto.getVertices()));
        HashMap<Integer,HashMap<Integer, Vertex>> vertexMap = new HashMap<>();
        for (Vertex vertex : entity.getVertices()) {
            if (vertexMap.containsKey(vertex.getX())) {
                if (vertexMap.get(vertex.getX()).containsKey(vertex.getY())) {
                    throw new IllegalArgumentException("error creating graph - double vertex");
                }
                vertexMap.get(vertex.getX()).put(vertex.getY(),vertex);
            }
            else {
                HashMap<Integer, Vertex> innerMap = new HashMap<>();
                innerMap.put(vertex.getY(),vertex);
                vertexMap.put(vertex.getX(),innerMap);
            }
        }

        // edges
        List<Edge> edgeList = new EdgeTransformer().toEntityList(dto.getEdges());
        for (Edge edgeEntity : edgeList) {
            Vertex vertex1 = edgeEntity.getSourceVertex();
            edgeEntity.setSourceVertex(vertexMap.get(vertex1.getX()).get(vertex1.getY()));

            Vertex vertex2 = edgeEntity.getTargetVertex();
            edgeEntity.setTargetVertex(vertexMap.get(vertex2.getX()).get(vertex2.getY()));
        }
        entity.setEdges(edgeList);

        return entity;
    }
}
