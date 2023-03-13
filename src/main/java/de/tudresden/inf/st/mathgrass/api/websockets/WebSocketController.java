package de.tudresden.inf.st.mathgrass.api.websockets;

import de.tudresden.inf.st.mathgrass.api.feedback.evaluator.EvaluatorApiImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

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
    private static final String ASSESSMENT_RESULT_TOPIC = "/topic/assessmentResult/%s";

    /**
     * Messaging template.
     */
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Evaluator.
     */
    @Autowired
    private EvaluatorApiImpl evaluator;

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
    @MessageMapping("/evaluateDynamicAssessment")
    public void evaluateDynamicAssessment(@Payload TaskSubmissionMessage message) {
        logger.info("Received submitted assessment for static task with ID {}", message.getTaskId());

        // get evaluation
        boolean correctAnswer = evaluator.evaluateDynamicTask(message.getTaskId(), message.getAnswer());

        // broadcast result
        messagingTemplate.convertAndSend(ASSESSMENT_RESULT_TOPIC.formatted(message.getTaskId()), correctAnswer);
    }

    /**
     * Receive and evaluate a dynamic assessment, and broadcast result of the assessment.
     *
     * @param message message containing task ID and submitted answer
     */
    @MessageMapping("/evaluateStaticAssessment")
    public void evaluateStaticAssessment(@Payload TaskSubmissionMessage message) {
        logger.info("Received submitted assessment for dynamic task with ID {}", message.getTaskId());

        // get evaluation
        boolean correctAnswer = evaluator.evaluateStaticTask(message.getTaskId(), message.getAnswer());

        // broadcast result
        messagingTemplate.convertAndSend(ASSESSMENT_RESULT_TOPIC.formatted(message.getTaskId()), correctAnswer);
    }
}
