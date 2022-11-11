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
        edge.setFirstVertex(new VertexTransformer().toDto(entity.getV1()));
        edge.setSecondVertex(new VertexTransformer().toDto(entity.getV2()));
        return edge;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EdgeEntity toEntity(Edge dto) {
        EdgeEntity edgeEntity = new EdgeEntity();

        edgeEntity.setLabel(dto.getLabel());
        edgeEntity.setV1(new VertexTransformer().toEntity(dto.getFirstVertex()));
        edgeEntity.setV2(new VertexTransformer().toEntity(dto.getSecondVertex()));

        return edgeEntity;
    }
}
