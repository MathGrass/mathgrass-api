package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.EvaluatorApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskRepository;
import de.tudresden.inf.st.mathgrassserver.messageBroker.MessageBrokerConn;
import de.tudresden.inf.st.mathgrassserver.messageBroker.Queue;
import de.tudresden.inf.st.mathgrassserver.model.TaskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EvaluatorApiImpl extends AbsApi implements EvaluatorApi {

    @Autowired
    TaskRepository taskRepository;

    @Override
    public ResponseEntity<TaskResult> getTaskResult(Long id, String answer) {
        return null;
    }

    @Override
    public ResponseEntity<Void> runTask(Long id, String answer) {
        //TODO: get task from database

        checkExistence(id,taskRepository);

        TaskEntity task = taskRepository.getReferenceById(id);


        //TODO: extract graph, executionType, (execution ref or fixed answer)
        boolean isDynamicAnswer = true;
        //TODO: if execution -> queue task in evaluator
        if (isDynamicAnswer) {
            MessageBrokerConn.getInstance().send(Queue.TASK_REQUEST,"test123");
            return new ResponseEntity(HttpStatus.OK);
        }

        return null;
    }
}
