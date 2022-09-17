package de.tudresden.inf.st.mathgrassserver.api;

import com.google.gson.Gson;
import de.tudresden.inf.st.mathgrassserver.apiModel.EvaluatorApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskSolverEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskRepository;
import de.tudresden.inf.st.mathgrassserver.evaluator.EvaluationRequestMessage;
import de.tudresden.inf.st.mathgrassserver.evaluator.MessageBrokerConn;
import de.tudresden.inf.st.mathgrassserver.evaluator.Queue;
import de.tudresden.inf.st.mathgrassserver.evaluator.TaskManager;
import de.tudresden.inf.st.mathgrassserver.model.TaskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class EvaluatorApiImpl extends AbsApi implements EvaluatorApi {

    @Autowired
    TaskRepository taskRepository;


    @Override
    public ResponseEntity<TaskResult> getTaskResult(Long id, String answer) {
        return null;
    }

    @Override
    public ResponseEntity<Void> runTask(Long taskId, String answer) {
        //TODO: get task from database

        checkExistence(taskId,taskRepository);

        TaskEntity task = taskRepository.getReferenceById(taskId);

        // save result to db
        long requestId = new Random().nextLong();

        boolean isDynamicAnswer = task.getTaskTemplate() != null;

        if (isDynamicAnswer) {
            new TaskManager().runTask(requestId,task.getTaskTemplate().getId(),answer);
            return ok();
        }

        return ok();
    }
}
