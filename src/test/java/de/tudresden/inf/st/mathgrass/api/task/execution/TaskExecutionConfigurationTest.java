package de.tudresden.inf.st.mathgrass.api.task.execution;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for {@link TaskExecutionConfiguration}. Tests that properties for the ThreadPoolTaskExecutor bean are
 * loaded correctly from application.properties.
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

    @Test
    void testApplicationPropertiesLoaded() {
        assertEquals(1, threadPoolTaskExecutor.getCorePoolSize());
        assertEquals(5, threadPoolTaskExecutor.getMaxPoolSize());
        assertEquals(10, threadPoolTaskExecutor.getThreadPoolExecutor().getQueue().remainingCapacity());
    }
}
