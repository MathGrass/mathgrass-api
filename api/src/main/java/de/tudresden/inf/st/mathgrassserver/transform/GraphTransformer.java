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

public class GraphTransformer extends ModelTransformer<Graph, GraphEntity> {

    @Autowired
    TagRepository tagRepository;

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

        //Edges
        entity.setEdges(new EdgeTransformer().toEntityList(dto.getEdges()));

        //Vertices
        entity.setVertices(new VertexTransformer().toEntityList(dto.getVertices()));

        //Tags
        entity.setTags(new TagTransformer().toEntityList(dto.getTags()));

        return entity;
    }
}
