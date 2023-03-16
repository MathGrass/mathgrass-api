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
        EdgeDTO EdgeDTO = new EdgeDTO();

        EdgeDTO.setLabel(entity.getLabel());
        EdgeDTO.setFirstVertex(new VertexTransformer().toDto(entity.getSourceVertex()));
        EdgeDTO.setSecondVertex(new VertexTransformer().toDto(entity.getTargetVertex()));
        return EdgeDTO;
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

        return edgeEntity;
    }
}
