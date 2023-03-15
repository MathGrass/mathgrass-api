package de.tudresden.inf.st.mathgrass.api.task.question;

import de.tudresden.inf.st.mathgrass.api.task.question.answer.AnswerVisitor;

public class QuestionVisitor {
    public String visitGraphMarkingQuestion(GraphMarkingQuestion question){
        return "graph marking visited";
    }

    public String visitFormQuestion(FormQuestion question){
        AnswerVisitor answerVisitor = new AnswerVisitor();
        String result = question.getAnswer().acceptVisitor(answerVisitor);
        return result;
    }
}
