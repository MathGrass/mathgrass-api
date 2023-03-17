package de.tudresden.inf.st.mathgrass.api.task;

import de.tudresden.inf.st.mathgrass.api.feedback.Feedback;
import de.tudresden.inf.st.mathgrass.api.graph.Graph;
import de.tudresden.inf.st.mathgrass.api.task.hint.Hint;
import de.tudresden.inf.st.mathgrass.api.task.question.Question;

import javax.persistence.*;
import java.util.List;

/**
 * This class represents a task, containing a {@link Graph} and a question regarding the graph.
 */

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
    @OneToOne(cascade = {CascadeType.ALL,CascadeType.MERGE},orphanRemoval = true)
    private Question question;

    /**
     * Label of task.
     */

    private String label;


    /**
     * Graph of task.
     */
    @ManyToOne
    private Graph graph = null;

    /**
     * Feedbacks of task.
     */
    @OneToMany(cascade = {CascadeType.ALL,CascadeType.MERGE},orphanRemoval = true)
    private List<Feedback> feedbacks;

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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
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

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public List<Hint> getHints() {
        return hints;
    }

    public void setHints(List<Hint> hints) {
        this.hints = hints;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

}
