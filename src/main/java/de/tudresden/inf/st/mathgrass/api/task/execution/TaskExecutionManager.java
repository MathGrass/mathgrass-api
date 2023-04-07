package de.tudresden.inf.st.mathgrass.api.task.execution;

import com.google.common.eventbus.EventBus;
import de.tudresden.inf.st.mathgrass.api.events.TaskEvaluationFinishedEvent;
import de.tudresden.inf.st.mathgrass.api.feedback.results.TaskResult;
import de.tudresden.inf.st.mathgrass.api.feedback.results.TaskResultRepository;
import de.tudresden.inf.st.mathgrass.api.task.Task;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import de.tudresden.inf.st.mathgrass.api.task.question.QuestionVisitor;
import de.tudresden.inf.st.mathgrass.api.task.question.answer.AnswerVisitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * This class manages the execution of multiple requested task evaluations.
 *
 * <p>
 * The execution of tasks is handled by a {@link ThreadPoolTaskExecutor}, allowing the execution of multiple task
 * evaluations at the same time in an asynchronous way. To be executed task evaluations are stored in a queue, allowing
 * the limitation of the number of simultaneously running task evaluations.
 * Upon completion an event is emitted to notify listeners of the finished task.
 */
@Component
public class TaskExecutionManager {
    /**
     * Logger.
     */
    private final Logger logger = LogManager.getLogger(TaskExecutionManager.class);

    /**
     * Task executor.
     */
    private final ThreadPoolTaskExecutor taskExecutor;

    /**
     * Task repository.
     */
    private final TaskRepository taskRepository;

    /**
     * Task result repository.
     */
    private final TaskResultRepository taskResultRepository;

    /**
     * Question visitor
     */
    private final QuestionVisitor questionVisitor;

    /**
     * Answer visitor
     */
    private final AnswerVisitor answerVisitor;

    /**
     * Event bus for.
     */
    private final EventBus eventBus;

    /**
     * Constructor.
     *
     * @param taskExecutor task executor
     * @param taskRepository task repository
     * @param taskResultRepository task result repository
     * @param questionVisitor question visitor
     * @param answerVisitor answer visitor
     * @param eventBus event bus
     */
    public TaskExecutionManager(ThreadPoolTaskExecutor taskExecutor, TaskRepository taskRepository,
                                TaskResultRepository taskResultRepository, QuestionVisitor questionVisitor,
                                AnswerVisitor answerVisitor, EventBus eventBus) {
        this.taskExecutor = taskExecutor;
        this.taskRepository = taskRepository;
        this.taskResultRepository = taskResultRepository;
        this.questionVisitor = questionVisitor;
        this.answerVisitor = answerVisitor;
        this.eventBus = eventBus;
    }

    /**
     * Request the evaluation of a submitted answer to a task.
     *
     * @param taskId ID of task
     * @param userAnswer given answer
     * @return ID of task result
     * @throws IllegalArgumentException if task result couldn't be created
     */
    public Long requestTaskExecution(Long taskId, String userAnswer) throws IllegalArgumentException {
        logger.info("Requesting task evaluation for task with ID {}", taskId);

        // create task result
        Long taskResultId = createTaskResult(taskId, userAnswer);

        // get task result
        Optional<TaskResult> optTaskResult = taskResultRepository.findById(taskResultId);
        if (optTaskResult.isEmpty()) {
            throw new IllegalArgumentException("Task result with ID " + taskResultId + " does not exist");
        }
        TaskResult taskResult = optTaskResult.get();

        // request task evaluation
        taskExecutor.execute(() -> {
            logger.info("Starting task evaluation for task with ID {}", taskId);

            // evaluate answer
            boolean answerCorrect = makeAssessment(taskId, userAnswer);

            // update task result
            updateTaskResult(taskResult.getId(), answerCorrect);

            logger.info("Finished task evaluation for task with ID {}. Task result ID: {}", taskId,
                    taskResult.getId());

            // publish event
            eventBus.post(new TaskEvaluationFinishedEvent(taskResult.getId()));
        });

        return taskResult.getId();
    }

    /**
     * Evaluate the answer to a certain task.
     *
     * @param taskId ID of task
     * @param userAnswer given answer
     * @return boolean determining whether given answer was correct or not
     * @throws RuntimeException if an error occurs during the evaluation
     */
    public boolean makeAssessment(Long taskId, String userAnswer) throws RuntimeException {
        // get task
        Optional<Task> optTask = taskRepository.findById(taskId);
        if (optTask.isEmpty()) {
            throw new IllegalArgumentException("Couldn't find task with ID " + taskId);
        }

        Task task = optTask.get();
        try {
            return task.getQuestion().acceptQuestionVisitor(questionVisitor, answerVisitor, taskId, userAnswer);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a new task result without evaluation result and date.
     *
     * @param taskId ID of task the result refers to
     * @param userAnswer given answer
     * @return ID of task result
     */
    protected Long createTaskResult(Long taskId, String userAnswer) {
        // find task
        Optional<Task> optTask = taskRepository.findById(taskId);
        if (optTask.isEmpty()) {
            throw new IllegalArgumentException("Couldn't find task with ID " + taskId);
        }

        Task task = optTask.get();

        // create new task result and save
        TaskResult taskResult = new TaskResult();
        taskResult.setTask(task);
        taskResult.setAnswer(userAnswer);
        taskResult.setSubmissionDate(LocalDateTime.now().toString());
        taskResultRepository.save(taskResult);

        return taskResult.getId();
    }

    /**
     * Update the result of a task evaluation.
     *
     * @param taskResultId ID of task result
     * @param answerCorrect boolean determining whether given answer was correct or not
     */
    protected void updateTaskResult(Long taskResultId, boolean answerCorrect) {
        // find task result
        Optional<TaskResult> optTaskResult = taskResultRepository.findById(taskResultId);
        if (optTaskResult.isEmpty()) {
            throw new IllegalArgumentException("Couldn't find task result with ID " + taskResultId);
        }

        TaskResult taskResult = optTaskResult.get();

        // update task result
        taskResult.setAnswerTrue(answerCorrect);
        taskResult.setEvaluationDate(LocalDateTime.now().toString());
        taskResultRepository.save(taskResult);
    }
}