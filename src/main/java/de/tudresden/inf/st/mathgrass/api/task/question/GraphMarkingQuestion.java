package de.tudresden.inf.st.mathgrass.api.task.question;

import de.tudresden.inf.st.mathgrass.api.task.question.Question;

public class GraphMarkingQuestion extends Question {
    @Override
    public String acceptVisitor(QuestionVisitor visitor) {
        return visitor.visitGraphMarkingQuestion(this);
    }
}
