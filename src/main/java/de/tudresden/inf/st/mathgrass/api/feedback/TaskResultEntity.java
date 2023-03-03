package de.tudresden.inf.st.mathgrass.api.feedback;

import de.tudresden.inf.st.mathgrass.api.task.Task;

import javax.persistence.*;

/**
 * This class represents the result of a {@link Task}, containing the users given answer, as well as
 * evaluation and submission dates.
 */
@Table(name = "taskresults")
@Entity
public class TaskResultEntity {
    /**
     * ID of task result.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Task of result.
     */
    @ManyToOne
    private Task task;

    /**
     * Given answer.
     */
    @Column
    private String answer;

    /**
     * Date of evaluation.
     */
    @Column
    private String evaluationDate;

    /**
     * Date of submission.
     */
    @Column
    private String submissionDate;

    /**
     * Correctness of given answer.
     */
    @Column
    private boolean answerTrue;

    public boolean isAnswerTrue() {
        return answerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        this.answerTrue = answerTrue;
    }

    public String getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(String evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
