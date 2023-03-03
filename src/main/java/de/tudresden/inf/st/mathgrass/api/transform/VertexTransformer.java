package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.graph.VertexEntity;
import de.tudresden.inf.st.mathgrass.api.model.Vertex;

/**
 * This class can convert {@link Vertex} to {@link VertexEntity} and vice versa.
 */
public class VertexTransformer extends ModelTransformer<Vertex, VertexEntity> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Vertex toDto(VertexEntity entity) {
        Vertex v = new Vertex();
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
    public VertexEntity toEntity(Vertex dto) {
        VertexEntity entity = new VertexEntity();
        entity.setLabel(dto.getLabel());
        entity.setX(dto.getX());
        entity.setY(dto.getY());
        return entity;
    }
}
