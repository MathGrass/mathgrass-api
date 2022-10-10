package de.tudresden.inf.st.mathgrassserver.evaluator;

import com.google.gson.Gson;

public class TaskManager {
    public void runTask(long requestId, long taskId, String answer) {
        // queue task in evaluator
        EvaluationRequestMessage msg = new EvaluationRequestMessage(requestId,taskId,answer);
        String msgString = new Gson().toJson(msg);
        MessageBrokerConn.getInstance().send(Queue.TASK_REQUEST,msgString);
    }
}
