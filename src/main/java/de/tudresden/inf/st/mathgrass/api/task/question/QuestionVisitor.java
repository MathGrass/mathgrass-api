package de.tudresden.inf.st.mathgrass.api.task.question;

import de.tudresden.inf.st.mathgrass.api.task.question.answer.AnswerVisitor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class QuestionVisitor {
    public boolean visitGraphMarkingQuestion(GraphMarkingQuestion question, AnswerVisitor answerVisitor, Long taskId,
                                             String answer) {
        // TODO
        return false;
    }

    public boolean visitFormQuestion(FormQuestion question, AnswerVisitor answerVisitor, Long taskId,
                                     String userAnswer) throws IOException, InterruptedException {
        return question.getAnswer().acceptAnswerVisitor(answerVisitor, taskId, userAnswer);
    }
}
