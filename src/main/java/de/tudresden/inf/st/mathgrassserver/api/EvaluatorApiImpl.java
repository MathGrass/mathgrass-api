package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.EvaluatorApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskResultEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskResultRepository;
import de.tudresden.inf.st.mathgrassserver.evaluator.TaskManager;
import de.tudresden.inf.st.mathgrassserver.model.TaskResult;
import de.tudresden.inf.st.mathgrassserver.transform.TaskResultTransformer;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class handles the communication with the evaluator, which includes the triggering of evaluation tasks and
 * the receiving of solutions.
 */
@RestController
public class EvaluatorApiImpl extends AbstractApiElement implements EvaluatorApi {
    // TODO: replace with Websockets
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

    @ApiOperation(value = "", notes = "Long poll the result for an evaluation process", response = TaskResult.class,
            tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "successful operation", response = TaskResult.class)})
    @GetMapping(value = "/evaluator/longPollTaskResult/{resultId}", produces = {"application/json"})
    DeferredResult<ResponseEntity<TaskResult>> longPollTaskResult(@ApiParam(value = "ID of task", required = true)
                                                                  @PathVariable("resultId") Long resultId) {
        // TODO - this is "rapid prototyping" for simulating a WebSocket-connection which notifies the client of a new result
        // change asap, integrate in OpenAPI-spec
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

    // TODO: include in OpenAPI-Spec - datatype + request/response

    /**
     * Record containing the answer given by the user.
     *
     * @param answer user specified answer
     */
    record UserAnswer(String answer){}

    /**
     * Get the assessment for a task ID and a {@link UserAnswer}.
     *
     * @param taskId ID of task
     * @param answer answer of user
     * @return {@link ResponseEntity} containing a boolean determining the correctness of the answer
     */
    @PostMapping(value = "/evaluator/staticEvaluation/{taskId}", consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<Boolean> getStaticAssessment(
            @ApiParam(value = "ID of task", required = true) @PathVariable("taskId") Long taskId,
            @ApiParam(value = "Answer of student", required = true) @RequestBody UserAnswer answer) {
        // load task from repository
        Optional<TaskEntity> optTask = taskRepository.findById(taskId);
        if (optTask.isPresent()) {
            // compare answers
            String expectedAnswer = optTask.get().getAnswer();

            return ok(expectedAnswer.equals(answer.answer()));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
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
