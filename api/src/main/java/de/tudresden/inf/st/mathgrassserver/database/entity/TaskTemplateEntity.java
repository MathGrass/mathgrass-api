package de.tudresden.inf.st.mathgrassserver.database.entity;


import de.tudresden.inf.st.mathgrassserver.model.Tag;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table
@Entity(name = "tasktemplate")
public class TaskTemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    @Column
    private String label = null;

    @Column
    private String question = null;

    @Column
    private Long taskSolver = null;

    @OneToMany
    private List<String> hints = new ArrayList<String>();

    @ManyToMany
    private List<Tag> tags = new ArrayList<Tag>();

    public TaskTemplateEntity() {

    }







}
