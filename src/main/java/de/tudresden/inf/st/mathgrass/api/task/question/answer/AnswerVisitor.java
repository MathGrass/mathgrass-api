package de.tudresden.inf.st.mathgrass.api.task.question.answer;

import de.tudresden.inf.st.mathgrass.api.evaluator.TaskManager;

public class AnswerVisitor {
    public boolean visitStaticAnswer(StaticAnswer answer, String userAnswer){
        return answer.getAnswer().equals(userAnswer);
    }

    public boolean visitDynamicAnswer(DynamicAnswer answer, String userAnswer){
        return TaskManager.runTask(11, userAnswer, answer.getExecutor());
    }
}
