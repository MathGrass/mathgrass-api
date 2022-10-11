package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.EdgeEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.GraphEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TagEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.VertexEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TagRepository;
import de.tudresden.inf.st.mathgrassserver.model.Edge;
import de.tudresden.inf.st.mathgrassserver.model.Graph;
import de.tudresden.inf.st.mathgrassserver.model.Vertex;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphTransformer extends ModelTransformer<Graph, GraphEntity> {

    
    TagRepository tagRepository;


    public GraphTransformer(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Graph toDto(GraphEntity entity) {

        Graph graph = new Graph();
        graph.setId(entity.getId());

        //Label
        graph.setLabel(entity.getLabel());

        //Edges
        graph.setEdges(new EdgeTransformer().toDtoList(entity.getEdges()));


        //Vertices
        graph.setVertices(new VertexTransformer().toDtoList(entity.getVertices()));


        //Tags
        graph.setTags(new TagTransformer().toDtoList(entity.getTags()));


        return graph;
    }

    @Override
    public GraphEntity toEntity(Graph dto) {
        //TODO: check consistency (are all vertices of edges in the list of vertices)
        GraphEntity entity = new GraphEntity();
        entity.setId(dto.getId());

        //Label
        entity.setLabel(dto.getLabel());

        // Vertices
        entity.setVertices(new VertexTransformer().toEntityList(dto.getVertices()));

        HashMap<Integer,HashMap<Integer,VertexEntity>> map = new HashMap<>();
        for (VertexEntity vertex : entity.getVertices()) {
            if (map.containsKey(vertex.getX())) {
                if (map.get(vertex.getX()).containsKey(vertex.getY())) {
                    throw new IllegalArgumentException("error creating graph - double vertex");
                }
                map.get(vertex.getX()).put(vertex.getY(),vertex);
            }
            else {
                HashMap<Integer,VertexEntity> innerMap = new HashMap<>();
                innerMap.put(vertex.getY(),vertex);
                map.put(vertex.getX(),innerMap);
            }

        }

        // Edges
        List<EdgeEntity> edgeList = new EdgeTransformer().toEntityList(dto.getEdges());
        for (EdgeEntity edgeEntity : edgeList) {
            VertexEntity vertex1 = edgeEntity.getV1();
            edgeEntity.setV1(map.get(vertex1.getX()).get(vertex1.getY()));

            VertexEntity vertex2 = edgeEntity.getV2();
            edgeEntity.setV2(map.get(vertex2.getX()).get(vertex2.getY()));
        }
        entity.setEdges(edgeList);

        //Tags
        entity.setTags(new TagTransformer().toEntityList(dto.getTags()));

        return entity;
    }
}
