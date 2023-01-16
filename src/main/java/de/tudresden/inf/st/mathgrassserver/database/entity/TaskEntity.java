package de.tudresden.inf.st.mathgrassserver.database.entity;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a task, containing a {@link GraphEntity} and a question regarding the graph.
 */
@Table(name = "tasks")
@Entity
public class TaskEntity {
    /**
     * ID of task.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Question of task.
     */
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @ManyToOne
    private QuestionEntity question;

    /**
     * Label of task.
     */
    @Column
    private String label;

    /**
     * Task template of task, can be used to create dynamic tasks.
     */
    @ManyToOne
    private TaskTemplateEntity taskTemplate = null;

    /**
     * Graph of task.
     */
    @ManyToOne
    private GraphEntity graph = null;

    /**
     * Answer of task.
     */
    @Column
    private String answer = null;

    /**
     * Feedbacks of task.
     */
    @OneToMany(cascade = {CascadeType.ALL,CascadeType.MERGE},orphanRemoval = true)
    private List<FeedbackEntity> feedbacks;

    /**
     * Hints of task.
     */
    @OneToMany(cascade = {CascadeType.ALL,CascadeType.MERGE},orphanRemoval = true,fetch = FetchType.EAGER)
    private List<TaskHintEntity> hints;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public TaskTemplateEntity getTaskTemplate() {
        return taskTemplate;
    }

    public void setTaskTemplate(TaskTemplateEntity taskTemplate) {
        this.taskTemplate = taskTemplate;
    }

    public GraphEntity getGraph() {
        return graph;
    }

    public void setGraph(GraphEntity graph) {
        this.graph = graph;
    }

    public void setFeedbacks(List<FeedbackEntity> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public List<TaskHintEntity> getHints() {
        return hints;
    }

    public void setHints(List<TaskHintEntity> hints) {
        this.hints = hints;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<FeedbackEntity> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(ArrayList<FeedbackEntity> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
