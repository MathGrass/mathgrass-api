package de.tudresden.inf.st.mathgrass.api.transform;

import javax.persistence.*;
import java.util.List;

/**
 * This class represents a task topic which can be used to categorize a {@link Task}.
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
    List<Task> tasks;

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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
