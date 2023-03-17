package de.tudresden.inf.st.mathgrass.api.graph;

import de.tudresden.inf.st.mathgrass.api.label.Label;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a graph, which consists of {@link Edge}s and {@link Vertex}s.
 * Additionally, the graph can be labelled and tagged with multiple {@link Label}s.
 */
@Table(name = "graphs")
@Entity
public class Graph {
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
    private List<String> labels = new ArrayList<>();

    /**
     * Tags of graph.
     */
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<Label> tags;

    /**
     * Edges of graph.
     */
    @OneToMany(cascade = {CascadeType.ALL,CascadeType.MERGE},orphanRemoval = true)
    private List<Edge> edges;

    /**
     * Vertices of graph.
     */
    @OneToMany(cascade = {CascadeType.ALL,CascadeType.MERGE},orphanRemoval = true)
    private List<Vertex> vertices;

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

    public List<Label> getTags() {
        return tags;
    }

    public void setTags(List<Label> tags) {
        this.tags = tags;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }
}
