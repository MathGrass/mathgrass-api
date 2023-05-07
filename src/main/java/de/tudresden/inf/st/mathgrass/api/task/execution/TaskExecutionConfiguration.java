package de.tudresden.inf.st.mathgrass.api.task.execution;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * This class is used to configure and create the task executor for the {@link TaskExecutionManager}.
 */
@Component
public class TaskExecutionConfiguration {
    /**
     * Minimal number of threads kept alive in the thread pool. Number of threads only increases if queue is full.
     */
    @Value("${taskExecutor.corePoolSize:1}")
    private int corePoolSize;

    /**
     * Max number of concurrently running threads.
     */
    @Value("${taskExecutor.maxPoolSize:5}")
    private int maxPoolSize;

    /**
     * Max number of tasks stored in the queue.
     */
    @Value("${taskExecutor.queueCapacity:" + Integer.MAX_VALUE + "}")
    private int queueCapacity;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        // create task executor and set properties
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);

        return taskExecutor;
    }
}
