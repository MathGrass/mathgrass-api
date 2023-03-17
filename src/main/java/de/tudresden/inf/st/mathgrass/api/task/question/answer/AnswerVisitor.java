package de.tudresden.inf.st.mathgrass.api.task.question.answer;

import de.tudresden.inf.st.mathgrass.api.evaluator.TaskManager;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AnswerVisitor {

    private final TaskManager taskManager;

    public AnswerVisitor(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public boolean visitStaticAnswer(StaticAnswer answer, Long taskId, String userAnswer) {
        return answer.getAnswer().equals(userAnswer);
    }

    public boolean visitDynamicAnswer(DynamicAnswer answer, Long taskId, String userAnswer) throws IOException {
        return taskManager.runTaskSynchronously(taskId, userAnswer, answer.getExecutor());
    }
}
