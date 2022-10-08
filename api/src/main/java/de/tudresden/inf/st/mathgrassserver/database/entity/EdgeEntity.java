package de.tudresden.inf.st.mathgrassserver.database.entity;

import javax.persistence.*;

@Table
@Entity(name = "vertex")
public class EdgeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String label;

    @ManyToOne
    private VertexEntity v1;

    @ManyToOne
    private VertexEntity v2;

    public VertexEntity getV1() {
        return v1;
    }

    public void setV1(VertexEntity v1) {
        this.v1 = v1;
    }

    public VertexEntity getV2() {
        return v2;
    }

    public void setV2(VertexEntity v2) {
        this.v2 = v2;
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
