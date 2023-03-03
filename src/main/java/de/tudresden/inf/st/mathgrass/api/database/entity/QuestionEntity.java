package de.tudresden.inf.st.mathgrass.api.database.entity;


import de.tudresden.inf.st.mathgrass.api.model.Question;

import javax.persistence.*;
import java.util.List;

@Entity
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String question;

    private Boolean isDynamicQuestion;
    private String simpleAnswer;

    private Question.QuestionTypeEnum questionType;

    @ElementCollection
    private List<String> possibleAnswers = null;
    @ManyToOne
    private TaskSolverEntity taskSolver;


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

    public Question.QuestionTypeEnum getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Question.QuestionTypeEnum questionType) {
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

    public TaskSolverEntity getTaskSolver() {
        return taskSolver;
    }

    public void setTaskSolver(TaskSolverEntity taskSolver) {
        this.taskSolver = taskSolver;
    }

}
