package de.tudresden.inf.st.mathgrass.api.feedback.evaluator;

import com.google.gson.Gson;

/**
 * This class creates {@link EvaluationRequestMessage} instances and uses the {@link MessageBrokerConn} to send
 * evaluation requests to evaluators.
 */
public class TaskManager {

    /**
     * Create an evaluation request message and send to evaluator.
     *
     * @param requestId ID of request
     * @param taskId ID of task
     * @param answer answer given via input
     */
    public void runTask(long requestId, long taskId, String answer) {
        // create message and convert to JSON
        EvaluationRequestMessage msg = new EvaluationRequestMessage(requestId, taskId, answer);
        String msgString = new Gson().toJson(msg);

        // send message
        MessageBrokerConn.getInstance().send(Queue.TASK_REQUEST,msgString);
    }
}
