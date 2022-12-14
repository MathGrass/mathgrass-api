package de.tudresden.inf.st.mathgrassserver.evaluator;

import java.util.Objects;

/**
 * This class represents an evaluation request message which can be sent to an evaluator.
 *
 * @param requestId   ID of request.
 * @param taskId      ID of task.
 * @param inputAnswer Answer given via input.
 */
public record EvaluationRequestMessage(Long requestId, Long taskId, String inputAnswer) {
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
