package de.tudresden.inf.st.mathgrassserver.database.entity;


import de.tudresden.inf.st.mathgrassserver.model.Tag;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "taskhints")
@Entity
public class TaskHintEntity {

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
    private String content = null;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
