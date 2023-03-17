package de.tudresden.inf.st.mathgrass.api.task.question;

import de.tudresden.inf.st.mathgrass.api.task.question.answer.Answer;
import de.tudresden.inf.st.mathgrass.api.task.question.answer.AnswerVisitor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.IOException;

@Entity
public class FormQuestion extends Question {
    @ManyToOne(cascade = CascadeType.ALL)
    private Answer answer;

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    @Override
    public boolean acceptQuestionVisitor(QuestionVisitor visitor, AnswerVisitor answerVisitor, Long taskId,
                                         String answer) throws IOException, InterruptedException {
        return visitor.visitFormQuestion(this, answerVisitor, taskId, answer);

    }
}
