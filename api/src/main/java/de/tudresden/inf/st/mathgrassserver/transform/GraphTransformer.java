package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.GraphEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TagEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TagRepository;
import de.tudresden.inf.st.mathgrassserver.model.Graph;
import de.tudresden.inf.st.mathgrassserver.model.GraphEdges;
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
        ArrayList<GraphEdges> edges = new ArrayList<>();
        for (String vertex : entity.getEdges().keySet()) {
            GraphEdges edge = new GraphEdges();
            edge.setFirstVertex(vertex);
            edge.setSecondVertex(entity.getEdges().get(vertex));
            edges.add(edge);
        }
        graph.setEdges(edges);


        //Vertices
        graph.setVertices(entity.getVertices());


        //Tags
        ArrayList<Long> tags = new ArrayList<>();
        for (TagEntity tag : entity.getTags()) {
            tags.add(tag.getId());
        }
        graph.setTags(tags);


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
        HashMap<String,String> edges = new HashMap<>();
        for (GraphEdges edge : dto.getEdges()) {
            edges.put(edge.getFirstVertex(),edge.getSecondVertex());
        }
        entity.setEdges(edges);

        //Vertices
        entity.setVertices(dto.getVertices());

        //Tags
        ArrayList<TagEntity> tags = new ArrayList<>();
        for (long tagId : dto.getTags()) {
            tags.add(tagRepository.getReferenceById(tagId));
        }
        entity.setTags(tags);

        return entity;
    }
}
