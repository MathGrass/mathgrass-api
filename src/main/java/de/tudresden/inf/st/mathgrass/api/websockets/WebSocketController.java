package de.tudresden.inf.st.mathgrass.api.websockets;

import de.tudresden.inf.st.mathgrass.api.feedback.evaluator.EvaluatorApiImpl;
import de.tudresden.inf.st.mathgrass.api.model.TaskResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

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
     * Messaging template.
     */
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Evaluator.
     */
    private final EvaluatorApiImpl evaluator;

    /**
     * Constructor.
     *
     * @param messagingTemplate messaging template
     * @param evaluator evaluator
     */
    public WebSocketController(SimpMessagingTemplate messagingTemplate, EvaluatorApiImpl evaluator) {
        this.messagingTemplate = messagingTemplate;
        this.evaluator = evaluator;
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
    @MessageMapping("/evaluateDynamicAssessment")
    public void evaluateDynamicAssessment(@Payload TaskSubmissionMessage message) {
        logger.info("Received submitted assessment for dynamic task with ID {}", message.getTaskId());

        // get evaluation
        DeferredResult<ResponseEntity<TaskResult>> taskResult =
                evaluator.evaluateDynamicTask(message.getTaskId(), message.getAnswer());

        // broadcast result on completion of evaluation
        taskResult.onCompletion(() -> {
            logger.info("Done evaluating dynamic task!");

            TaskResult result = (TaskResult) taskResult.getResult();
            if (result != null) {
                messagingTemplate.convertAndSend(ASSESSMENT_RESULT_TOPIC.formatted(message.getTaskId()),
                        result.getAnswerTrue());
            }
        });
    }

    /**
     * Receive and evaluate a dynamic assessment, and broadcast result of the assessment.
     *
     * @param message message containing task ID and submitted answer
     */
    @MessageMapping("/evaluateStaticAssessment")
    public void evaluateStaticAssessment(@Payload TaskSubmissionMessage message) {
        logger.info("Received submitted assessment for static task with ID {}", message.getTaskId());

        // get evaluation
        boolean correctAnswer = evaluator.evaluateStaticTask(message.getTaskId(), message.getAnswer());

        // broadcast result
        messagingTemplate.convertAndSend(ASSESSMENT_RESULT_TOPIC.formatted(message.getTaskId()), correctAnswer);
    }
}
