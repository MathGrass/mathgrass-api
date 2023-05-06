package de.tudresden.inf.st.mathgrass.api.websockets;

import de.tudresden.inf.st.mathgrass.api.events.CustomEventBus;
import de.tudresden.inf.st.mathgrass.api.events.TaskEvaluationFinishedEvent;
import de.tudresden.inf.st.mathgrass.api.feedback.results.TaskResult;
import de.tudresden.inf.st.mathgrass.api.feedback.results.TaskResultRepository;
import de.tudresden.inf.st.mathgrass.api.task.execution.TaskExecutionManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static de.tudresden.inf.st.mathgrass.api.websockets.WebSocketController.ASSESSMENT_RESULT_TOPIC;
import static de.tudresden.inf.st.mathgrass.api.websockets.WebSocketController.TASK_RESULT_ID_TOPIC;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link de.tudresden.inf.st.mathgrass.api.websockets.WebSocketController}.
 */
@SpringBootTest
@ActiveProfiles(profiles = "test")
class WebSocketControllerTest {
    /**
     * Websocket controller.
     */
    @SpyBean
    private WebSocketController webSocketController;

    /**
     * Messaging template.
     */
    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Task execution manager
     */
    @MockBean
    private TaskExecutionManager taskExecutionManager;

    /**
     * Task result repository.
     */
    @MockBean
    private TaskResultRepository taskResultRepository;

    /**
     * Event bus.
     */
    @Autowired
    private CustomEventBus eventBus;

    /**
     * Test that calling the evaluateTask endpoint notifies clients about the generated task result ID, as well as that
     * a listener has been created, which is an object of the class
     * {@link de.tudresden.inf.st.mathgrass.api.websockets.WebSocketController.TaskEvaluationCompletedListener}.
     */
    @Test
    void taskEvaluationClientCommunication() {
        // define task result id
        Long expectedTaskResultId = 1L;

        // make request for task execution return expected task result id
        when(taskExecutionManager.requestTaskExecution(anyLong(), anyString())).thenReturn(expectedTaskResultId);

        // create task submission message as input
        TaskSubmissionMessage taskSubmissionMessage = new TaskSubmissionMessage(1L, "test");

        // call function
        webSocketController.evaluateTask(taskSubmissionMessage);

        // get listener from eventBus
        Object listener = eventBus.getRegisteredListeners().stream().findAny().orElse(null);

        // check that listener is not null
        assertNotNull(listener);

        // check that task execution manager was called
        verify(taskExecutionManager).requestTaskExecution(anyLong(), anyString());

        // check that the messaging template has been called with the expected task result id
        verify(messagingTemplate).convertAndSend(String.format(TASK_RESULT_ID_TOPIC, taskSubmissionMessage.getTaskId()),
                expectedTaskResultId);
    }

    /**
     * Test that the {@link de.tudresden.inf.st.mathgrass.api.websockets.WebSocketController.TaskEvaluationCompletedListener}
     * sends a message via the messaging template when the task evaluation has been completed. Also tests that listener
     * triggers on the correct event.
     */
    @Test
    void listenerNotifiesClientOnCompletion() {
        // set task result id
        Long taskResultId = 1L;

        // create listener
        webSocketController.new TaskEvaluationCompletedListener(taskResultId, messagingTemplate, taskResultRepository);

        // create event
        TaskEvaluationFinishedEvent event = new TaskEvaluationFinishedEvent(taskResultId);

        // create task result object
        TaskResult taskResult = new TaskResult();
        taskResult.setAnswerTrue(true);

        // mock repo to return task result
        when(taskResultRepository.findById(taskResultId)).thenReturn(Optional.of(taskResult));

        // send event
        eventBus.post(event);

        // check that the messaging template has been called with the expected task result id and result
        verify(messagingTemplate).convertAndSend(String.format(ASSESSMENT_RESULT_TOPIC, taskResultId),
                taskResult.isAnswerTrue());

        // send event with wrong ID, verify that messaging template has not been called
        eventBus.post(new TaskEvaluationFinishedEvent(2L));
        verify(messagingTemplate, atMostOnce()).convertAndSend(anyString(), anyLong());
    }
}
