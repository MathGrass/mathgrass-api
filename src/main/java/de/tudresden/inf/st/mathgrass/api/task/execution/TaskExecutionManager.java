package de.tudresden.inf.st.mathgrass.api.task.execution;

import de.tudresden.inf.st.mathgrass.api.feedback.results.TaskResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * This class manages the execution of multiple requested task evaluations.
 *
 * <p>
 * The execution of tasks is handled by a {@link ThreadPoolTaskExecutor}, allowing the execution of multiple task
 * evaluations at the same time in an asynchronous way. To be executed task evaluations are stored in a queue, allowing
 * the limitation of the number of simultaneously running task evaluations.
 * Upon completion an event is emitted to notify listeners of the finished task.
 */
@Component
public class TaskExecutionManager {
    /**
     * Logger.
     */
    private final Logger logger = LogManager.getLogger(TaskExecutionManager.class);

    /**
     * Task executor.
     */
    private final ThreadPoolTaskExecutor taskExecutor;

    /**
     * Worker class.
     */
    private final TaskExecutionWorker taskExecutionWorker;

    /**
     * Constructor.
     *
     * @param taskExecutor task executor
     * @param taskExecutionWorker worker class
     */
    public TaskExecutionManager(ThreadPoolTaskExecutor taskExecutor, TaskExecutionWorker taskExecutionWorker) {
        this.taskExecutor = taskExecutor;
        this.taskExecutionWorker = taskExecutionWorker;
    }

    /**
     * Request the evaluation of a submitted answer to a task.
     *
     * @param taskId ID of task
     * @param userAnswer given answer
     * @return ID of task result
     * @throws IllegalArgumentException if task result couldn't be created
     */
    public Long requestTaskExecution(Long taskId, String userAnswer) throws IllegalArgumentException {
        logger.info("Requesting task evaluation for task with ID {}", taskId);

        // initialize task result
        TaskResult taskResult = taskExecutionWorker.createTaskResult(taskId, userAnswer);

        // request task evaluation
        taskExecutor.execute(() -> {
            // add short delay to ensure that the task result is returned before the task evaluation is started
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                logger.error("Task evaluation for task with ID {} was interrupted", taskId);
            }
            taskExecutionWorker.runTaskEvaluation(taskId, userAnswer, taskResult.getId());
        });

        return taskResult.getId();
    }
}
