package de.tudresden.inf.st.mathgrass.api.database.entity;

import javax.persistence.*;
import java.util.List;

/**
 * This class represents a graph, which consists of {@link EdgeEntity}s and {@link VertexEntity}s.
 * Additionally, the graph can be labelled and tagged with multiple {@link LabelEntity}s.
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
    @ElementCollection
    private List<String> labels = null;

    /**
     * Tags of graph.
     */
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<LabelEntity> tags;

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

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<LabelEntity> getTags() {
        return tags;
    }

    public void setTags(List<LabelEntity> tags) {
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
