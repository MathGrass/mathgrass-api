package de.tudresden.inf.st.mathgrassserver.websockets;

/**
 * This class contains information about a {@link de.tudresden.inf.st.mathgrassserver.model.TaskResult} and can be used
 * for communication.
 */
public class TaskResultMessage {
    /**
     * ID of task result.
     */
    private long taskResultId;

    /**
     * Constructor.
     *
     * @param taskResultId ID of task result
     */
    public TaskResultMessage(long taskResultId) {
        this.taskResultId = taskResultId;
    }

    /**
     * Getter for task result ID.
     *
     * @return ID of task result
     */
    public long getTaskResultId() {
        return taskResultId;
    }

    /**
     * Setter for task result ID.
     *
     * @param taskResultId ID of task result
     */
    public void setTaskResultId(long taskResultId) {
        this.taskResultId = taskResultId;
    }
}
