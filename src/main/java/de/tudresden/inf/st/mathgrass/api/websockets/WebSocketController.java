package de.tudresden.inf.st.mathgrass.api.websockets;

import de.tudresden.inf.st.mathgrass.api.model.EvaluateAnswerRequest;
import de.tudresden.inf.st.mathgrass.api.task.TaskApiImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * This class handles websocket messages.
 */
@RestController
public class WebSocketController {
    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger(WebSocketController.class);

    /**
     * Template for assessment result channel for publishing results.
     */
    protected static final String ASSESSMENT_RESULT_TOPIC = "/topic/assessmentResult/%s";

    /**
     * Task API for evaluation.
     */
    private final TaskApiImpl taskApi;

    /**
     * Messaging template.
     */
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Constructor.
     *
     * @param taskApi task API
     * @param messagingTemplate messaging template
     */
    public WebSocketController(TaskApiImpl taskApi, SimpMessagingTemplate messagingTemplate) {
        this.taskApi = taskApi;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Handle messages sent to '/app/sendMessage'. Mainly used for development purposes to test connection to client.
     *
     * @param message message
     */
    @MessageMapping("/sendMessage")
    public void onMessage(@Payload String message) {
        // handle message
        logger.info("Received message: {}", message);
    }

    /**
     * Receive and evaluate a dynamic assessment, and broadcast result of the assessment.
     *
     * @param message message containing task ID and submitted answer
     */
    @MessageMapping("/fetchAssessment")
    public void evaluateStaticAssessment(@Payload TaskSubmissionMessage message) {
        logger.info("Received submitted assessment task with ID {}", message.getTaskId());

        // get evaluation
        boolean correctAnswer = taskApi.makeAssessment(message.getTaskId(), message.getAnswer());

        // broadcast result
        messagingTemplate.convertAndSend(ASSESSMENT_RESULT_TOPIC.formatted(message.getTaskId()), correctAnswer);
    }
}
