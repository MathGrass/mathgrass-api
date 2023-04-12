package de.tudresden.inf.st.mathgrass.api.task.execution;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for {@link TaskExecutionConfiguration}.
 */
@SpringBootTest
@ActiveProfiles(profiles = "test")
@TestPropertySource(properties = {
        "taskExecutor.corePoolSize=1",
        "taskExecutor.maxPoolSize=5",
        "taskExecutor.queueCapacity=10"
})
class TaskExecutionConfigurationTest {
    /**
     * ThreadPoolTaskExecutor bean.
     */
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * Minimal number of threads kept alive in the thread pool.
     */
    @Value("${taskExecutor.corePoolSize}")
    private int corePoolSize;

    /**
     * Max number of concurrently running threads.
     */
    @Value("${taskExecutor.maxPoolSize}")
    private int maxPoolSize;

    /**
     * Max number of tasks stored in the queue.
     */
    @Value("${taskExecutor.queueCapacity}")
    private int queueCapacity;

    /**
     * Tests that properties for the ThreadPoolTaskExecutor bean are loaded correctly from application.properties.
     */
    @Test
    void testApplicationPropertiesLoaded() {
        assertEquals(corePoolSize, threadPoolTaskExecutor.getCorePoolSize());
        assertEquals(maxPoolSize, threadPoolTaskExecutor.getMaxPoolSize());
        assertEquals(queueCapacity, threadPoolTaskExecutor.getThreadPoolExecutor().getQueue().remainingCapacity());
    }
}
