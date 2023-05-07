package de.tudresden.inf.st.mathgrass.api.task.execution;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.tudresden.inf.st.mathgrass.api.events.TaskEvaluationFinishedEvent;
import de.tudresden.inf.st.mathgrass.api.feedback.results.TaskResult;
import de.tudresden.inf.st.mathgrass.api.feedback.results.TaskResultRepository;
import de.tudresden.inf.st.mathgrass.api.task.Task;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import de.tudresden.inf.st.mathgrass.api.task.question.Question;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.equalTo;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskExecutionManagerTest {
    /**
     * Task execution manager.
     */
    @SpyBean
    private TaskExecutionManager taskExecutionManager;

    /**
     * Task execution worker.
     */
    @SpyBean
    private TaskExecutionWorker taskExecutionWorker;

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
     * Event bus.
     */
    @Autowired
    EventBus eventBus;

    /**
     * Task.
     */
    private Task task;

    /**
     * Set up a demo task for the tests.
     */
    @BeforeAll
    public void setUp() {
        // create task
        task = new Task();
        taskRepository.save(task);
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
        TaskResult taskResult = taskExecutionWorker.createTaskResult(task.getId(), userAnswerExpected);

        // check that exists in taskResultRepository
        Optional<TaskResult> optTaskResult = taskResultRepository.findById(taskResult.getId());

        assertTrue(optTaskResult.isPresent());

        // convert submission date to local date time
        LocalDateTime submissionDate = LocalDateTime.parse(taskResult.getSubmissionDate())
                .truncatedTo(ChronoUnit.MINUTES);

        // check that expected values exist
        assertEquals(userAnswerExpected, taskResult.getAnswer());
        assertEquals(submissionDateExpected, submissionDate);
        assertEquals(task.getId(), taskResult.getTask().getId());

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
        TaskResult taskResultBeforeUpdate = taskExecutionWorker.createTaskResult(task.getId(), "test");

        // check that evaluation date doesn't exist and isAnswerTrue is false
        assertNull(taskResultBeforeUpdate.getEvaluationDate());
        assertFalse(taskResultBeforeUpdate.isAnswerTrue());

        // now update task result
        taskExecutionWorker.updateTaskResult(taskResultBeforeUpdate.getId(), true);

        // get updated task result
        Optional<TaskResult> optTaskResult = taskResultRepository.findById(taskResultBeforeUpdate.getId());
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

        // create spy on task
        Task taskSpy = spy(task);

        // mock task accept question visitor
        Question mockQuestion = Mockito.mock(Question.class);
        doReturn(expectedResult).when(mockQuestion).acceptQuestionVisitor(any(), any(), anyLong(), anyString());
        doReturn(mockQuestion).when(taskSpy).getQuestion();

        // mock task repository to return taskSpy
        doReturn(Optional.of(taskSpy)).when(taskRepository).findById(anyLong());

        // make assessment, test fails if any exceptions thrown
        assertEquals(expectedResult, taskExecutionWorker.makeAssessment(taskSpy.getId(), "test"));
    }

    /**
     * Test that the task execution request returns a task result id and no errors are thrown.
     */
    @Test
    void testTaskExecutionRequestNoExceptions() {
        // mock taskExecutor to do nothing upon execution request
        doNothing().when(taskExecutor).execute(any());

        // make task execution request, test fails if any exceptions thrown
        assertNotNull(taskExecutionManager.requestTaskExecution(task.getId(), "test"));
    }

    /**
     * Test that the task execution request updates the task result correctly and publishes an event upon completion.
     */
    @Test
    void testTaskExecutionRequest() {
        // mock assessment and update of task result
        doReturn(true).when(taskExecutionWorker).makeAssessment(anyLong(), anyString());
        doNothing().when(taskExecutionWorker).updateTaskResult(anyLong(), anyBoolean());

        // create event bus subscriber
        EventBusSubscriber eventBusSubscriber = new EventBusSubscriber();

        // trigger task execution
        taskExecutionManager.requestTaskExecution(task.getId(), "test");

        // wait for event -> also tests that event has been published after task execution
        Awaitility.await().untilAtomic(eventBusSubscriber.getEventCount(), equalTo(1));

        // check that task result is updated
        verify(taskExecutionWorker, times(1)).updateTaskResult(anyLong(), anyBoolean());
    }

    /**
     * Helper class to subscribe to events.
     */
    public class EventBusSubscriber {
        private final AtomicInteger eventCount = new AtomicInteger();

        public EventBusSubscriber() {
            eventBus.register(this);
        }

        @Subscribe
        public void onEvent(TaskEvaluationFinishedEvent event) {
            eventCount.incrementAndGet();
        }

        public AtomicInteger getEventCount() {
            return eventCount;
        }
    }
}
