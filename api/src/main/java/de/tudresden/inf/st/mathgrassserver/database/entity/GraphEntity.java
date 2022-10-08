package de.tudresden.inf.st.mathgrassserver.database.entity;

import de.tudresden.inf.st.mathgrassserver.model.Edge;

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

    @OneToMany
    private List<EdgeEntity> edges;

    @OneToMany
    private List<VertexEntity> vertices;

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

    public List<EdgeEntity> getEdges() {
        return edges;
    }

    public void setEdges(List<EdgeEntity> edges) {
        this.edges = edges;
    }

    public List<VertexEntity> getVertices() {
        return vertices;
    }

    public void setVertices(List<VertexEntity> vertices) {
        this.vertices = vertices;
    }
}
