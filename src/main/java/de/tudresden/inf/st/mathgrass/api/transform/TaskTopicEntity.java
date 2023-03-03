package de.tudresden.inf.st.mathgrass.api.transform;

import javax.persistence.*;
import java.util.List;

/**
 * This class represents a task topic which can be used to categorize a {@link TaskEntity}.
 */
@Table(name = "tasktopics")
@Entity
public class TaskTopicEntity {
    /**
     * ID of task topic.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Label of task topic.
     */
    @Column
    private String label;

    /**
     * Task entities in the task topic.
     */
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<TaskEntity> tasks;

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

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }
}
