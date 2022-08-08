package de.tudresden.inf.st.mathgrassserver.database.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table
@Entity(name = "task")
public class TaskEntity {
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }


    @Column
    private Long taskTemplate = null;

    @Column
    private Long graph = null;

    @OneToMany
    private List<String> hints = new ArrayList<String>();

    @Column
    private String answer = null;

    @OneToMany
    private List<Long> feedback = new ArrayList<Long>();
}
