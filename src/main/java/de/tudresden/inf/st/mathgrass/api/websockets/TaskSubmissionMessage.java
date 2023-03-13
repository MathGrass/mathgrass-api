package de.tudresden.inf.st.mathgrass.api.websockets;

/**
 * This class contains information about the submission of a task and can be used for communication.
 */
public class TaskSubmissionMessage {
    /**
     * ID of task.
     */
    private int taskId;

    /**
     * Submitted answer.
     */
    private String answer;

    /**
     * Constructor.
     *
     * @param taskId ID of task
     * @param answer submitted answer
     */
    public TaskSubmissionMessage(int taskId, String answer) {
        this.taskId = taskId;
        this.answer = answer;
    }

    /**
     * Getter for task ID.
     *
     * @return ID of task
     */
    public int getTaskId() {
        return taskId;
    }

    /**
     * Setter for task ID.
     *
     * @param taskId ID of task
     */
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    /**
     * Getter for answer.
     *
     * @return submitted answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Setter for answer.
     *
     * @param answer submitted answer
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
