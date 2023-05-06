package de.tudresden.inf.st.mathgrass.api.task.execution;

import de.tudresden.inf.st.mathgrass.api.feedback.results.TaskResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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
    private final DelayedTaskExecutor taskExecutor;

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
    public TaskExecutionManager(DelayedTaskExecutor taskExecutor, TaskExecutionWorker taskExecutionWorker) {
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

        // request task evaluation with delay to ensure that task result is returned before task evaluation is started
        taskExecutor.executeWithDelay(
                () -> taskExecutionWorker.runTaskEvaluation(taskId, userAnswer, taskResult.getId()),
                50, TimeUnit.MILLISECONDS);

        return taskResult.getId();
    }
}
