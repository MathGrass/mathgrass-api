package de.tudresden.inf.st.mathgrassserver.database.entity;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "tasks")
@Entity
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
    private String question;

    @Column
    private String label;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @ManyToOne
    private TaskTemplateEntity taskTemplate = null;

    @ManyToOne
    private GraphEntity graph = null;

    @Column
    private String answer = null;

    @OneToMany
    private List<FeedbackEntity> feedbacks;

    @OneToMany
    private List<TaskHintEntity> hints;


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
