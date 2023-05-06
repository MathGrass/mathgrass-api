package de.tudresden.inf.st.mathgrass.api.websockets;

import com.google.common.eventbus.Subscribe;
import de.tudresden.inf.st.mathgrass.api.events.CustomEventBus;
import de.tudresden.inf.st.mathgrass.api.events.TaskEvaluationFinishedEvent;
import de.tudresden.inf.st.mathgrass.api.feedback.results.TaskResult;
import de.tudresden.inf.st.mathgrass.api.feedback.results.TaskResultRepository;
import de.tudresden.inf.st.mathgrass.api.task.execution.TaskExecutionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

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
     * Template for publishing task result IDs.
     */
    protected static final String TASK_RESULT_ID_TOPIC = "/topic/taskResultId/%s";

    /**
     * Task API for evaluation.
     */
    private final TaskExecutionManager taskExecutionManager;

    /**
     * Messaging template.
     */
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Event bus.
     */
    private final CustomEventBus eventBus;

    /**
     * Task result repository.
     */
    private final TaskResultRepository taskResultRepository;

    /**
     * Constructor.
     *
     * @param taskExecutionManager task execution manager
     * @param messagingTemplate messaging template
     * @param eventBus event bus
     * @param taskResultRepository task result repository
     */
    public WebSocketController(TaskExecutionManager taskExecutionManager, SimpMessagingTemplate messagingTemplate,
                               CustomEventBus eventBus, TaskResultRepository taskResultRepository) {
        this.taskExecutionManager = taskExecutionManager;
        this.messagingTemplate = messagingTemplate;
        this.eventBus = eventBus;
        this.taskResultRepository = taskResultRepository;
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
    public void evaluateTask(@Payload TaskSubmissionMessage message) {
        logger.info("Received submitted assessment task with ID {}", message.getTaskId());

        // get evaluation
        Long taskResultId = taskExecutionManager.requestTaskExecution(message.getTaskId(), message.getAnswer());

        // create listener for result, listener will notify client about result
        new TaskEvaluationCompletedListener(taskResultId, messagingTemplate, taskResultRepository);

        // notify client about task result ID
        messagingTemplate.convertAndSend(String.format(TASK_RESULT_ID_TOPIC, message.getTaskId()), taskResultId);
    }

    /**
     * Helper class to handle EventBus events.
     */
    public class TaskEvaluationCompletedListener {
        /**
         * Task result ID to listen to.
         */
        private final Long taskResultId;

        /**
         * Messaging template to notify websocket clients.
         */
        private final SimpMessagingTemplate messagingTemplate;

        /**
         * Task result repository.
         */
        private final TaskResultRepository taskResultRepository;

        /**
         * Constructor.
         *
         * @param taskResultId task result ID to listen to
         * @param messagingTemplate messaging template
         * @param taskResultRepository task result repository
         */
        public TaskEvaluationCompletedListener(Long taskResultId, SimpMessagingTemplate messagingTemplate,
                                               TaskResultRepository taskResultRepository) {
            eventBus.register(this);
            this.taskResultId = taskResultId;
            this.messagingTemplate = messagingTemplate;
            this.taskResultRepository = taskResultRepository;
        }

        /**
         * Handle task evaluation finished event.
         *
         * @param event event
         */
        @Subscribe
        public void onTaskCompletion(TaskEvaluationFinishedEvent event) {
            if (Objects.equals(event.taskResultId(), taskResultId)) {
                // get task result
                Optional<TaskResult> optTaskResult = taskResultRepository.findById(taskResultId);
                if (optTaskResult.isEmpty()) {
                    throw new IllegalArgumentException("Task result with ID " + taskResultId + " does not exist.");
                }
                TaskResult taskResult = optTaskResult.get();

                // send result to client and unregister listener
                messagingTemplate.convertAndSend(String.format(ASSESSMENT_RESULT_TOPIC, taskResultId),
                                                 taskResult.isAnswerTrue());
                eventBus.unregister(this);
            }
        }
    }
}
