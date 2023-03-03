package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.graph.EdgeEntity;
import de.tudresden.inf.st.mathgrass.api.model.EdgeDTO;
import de.tudresden.inf.st.mathgrass.api.model.VertexDTO;


/**
 * This class can convert {@link EdgeDTO} to {@link EdgeEntity} and vice versa.
 */
public class EdgeTransformer extends ModelTransformer<EdgeDTO, EdgeEntity> {
    /**
     * {@inheritDoc}
     */
    @Override
    public EdgeDTO toDto(EdgeEntity entity) {
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
    public EdgeEntity toEntity(EdgeDTO dto) {
        EdgeEntity edgeEntity = new EdgeEntity();

        edgeEntity.setLabel(dto.getLabel());
        edgeEntity.setSourceVertex(new VertexTransformer().toEntity(dto.getFirstVertex()));
        edgeEntity.setTargetVertex(new VertexTransformer().toEntity(dto.getSecondVertex()));

        return edgeEntity;
    }
}
