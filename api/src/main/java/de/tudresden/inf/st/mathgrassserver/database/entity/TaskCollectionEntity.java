package de.tudresden.inf.st.mathgrassserver.database.entity;


import javax.persistence.*;
import java.util.List;

@Table(name = "taskcollections")
@Entity
public class TaskCollectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String label;

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

    @ManyToMany
    List<TaskEntity> tasks;


    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }


}
