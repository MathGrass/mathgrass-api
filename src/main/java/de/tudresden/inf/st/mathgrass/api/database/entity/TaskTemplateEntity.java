package de.tudresden.inf.st.mathgrass.api.database.entity;

import javax.persistence.*;
import java.util.List;

/**
 * This class represents a task template, which can be used to create dynamic {@link TaskEntity}s.
 */
@Table(name = "tasktemplates")
@Entity
public class TaskTemplateEntity {
    /**
     * ID of task template.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Label of task template.
     */
    @Column
    private String label = null;

    /**
     * Question of task template.
     */
    @Column
    private String question = null;

    /**
     * Task solver of task template.
     */
    @OneToOne
    private TaskSolverEntity taskSolver = null;

    //TODO: remove cascade?
    /**
     * Tags of task template.
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private List<LabelEntity> tags;

    /**
     * Hints of task template.
     */
    @OneToMany(cascade = {CascadeType.ALL,CascadeType.MERGE},orphanRemoval = true)
    private List<TaskHintEntity> hints;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<TaskHintEntity> getHints() {
        return hints;
    }

    public void setHints(List<TaskHintEntity> hints) {
        this.hints = hints;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public TaskSolverEntity getTaskSolver() {
        return taskSolver;
    }

    public void setTaskSolver(TaskSolverEntity taskSolver) {
        this.taskSolver = taskSolver;
    }

    public List<LabelEntity> getTags() {
        return tags;
    }

    public void setTags(List<LabelEntity> tags) {
        this.tags = tags;
    }
}
