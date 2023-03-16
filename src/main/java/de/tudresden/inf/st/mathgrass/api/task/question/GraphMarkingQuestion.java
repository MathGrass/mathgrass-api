package de.tudresden.inf.st.mathgrass.api.task.question;

import de.tudresden.inf.st.mathgrass.api.task.question.Question;

public class GraphMarkingQuestion extends Question {

    @Override
    public boolean acceptQuestionVisitor(QuestionVisitor visitor, String answer) {
        return visitor.visitGraphMarkingQuestion(this, answer);
    }
}
