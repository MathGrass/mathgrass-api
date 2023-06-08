package de.tudresden.inf.st.mathgrass.api.graph;

import de.tudresden.inf.st.mathgrass.api.model.EdgeDTO;
import de.tudresden.inf.st.mathgrass.api.transform.ModelTransformer;


/**
 * This class can convert {@link EdgeDTO} to {@link Edge} and vice versa.
 */
public class EdgeTransformer extends ModelTransformer<EdgeDTO, Edge> {
    /**
     * {@inheritDoc}
     */
    @Override
    public EdgeDTO toDto(Edge entity) {
        EdgeDTO edgeDTO = new EdgeDTO();

        edgeDTO.setLabel(entity.getLabel());
        edgeDTO.setId(entity.getId());
        edgeDTO.setFirstVertex(new VertexTransformer().toDto(entity.getSourceVertex()));
        edgeDTO.setSecondVertex(new VertexTransformer().toDto(entity.getTargetVertex()));
        //Changes for Admin Interface - Starts
        edgeDTO.setEdgeId(entity.getEdgeId());
        //Changes for Admin Interface - Ends
        return edgeDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Edge toEntity(EdgeDTO dto) {
        Edge edgeEntity = new Edge();

        edgeEntity.setLabel(dto.getLabel());
        edgeEntity.setSourceVertex(new VertexTransformer().toEntity(dto.getFirstVertex()));
        edgeEntity.setTargetVertex(new VertexTransformer().toEntity(dto.getSecondVertex()));
        //Changes for Admin Interface - Starts
        edgeEntity.setEdgeId(dto.getEdgeId());
        //Changes for Admin Interface - Ends

        return edgeEntity;
    }
}
