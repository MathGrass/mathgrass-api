package de.tudresden.inf.st.mathgrass.api.graph;

import javax.persistence.*;

/**
 * This class represents a vertex in a {@link GraphEntity}.
 */
@Table(name = "vertices")
@Entity
public class VertexEntity {
    /**
     * ID of vertex.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * X coordinate of vertex.
     */
    @Column
    private int x;

    /**
     * Y coordinate of vertex.
     */
    @Column
    private int y;

    /**
     * Label of vertex.
     */
    @Column
    private String label;

    public int getX() {
        return x;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
