package de.tudresden.inf.st.mathgrassserver.database.entity;

import javax.persistence.*;
import java.util.List;

/**
 * This class represents a graph, which consists of {@link EdgeEntity}s and {@link VertexEntity}s.
 * Additionally, the graph can be labelled and tagged with multiple {@link TagEntity}s.
 */
@Table(name = "graphs")
@Entity
public class GraphEntity {
    /**
     * ID of graph.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Label of graph.
     */
    @Column
    private String label = null;

    /**
     * Tags of graph.
     */
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<TagEntity> tags;

    /**
     * Edges of graph.
     */
    @OneToMany(cascade = {CascadeType.ALL,CascadeType.MERGE},orphanRemoval = true)
    private List<EdgeEntity> edges;

    /**
     * Vertices of graph.
     */
    @OneToMany(cascade = {CascadeType.ALL,CascadeType.MERGE},orphanRemoval = true)
    private List<VertexEntity> vertices;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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
