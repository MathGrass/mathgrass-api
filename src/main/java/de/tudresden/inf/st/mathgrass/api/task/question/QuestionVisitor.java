package de.tudresden.inf.st.mathgrass.api.task.question;

import de.tudresden.inf.st.mathgrass.api.task.question.answer.AnswerVisitor;

public class QuestionVisitor {
    public boolean visitGraphMarkingQuestion(GraphMarkingQuestion question, String answer){
        // TODO
        return false;
    }

    public boolean visitFormQuestion(FormQuestion question, String userAnswer){
        AnswerVisitor answerVisitor = new AnswerVisitor();
        boolean result = question.getAnswer().acceptAnswerVisitor(answerVisitor, userAnswer);
        return result;
    }
}
