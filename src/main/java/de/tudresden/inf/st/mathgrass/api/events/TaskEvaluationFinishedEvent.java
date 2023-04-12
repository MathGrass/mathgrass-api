package de.tudresden.inf.st.mathgrass.api.events;

/**
 * Event class for task evaluation finished events.
 *
 * @param getTaskResultId ID of the task result.
 */
public record TaskEvaluationFinishedEvent(Long getTaskResultId) {
    /**
     * Get id of task result.
     *
     * @return task result id
     */
    public Long getTaskResultId() {
        return getTaskResultId;
    }
}
