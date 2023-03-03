package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.feedback.FeedbackEntity;
import de.tudresden.inf.st.mathgrass.api.task.hint.Hint;
import de.tudresden.inf.st.mathgrass.api.graph.Graph;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a task, containing a {@link Graph} and a question regarding the graph.
 */
@Table(name = "tasks")
@Entity
public class Task {
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
     * Graph of task.
     */
    @ManyToOne
    private Graph graph = null;

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
    private List<Hint> hints;

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

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setFeedbacks(List<FeedbackEntity> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public List<Hint> getHints() {
        return hints;
    }

    public void setHints(List<Hint> hints) {
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
