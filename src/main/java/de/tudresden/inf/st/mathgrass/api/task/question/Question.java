package de.tudresden.inf.st.mathgrass.api.task.question;


import de.tudresden.inf.st.mathgrass.api.feedback.TaskSolver;

import javax.persistence.*;
import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String question;

    private Boolean isDynamicQuestion;
    private String simpleAnswer;

    private de.tudresden.inf.st.mathgrass.api.model.Question.QuestionTypeEnum questionType;

    @ElementCollection
    private List<String> possibleAnswers = null;
    @ManyToOne
    private TaskSolver taskSolver;


    public List<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(List<String> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getDynamicQuestion() {
        return isDynamicQuestion;
    }

    public void setDynamicQuestion(Boolean dynamicQuestion) {
        isDynamicQuestion = dynamicQuestion;
    }

    public de.tudresden.inf.st.mathgrass.api.model.Question.QuestionTypeEnum getQuestionType() {
        return questionType;
    }

    public void setQuestionType(de.tudresden.inf.st.mathgrass.api.model.Question.QuestionTypeEnum questionType) {
        this.questionType = questionType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSimpleAnswer() {
        return simpleAnswer;
    }

    public void setSimpleAnswer(String simpleAnswer) {
        this.simpleAnswer = simpleAnswer;
    }

    public TaskSolver getTaskSolver() {
        return taskSolver;
    }

    public void setTaskSolver(TaskSolver taskSolver) {
        this.taskSolver = taskSolver;
    }

}
