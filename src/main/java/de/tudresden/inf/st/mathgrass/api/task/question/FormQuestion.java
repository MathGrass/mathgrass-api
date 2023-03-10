package de.tudresden.inf.st.mathgrass.api.task.question;

import de.tudresden.inf.st.mathgrass.api.task.question.Question;
import de.tudresden.inf.st.mathgrass.api.task.question.QuestionLegacy;
import de.tudresden.inf.st.mathgrass.api.task.question.answer.Answer;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class FormQuestion extends Question {
    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
