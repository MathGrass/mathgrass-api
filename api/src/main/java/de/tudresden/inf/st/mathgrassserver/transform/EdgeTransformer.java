package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.EdgeEntity;
import de.tudresden.inf.st.mathgrassserver.model.Edge;

public class EdgeTransformer extends ModelTransformer<Edge, EdgeEntity> {


    @Override
    public Edge toDto(EdgeEntity entity) {
        Edge edge = new Edge();
        //no id is needed here
        edge.setLabel(entity.getLabel());
        edge.setFirstVertex(new VertexTransformer().toDto(entity.getV1()));
        edge.setSecondVertex(new VertexTransformer().toDto(entity.getV2()));
        return edge;
    }

    @Override
    public EdgeEntity toEntity(Edge dto) {
        EdgeEntity edgeEntity = new EdgeEntity();
        //no id is needed here
        edgeEntity.setLabel(dto.getLabel());
        edgeEntity.setV1(new VertexTransformer().toEntity(dto.getFirstVertex()));
        edgeEntity.setV2(new VertexTransformer().toEntity(dto.getSecondVertex()));

        return edgeEntity;
    }
}
