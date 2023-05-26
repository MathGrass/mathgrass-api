package de.tudresden.inf.st.mathgrass.api.admin.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "question_answer")
public class QuestionAnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    private String answerType;


    @ElementCollection
    private List<String> multipleChoice;

    private String multipleChoiceAnswer;
    private String writtenAnswer;
    private String sageMathScript;

    @ElementCollection
    private List<String> graphElemetId;

    @ElementCollection
    private List<String> graphLinkId;

    public QuestionAnswerEntity() {
    }

    @Override
    public String toString() {
        return "QuestionAnswerEntity{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answerType='" + answerType + '\'' +
                ", multipleChoice=" + multipleChoice +
                ", multipleChoiceAnswer='" + multipleChoiceAnswer + '\'' +
                ", writtenAnswer='" + writtenAnswer + '\'' +
                ", sageMathScript='" + sageMathScript + '\'' +
                ", graphElemetId=" + graphElemetId +
                ", graphLinkId=" + graphLinkId +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public List<String> getMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(List<String> multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public String getMultipleChoiceAnswer() {
        return multipleChoiceAnswer;
    }

    public void setMultipleChoiceAnswer(String multipleChoiceAnswer) {
        this.multipleChoiceAnswer = multipleChoiceAnswer;
    }

    public String getWrittenAnswer() {
        return writtenAnswer;
    }

    public void setWrittenAnswer(String writtenAnswer) {
        this.writtenAnswer = writtenAnswer;
    }

    public String getSageMathScript() {
        return sageMathScript;
    }

    public void setSageMathScript(String sageMathScript) {
        this.sageMathScript = sageMathScript;
    }

    public List<String> getGraphElemetId() {
        return graphElemetId;
    }

    public void setGraphElemetId(List<String> graphElemetId) {
        this.graphElemetId = graphElemetId;
    }

    public List<String> getGraphLinkId() {
        return graphLinkId;
    }

    public void setGraphLinkId(List<String> graphLinkId) {
        this.graphLinkId = graphLinkId;
    }

    public QuestionAnswerEntity(Long id, String question, String answerType, List<String> multipleChoice, String multipleChoiceAnswer, String writtenAnswer, String sageMathScript, List<String> graphElemetId, List<String> graphLinkId) {
        this.id = id;
        this.question = question;
        this.answerType = answerType;
        this.multipleChoice = multipleChoice;
        this.multipleChoiceAnswer = multipleChoiceAnswer;
        this.writtenAnswer = writtenAnswer;
        this.sageMathScript = sageMathScript;
        this.graphElemetId = graphElemetId;
        this.graphLinkId = graphLinkId;
    }
}

