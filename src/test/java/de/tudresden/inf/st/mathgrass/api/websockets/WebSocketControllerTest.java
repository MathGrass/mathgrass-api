package de.tudresden.inf.st.mathgrass.api.websockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Test class for {@link de.tudresden.inf.st.mathgrass.api.websockets.WebSocketController}.
 */
@SpringBootTest
class WebSocketControllerTest {
    /**
     * Websocket controller.
     */
    @Autowired
    private WebSocketController webSocketController;

    /**
     * Messaging template.
     */
    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Test that incoming task submissions for dynamic tasks are handled correctly.
     */
//    @Test
//    void testEvaluateDynamicAssessment() {
//        // example task submission
//        long taskId = 1;
//        TaskSubmissionMessage message = new TaskSubmissionMessage(taskId, "Test");
//
//        // expected result
//        boolean expectedResult = true;
//
//        // mock evaluator api
//        // Mockito.when(evaluatorApi.evaluateDynamicTask(anyLong(), anyString())).thenReturn(expectedResult);
//
//        // expected message to be sent
//        String expectedChannel = ASSESSMENT_RESULT_TOPIC.formatted(message.getTaskId());
//
//        // call websocket controller function
//        webSocketController.evaluateDynamicAssessment(message);
//
//        // verify that messaging template has sent message
//        Mockito.verify(messagingTemplate, Mockito.times(1))
//                .convertAndSend(expectedChannel, expectedResult);
//    }
//
//    /**
//     * Test that incoming task submissions for static tasks are handled correctly.
//     */
//    @Test
//    void testEvaluateStaticAssessment() {
//        // example task submission
//        long taskId = 1;
//        TaskSubmissionMessage message = new TaskSubmissionMessage(taskId, "Test");
//
//        // expected result
//        boolean expectedResult = true;
//
//        // mock evaluator api
//        Mockito.when(evaluatorApi.evaluateStaticTask(anyLong(), anyString())).thenReturn(expectedResult);
//
//        // expected message to be sent
//        String expectedChannel = ASSESSMENT_RESULT_TOPIC.formatted(message.getTaskId());
//
//        // call websocket controller function
//        webSocketController.evaluateStaticAssessment(message);
//
//        // verify that messaging template has sent message
//        Mockito.verify(messagingTemplate, Mockito.times(1))
//                .convertAndSend(expectedChannel, expectedResult);
//    }
}
