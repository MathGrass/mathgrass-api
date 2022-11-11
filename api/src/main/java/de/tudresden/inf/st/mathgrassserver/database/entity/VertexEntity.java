package de.tudresden.inf.st.mathgrassserver.database.entity;

import javax.persistence.*;

@Table(name = "vertices")
@Entity
public class VertexEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private int x;

    @Column
    private int y;

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
