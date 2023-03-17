package de.tudresden.inf.st.mathgrass.api.task.question.answer;

import javax.persistence.Entity;

@Entity
public class StaticAnswer extends Answer {
    private String answer;

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }

    @Override
    public boolean acceptAnswerVisitor(AnswerVisitor visitor, Long taskId, String userAnswer) {
        return visitor.visitStaticAnswer(this, taskId, userAnswer);
    }
}
