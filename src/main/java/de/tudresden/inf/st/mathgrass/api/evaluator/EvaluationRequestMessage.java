package de.tudresden.inf.st.mathgrass.api.evaluator;

import de.tudresden.inf.st.mathgrass.api.evaluator.executor.Executor;

import java.util.Objects;

/**
 * This class represents an evaluation request message which can be sent to an evaluator.
 *
 * @param requestId   ID of request.
 * @param taskId      ID of task.
 * @param inputAnswer Answer given via input.
 */
public record EvaluationRequestMessage(Long requestId, Long taskId, String inputAnswer, Executor executor) {
    /**
     * Constructor.
     *
     * @param requestId   ID of request
     * @param taskId      ID of task
     * @param inputAnswer answer given via input
     */
    public EvaluationRequestMessage {
        Objects.requireNonNull(requestId);
        Objects.requireNonNull(taskId);
        Objects.requireNonNull(inputAnswer);
    }
}
