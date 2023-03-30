package de.tudresden.inf.st.mathgrass.api.task.execution;

import org.springframework.context.ApplicationEvent;

/**
 * Event class for task evaluation finished events.
 */
public class TaskEvaluationFinishedEvent extends ApplicationEvent {
    /**
     * ID of the task result.
     */
    private final Long taskResultId;

    /**
     * Constructor.
     *
     * @param source source of event
     * @param taskResultId id of task result
     */
    public TaskEvaluationFinishedEvent(Object source, Long taskResultId) {
        super(source);
        this.taskResultId = taskResultId;
    }

    /**
     * Get id of task result.
     *
     * @return task result id
     */
    public Long getTaskResultId() {
        return taskResultId;
    }
}
