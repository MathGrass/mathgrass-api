package de.tudresden.inf.st.mathgrassserver.database.entity;


import de.tudresden.inf.st.mathgrassserver.model.GraphEdges;
import de.tudresden.inf.st.mathgrassserver.model.Tag;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table
@Entity(name = "graph")
public class GraphEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }


    @Column
    private String label = null;

    @Column
    private List<Tag> tags = new ArrayList<Tag>();

    @OneToMany
    private List<GraphEdges> edges = new ArrayList<GraphEdges>();

    @OneToMany
    private List<Long> vertices = new ArrayList<Long>();

    public GraphEntity() {

    }







}
