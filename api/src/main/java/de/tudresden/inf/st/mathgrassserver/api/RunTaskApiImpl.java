package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.RunTaskApi;
import de.tudresden.inf.st.mathgrassserver.messageBroker.MessageBrokerConn;
import de.tudresden.inf.st.mathgrassserver.messageBroker.Queue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunTaskApiImpl implements RunTaskApi {

    @Override
    public ResponseEntity runTask(String id, String answer) {
        //TODO: get task from database
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
