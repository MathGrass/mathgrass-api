package de.tudresden.inf.st.mathgrass.api.websockets;

/**
 * This class contains information about a {@link de.tudresden.inf.st.mathgrass.api.model.TaskResult} and can be used
 * for communication.
 */
public class TaskResultMessage {
    /**
     * ID of task result.
     */
    private long taskId;

    /**
     * Result of task.
     */
    private boolean result;

    /**
     * Constructor.
     *
     * @param taskId ID of task result
     */
    public TaskResultMessage(long taskId, boolean result) {
        this.taskId = taskId;
        this.result = result;
    }

    /**
     * Getter for task result ID.
     *
     * @return ID of task result
     */
    public long getTaskId() {
        return taskId;
    }

    /**
     * Setter for task result ID.
     *
     * @param taskId ID of task result
     */
    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    /**
     * Getter for result.
     *
     * @return result of task
     */
    public boolean getResult() {
        return result;
    }

    /**
     * Setter for result.
     *
     * @param result result of task
     */
    public void setResult(boolean result) {
        this.result = result;
    }
}
