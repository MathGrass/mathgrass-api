package de.tudresden.inf.st.mathgrass.api.task.question;

import de.tudresden.inf.st.mathgrass.api.label.Label;
import de.tudresden.inf.st.mathgrass.api.task.question.answer.AnswerVisitor;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

@Entity
public abstract class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @OneToMany
    private List<Label> labels;

    private String questionText;

    public Long getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public abstract boolean acceptQuestionVisitor(QuestionVisitor visitor, AnswerVisitor answerVisitor,
                                                  String answer) throws IOException, InterruptedException;
}
