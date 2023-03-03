package de.tudresden.inf.st.mathgrass.api.database.entity;

import javax.persistence.*;

/**
 * This class represents an Edge in a {@link GraphEntity}.
 * An edge is directed, which means it has a source and a target {@link VertexEntity}.
 */
@Table(name = "edges")
@Entity
public class EdgeEntity {
    /**
     * ID of edge.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Label of edge.
     */
    @Column
    private String label;

    /**
     * Source vertex.
     */
    @ManyToOne(cascade = {CascadeType.ALL,CascadeType.MERGE})
    private VertexEntity sourceVertex;

    /**
     * Target vertex.
     */
    @ManyToOne(cascade = {CascadeType.ALL,CascadeType.MERGE})
    private VertexEntity targetVertex;

    public VertexEntity getSourceVertex() {
        return sourceVertex;
    }

    public void setSourceVertex(VertexEntity v1) {
        this.sourceVertex = v1;
    }

    public VertexEntity getTargetVertex() {
        return targetVertex;
    }

    public void setTargetVertex(VertexEntity v2) {
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
