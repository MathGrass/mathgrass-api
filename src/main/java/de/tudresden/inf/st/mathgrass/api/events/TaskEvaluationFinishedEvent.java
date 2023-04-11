package de.tudresden.inf.st.mathgrass.api.events;

/**
 * Event class for task evaluation finished events.
 *
 * @param taskResultId ID of the task result.
 */
public record TaskEvaluationFinishedEvent(Long taskResultId) {
}
