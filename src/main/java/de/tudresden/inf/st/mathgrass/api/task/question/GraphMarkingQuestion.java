package de.tudresden.inf.st.mathgrass.api.task.question;

import de.tudresden.inf.st.mathgrass.api.task.question.answer.AnswerVisitor;

public class GraphMarkingQuestion extends Question {

    @Override
    public boolean acceptQuestionVisitor(QuestionVisitor visitor, AnswerVisitor answerVisitor, Long taskId,
                                         String answer) {
        return visitor.visitGraphMarkingQuestion(this, answerVisitor, taskId, answer);
    }
}
