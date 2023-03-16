package de.tudresden.inf.st.mathgrass.api.evaluator;

import de.tudresden.inf.st.mathgrass.api.evaluator.executor.Executor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskManagerTest {
    @Test
    void runTaskSmokeTest() {
        Executor executor = new Executor();
        executor.setContainerImage("alpine");
        boolean result = TaskManager.runTaskSynchronously(1, "my answer", executor);
        assertTrue(result);
    }

}