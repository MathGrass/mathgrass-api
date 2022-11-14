package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.EdgeEntity;
import de.tudresden.inf.st.mathgrassserver.model.Edge;

/**
 * This class can convert {@link Edge} to {@link EdgeEntity} and vice versa.
 */
public class EdgeTransformer extends ModelTransformer<Edge, EdgeEntity> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Edge toDto(EdgeEntity entity) {
        Edge edge = new Edge();

        edge.setLabel(entity.getLabel());
        edge.setFirstVertex(new VertexTransformer().toDto(entity.getSourceVertex()));
        edge.setSecondVertex(new VertexTransformer().toDto(entity.getTargetVertex()));
        return edge;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EdgeEntity toEntity(Edge dto) {
        EdgeEntity edgeEntity = new EdgeEntity();

        edgeEntity.setLabel(dto.getLabel());
        edgeEntity.setSourceVertex(new VertexTransformer().toEntity(dto.getFirstVertex()));
        edgeEntity.setTargetVertex(new VertexTransformer().toEntity(dto.getSecondVertex()));

        return edgeEntity;
    }
}
