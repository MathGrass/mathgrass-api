package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.VertexEntity;
import de.tudresden.inf.st.mathgrassserver.model.Vertex;

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
