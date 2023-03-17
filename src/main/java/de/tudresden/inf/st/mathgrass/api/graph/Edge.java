package de.tudresden.inf.st.mathgrass.api.graph;

import javax.persistence.*;

/**
 * This class represents an Edge in a {@link Graph}.
 * An edge is directed, which means it has a source and a target {@link Vertex}.
 */

@Entity
public class Edge {
    /**
     * ID of edge.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Label of edge.
     */

    private String label;

    /**
     * Source vertex.
     */
    @ManyToOne(cascade = {CascadeType.ALL,CascadeType.MERGE})
    private Vertex sourceVertex;

    /**
     * Target vertex.
     */
    @ManyToOne(cascade = {CascadeType.ALL,CascadeType.MERGE})
    private Vertex targetVertex;

    public Vertex getSourceVertex() {
        return sourceVertex;
    }

    public void setSourceVertex(Vertex v1) {
        this.sourceVertex = v1;
    }

    public Vertex getTargetVertex() {
        return targetVertex;
    }

    public void setTargetVertex(Vertex v2) {
        this.targetVertex = v2;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
