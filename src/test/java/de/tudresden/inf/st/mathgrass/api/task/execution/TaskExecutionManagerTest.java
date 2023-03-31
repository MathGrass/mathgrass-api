package de.tudresden.inf.st.mathgrass.api.task.execution;

import de.tudresden.inf.st.mathgrass.api.feedback.results.TaskResult;
import de.tudresden.inf.st.mathgrass.api.feedback.results.TaskResultRepository;
import de.tudresden.inf.st.mathgrass.api.task.Task;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import de.tudresden.inf.st.mathgrass.api.task.question.Question;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TaskExecutionManager}.
 */
@SpringBootTest
@ActiveProfiles(profiles = "test")
@TestPropertySource(properties = {
        "taskExecutor.corePoolSize=1",
        "taskExecutor.maxPoolSize=5",
        "taskExecutor.queueCapacity=10"
})
class TaskExecutionManagerTest {
    /**
     * Task execution manager.
     */
    @SpyBean
    private TaskExecutionManager taskExecutionManager;

    /**
     * Task executor.
     */
    @SpyBean
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * Task repository.
     */
    @SpyBean
    private TaskRepository taskRepository;

    /**
     * Task result repository.
     */
    @SpyBean
    private TaskResultRepository taskResultRepository;

    /**
     * Task spy.
     */
    private Task taskSpy;

    /**
     * Set up a demo task for the tests.
     */
    @BeforeEach
    public void setUp() {
        // create task
        Task task = new Task();
        taskRepository.save(task);

        taskSpy = Mockito.spy(task);
    }

    /**
     * Test that task result is created correctly with initial values.
     */
    @Test
    void testCreateTaskResult() {
        // set expected values
        String userAnswerExpected = "test";
        LocalDateTime submissionDateExpected = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        // create task result
        Long taskResultId = taskExecutionManager.createTaskResult(taskSpy.getId(), userAnswerExpected);

        // check that exists in taskResultRepository
        Optional<TaskResult> optTaskResult = taskResultRepository.findById(taskResultId);

        assertTrue(optTaskResult.isPresent());
        TaskResult taskResult = optTaskResult.get();

        // convert submission date to local date time
        LocalDateTime submissionDate = LocalDateTime.parse(taskResult.getSubmissionDate())
                .truncatedTo(ChronoUnit.MINUTES);

        // check that expected values exist
        assertEquals(userAnswerExpected, taskResult.getAnswer());
        assertEquals(submissionDateExpected, submissionDate);
        assertEquals(taskSpy.getId(), taskResult.getTask().getId());

        // check that other values of task result are not set
        assertNull(taskResult.getEvaluationDate());
        // can't check isAnswerTrue for null because default value is always false
        assertFalse(taskResult.isAnswerTrue());
    }

    /**
     * Test that task result is updated correctly. After the update the values
     * {@link de.tudresden.inf.st.mathgrass.api.feedback.results.TaskResult#answerTrue} and
     * {@link de.tudresden.inf.st.mathgrass.api.feedback.results.TaskResult#evaluationDate} should be set.
     */
    @Test
    void testUpdateTaskResult() {
        // create task result
        Long taskResultId = taskExecutionManager.createTaskResult(taskSpy.getId(), "test");

        // check that exists in taskResultRepository
        Optional<TaskResult> optTaskResult = taskResultRepository.findById(taskResultId);

        assertTrue(optTaskResult.isPresent());
        TaskResult taskResultBeforeUpdate = optTaskResult.get();

        // check that evaluation date doesn't exist and isAnswerTrue is false
        assertNull(taskResultBeforeUpdate.getEvaluationDate());
        assertFalse(taskResultBeforeUpdate.isAnswerTrue());

        // now update task result
        taskExecutionManager.updateTaskResult(taskResultId, true);

        // get updated task result
        optTaskResult = taskResultRepository.findById(taskResultId);
        assertTrue(optTaskResult.isPresent());
        TaskResult taskResultAfterUpdate = optTaskResult.get();

        // check that evaluation date exists and isAnswerTrue is true
        assertNotNull(taskResultAfterUpdate.getEvaluationDate());
        assertTrue(taskResultAfterUpdate.isAnswerTrue());
    }

    /**
     * Test that assessments work properly and don't throw unexpected exceptions. Exceptions are only thrown if there
     * are any DB issues, which are ok and not testable.
     */
    @Test
    void testAssessments() throws IOException, InterruptedException {
        // expected result
        boolean expectedResult = true;

        // mock task accept question visitor
        Question mockQuestion = Mockito.mock(Question.class);
        doReturn(expectedResult).when(mockQuestion).acceptQuestionVisitor(any(), any(), anyLong(), anyString());
        doReturn(mockQuestion).when(taskSpy).getQuestion();

        // mock task repository to return taskSpy
        doReturn(Optional.of(taskSpy)).when(taskRepository).findById(anyLong());

        // make assessment, test fails if any exceptions thrown
        assertEquals(expectedResult, taskExecutionManager.makeAssessment(taskSpy.getId(), "test"));
    }

    /**
     * Test that the task execution request returns a task result id and no errors are thrown.
     */
    @Test
    void testTaskExecutionRequestNoExceptions() {
        // mock taskExecutor to do nothing upon execution request
        doNothing().when(taskExecutor).execute(any());

        // make task execution request, test fails if any exceptions thrown
        assertNotNull(taskExecutionManager.requestTaskExecution(taskSpy.getId(), "test"));
    }

    /**
     * Test that the task execution request updates the task result correctly and publishes an event upon completion.
     */
    @Test
    void testTaskExecutionRequest() {
        // mock assessment and update of task result
        doReturn(true).when(taskExecutionManager).makeAssessment(anyLong(), anyString());
        doNothing().when(taskExecutionManager).updateTaskResult(anyLong(), anyBoolean());

        // create listener
        TaskEvaluationFinishedEventListener listener = new TaskEvaluationFinishedEventListener();

        // trigger task execution
        taskExecutionManager.requestTaskExecution(taskSpy.getId(), "test");

        // wait for task execution
        Awaitility.await().atLeast(Duration.ofMillis(1000));

        // check that task result is updated
        verify(taskExecutionManager, times(1)).updateTaskResult(anyLong(), anyBoolean());
        // check that event has been published
        assertTrue(listener.isEventReceived());
    }

    private static class TaskEvaluationFinishedEventListener {
        private boolean eventReceived = false;

        @EventListener
        public void handleEvent(TaskEvaluationFinishedEvent event) {
            eventReceived = true;
        }

        public boolean isEventReceived() {
            return eventReceived;
        }
    }
}
