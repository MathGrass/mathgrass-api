package de.tudresden.inf.st.mathgrassserver.database.entity;


import de.tudresden.inf.st.mathgrassserver.model.Tag;
import de.tudresden.inf.st.mathgrassserver.model.TaskSolver;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "tasktemplates")
@Entity
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

    @OneToOne
    private TaskSolverEntity taskSolver = null;

    @ManyToMany
    private List<TagEntity> tags;

    @OneToMany
    private List< TaskHintEntity> hints;


    public List<TaskHintEntity> getHints() {
        return hints;
    }

    public void setHints(List<TaskHintEntity> hints) {
        this.hints = hints;
    }

    public TaskTemplateEntity() {

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

    public List<TagEntity> getTags() {
        return tags;
    }

    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
    }
}
