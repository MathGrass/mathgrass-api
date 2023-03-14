package de.tudresden.inf.st.mathgrass.api.feedback.evaluator;

import de.tudresden.inf.st.mathgrass.api.common.AbstractApiElement;
import de.tudresden.inf.st.mathgrass.api.transform.TaskEntity;
import de.tudresden.inf.st.mathgrass.api.feedback.TaskResultEntity;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import de.tudresden.inf.st.mathgrass.api.feedback.TaskResultRepository;
import de.tudresden.inf.st.mathgrass.api.model.TaskResult;
import de.tudresden.inf.st.mathgrass.api.apiModel.EvaluatorApi;
import de.tudresden.inf.st.mathgrass.api.transform.TaskResultTransformer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class handles the communication with the evaluator, which includes
 * the triggering of evaluation tasks and
 * the receiving of solutions.
 */
@RestController
public class EvaluatorApiImpl extends AbstractApiElement implements EvaluatorApi {
    /**
     * Thread pool for long polling, used for retrieving results.
     */
    private final ExecutorService longPollingTaskThreads = Executors.newFixedThreadPool(5);
    /**
     * Task repository.
     */
    final TaskRepository taskRepository;
    /**
     * Task result repository.
     */
    final TaskResultRepository taskResultRepository;

    /**
     * Constructor.
     *
     * @param taskRepository task repository
     * @param taskResultRepository task result repository
     */
    public EvaluatorApiImpl(TaskRepository taskRepository, TaskResultRepository taskResultRepository) {
        this.taskRepository = taskRepository;
        this.taskResultRepository = taskResultRepository;
    }

    /**
     * Get the {@link ResponseEntity} for a task result.
     *
     * @param id ID of task result
     * @return task result object
     */
    @Override
    public ResponseEntity<TaskResult> getTaskResult(Long id) {
        Optional<TaskResultEntity> optTaskResultEntity = taskResultRepository.findById(id);

        if (optTaskResultEntity.isPresent()) {
            return ok(new TaskResultTransformer(taskRepository).toDto(optTaskResultEntity.get()));
        } else {
            return notFound();
        }
    }

    @GetMapping(value = "/evaluator/longPollTaskResult/{resultId}", produces = {"application/json"})
    DeferredResult<ResponseEntity<TaskResult>> longPollTaskResult(@PathVariable("resultId") Long resultId) {
        // TODO - this is "rapid prototyping" for simulating a WebSocket-connection which notifies the client of a new result
        // change asap, integrate in OpenAPI-spec
        // TODO websockets: listen to rabbitmq publish
        DeferredResult<ResponseEntity<TaskResult>> output = new DeferredResult<>();

        longPollingTaskThreads.execute(() -> {
            TaskResultEntity taskResultEntity = taskResultRepository.findById(resultId).orElse(null);
            if (taskResultEntity == null) {
                return;
            }
            if (taskResultEntity.getEvaluationDate() == null) {
                int retries = 0;
                final int MAX_RETRIES = 500;
                final int SLEEP_TIME_BETWEEN_DB_LOOKUPS = 500;

                while (taskResultEntity.getEvaluationDate() == null && retries < MAX_RETRIES) {
                    try {
                        Thread.sleep(SLEEP_TIME_BETWEEN_DB_LOOKUPS);
                        taskResultEntity = taskResultRepository.findById(resultId).orElse(null);
                        retries++;
                        if (taskResultEntity != null && taskResultEntity.getEvaluationDate() != null) {
                            output.setResult(ok(new TaskResultTransformer(taskRepository).toDto(taskResultEntity)));
                            break;
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                output.setResult(ok(new TaskResultTransformer(taskRepository).toDto(taskResultEntity)));
            }
        });
        return output;
    }

    /**
     * Evaluate the given answer of a static task and return the result.
     *
     * @param taskId ID of task
     * @param answer answer specified by user
     * @return correctness of answer
     * @throws IllegalArgumentException if task doesn't exist
     */
    public boolean evaluateStaticTask(long taskId, String answer) throws IllegalArgumentException {
        // load task from repository
        Optional<TaskEntity> optTask = taskRepository.findById(taskId);
        if (optTask.isPresent()) {
            // compare answers
            String expectedAnswer = optTask.get().getAnswer();

            return expectedAnswer.equals(answer);
        } else {
            throw new IllegalArgumentException(String.format("Couldn't find task with ID %s!", taskId));
        }
    }

    /**
     * Evaluate the given answer of a dynamic task and return the result.
     *
     * @param taskId ID of task
     * @param answer answer specified by user
     * @return DeferredResult with {@link TaskResult}
     * @throws IllegalArgumentException if task doesn't exist
     */
    public DeferredResult<ResponseEntity<TaskResult>> evaluateDynamicTask(long taskId, String answer)
            throws IllegalArgumentException {
        // request evaluation of task
        ResponseEntity<Long> taskResultResponse = runTask(taskId, answer);
        if (!taskResultResponse.getStatusCode().equals(HttpStatus.OK)) {
            throw new IllegalArgumentException(String.format("Couldn't find task with ID %s!", taskId));
        }

        // get task result id
        if (taskResultResponse.getBody() != null) {
            throw new IllegalArgumentException(String.format("Couldn't find task result with ID %s!", taskId));
        }
        long taskResultId = taskResultResponse.getBody();

        // get result of task result as DeferredResult
        return longPollTaskResult(taskResultId);
    }

    /**
     * Send a task to the evaluator and run the task.
     *
     * @param taskId ID of task
     * @param answer answer given by user
     * @return ID of task result
     */
    @Override
    public ResponseEntity<Long> runTask(Long taskId, String answer) {
        // get task from repository
        Optional<TaskEntity> optTask = taskRepository.findById(taskId);
        if (optTask.isEmpty()) {
            return notFound();
        }

        TaskEntity task = optTask.get();

        // set up task result entity
        TaskResultEntity taskResult = new TaskResultEntity();
        taskResult.setTask(task);
        taskResult.setAnswer(answer);
        taskResult.setSubmissionDate(LocalDateTime.now().toString());

        // save to db
        long taskResultId = taskResultRepository.save(taskResult).getId();

        // check if answer is dynamic
        boolean isDynamicAnswer = task.getTaskTemplate() != null;

        // if answer is dynamic it is necessary to use evaluator
        if (isDynamicAnswer) {
            new TaskManager().runTask(taskResultId, task.getId(), answer);
        }

        return ok(taskResultId);
    }
}
