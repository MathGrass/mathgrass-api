package de.tudresden.inf.st.mathgrass.api.task.execution;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Extension of {@link ThreadPoolTaskExecutor} that allows to schedule tasks with a delay.
 */
@Component
public class DelayedTaskExecutor {
    /**
     * Task executor.
     */
    private final ThreadPoolTaskExecutor taskExecutor;
    /**
     * Scheduled executor service.
     */
    private final ScheduledExecutorService scheduledExecutorService;

    /**
     * Constructor.
     *
     * @param taskExecutor task executor
     */
    public DelayedTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    /**
     * Execute a task with a delay.
     *
     * @param task task to execute
     * @param delay delay
     * @param timeUnit time unit
     */
    public void executeWithDelay(Runnable task, long delay, TimeUnit timeUnit) {
        scheduledExecutorService.schedule(() -> taskExecutor.execute(task), delay, timeUnit);
    }
}
