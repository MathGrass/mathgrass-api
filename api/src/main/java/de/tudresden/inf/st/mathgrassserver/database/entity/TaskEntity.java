package de.tudresden.inf.st.mathgrassserver.database.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table
@Entity(name = "task")
public class TaskEntity {

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
    private Long taskTemplate = null;

    @Column
    private Long graph = null;

    @ElementCollection
    private List<String> hints = new ArrayList<String>();

    @Column
    private String answer = null;

    @ManyToMany
    private ArrayList<FeedbackEntity> feedbacks = new ArrayList<>();


    public Long getTaskTemplate() {
        return taskTemplate;
    }

    public void setTaskTemplate(Long taskTemplate) {
        this.taskTemplate = taskTemplate;
    }

    public Long getGraph() {
        return graph;
    }

    public void setGraph(Long graph) {
        this.graph = graph;
    }

    public List<String> getHints() {
        return hints;
    }

    public void setHints(List<String> hints) {
        this.hints = hints;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ArrayList<FeedbackEntity> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(ArrayList<FeedbackEntity> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
