package de.tudresden.inf.st.mathgrass.api.task.collection;

import de.tudresden.inf.st.mathgrass.api.task.Task;

import javax.persistence.*;
import java.util.List;

/**
 * This class represents a collection of {@link Task}s.
 */

@Entity
public class TaskCollection {
    /**
     * ID of task collection.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Label of task collection.
     */

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

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<Task> tasks;

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
