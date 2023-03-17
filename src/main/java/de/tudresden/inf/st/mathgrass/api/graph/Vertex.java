package de.tudresden.inf.st.mathgrass.api.graph;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This class represents a vertex in a {@link Graph}.
 */

@Entity
public class Vertex {
    /**
     * ID of vertex.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * X coordinate of vertex.
     */

    private int x;

    /**
     * Y coordinate of vertex.
     */

    private int y;

    /**
     * Label of vertex.
     */

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
