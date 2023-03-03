package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.graph.VertexEntity;
import de.tudresden.inf.st.mathgrass.api.model.VertexDTO;

/**
 * This class can convert {@link VertexDTO} to {@link VertexEntity} and vice versa.
 */
public class VertexTransformer extends ModelTransformer<VertexDTO, VertexEntity> {
    /**
     * {@inheritDoc}
     */
    @Override
    public VertexDTO toDto(VertexEntity entity) {
        VertexDTO v = new VertexDTO();
        v.setId(entity.getId());
        v.setLabel(entity.getLabel());
        v.setX(entity.getX());
        v.setY(entity.getY());
        return v;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VertexEntity toEntity(VertexDTO dto) {
        VertexEntity entity = new VertexEntity();
        entity.setLabel(dto.getLabel());
        entity.setX(dto.getX());
        entity.setY(dto.getY());
        return entity;
    }
}
