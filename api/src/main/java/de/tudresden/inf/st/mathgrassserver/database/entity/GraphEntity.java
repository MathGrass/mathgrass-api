package de.tudresden.inf.st.mathgrassserver.database.entity;


import de.tudresden.inf.st.mathgrassserver.model.GraphEdges;
import de.tudresden.inf.st.mathgrassserver.model.Tag;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Table
@Entity(name = "graph")
public class GraphEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    @Column
    private String label = null;

    @ManyToMany
    private List<TagEntity> tags;

    @ElementCollection
    private Map<String,String> edges;

    @ElementCollection
    private List<String> vertices;

    public GraphEntity() {

    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TagEntity> getTags() {
        return tags;
    }

    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
    }

    public Map<String, String> getEdges() {
        return edges;
    }

    public void setEdges(Map<String, String> edges) {
        this.edges = edges;
    }

    public List<String> getVertices() {
        return vertices;
    }

    public void setVertices(List<String> vertices) {
        this.vertices = vertices;
    }
}
