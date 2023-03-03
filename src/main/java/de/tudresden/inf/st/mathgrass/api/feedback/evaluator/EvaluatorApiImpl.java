package de.tudresden.inf.st.mathgrass.api.feedback.evaluator;

import de.tudresden.inf.st.mathgrass.api.common.AbstractApiElement;
import de.tudresden.inf.st.mathgrass.api.model.TaskResultDTO;
import de.tudresden.inf.st.mathgrass.api.task.Task;
import de.tudresden.inf.st.mathgrass.api.feedback.TaskResult;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import de.tudresden.inf.st.mathgrass.api.feedback.TaskResultRepository;
import de.tudresden.inf.st.mathgrass.api.model.RunStaticAssessment200Response;
import de.tudresden.inf.st.mathgrass.api.model.RunStaticAssessmentRequest;
import de.tudresden.inf.st.mathgrass.api.apiModel.EvaluatorApi;
import de.tudresden.inf.st.mathgrass.api.transform.TaskResultTransformer;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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
    // TODO: replace with Websockets
    /**
     * Thread pool for long polling, used for retrieving results.
     */
    private final ExecutorService longPollingTaskThreads =
            Executors.newFixedThreadPool(5);
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
     * @param taskRepository       task repository
     * @param taskResultRepository task result repository
     */
    public EvaluatorApiImpl(TaskRepository taskRepository,
                            TaskResultRepository taskResultRepository) {
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
    public ResponseEntity<TaskResultDTO> getTaskResult(Long id) {
        Optional<TaskResult> optTaskResultEntity =
                taskResultRepository.findById(id);

        if (optTaskResultEntity.isPresent()) {
            return ok(new TaskResultTransformer(taskRepository).toDto(optTaskResultEntity.get()));
        } else {
            return notFound();
        }
    }


    @GetMapping(value = "/evaluator/longPollTaskResult/{resultId}", produces
            = {"application/json"})
    DeferredResult<ResponseEntity<TaskResultDTO>> longPollTaskResult(@PathVariable("resultId") Long resultId) {
        // TODO - this is "rapid prototyping" for simulating a
        //  WebSocket-connection which notifies the client of a new result
        // change asap, integrate in OpenAPI-spec
        DeferredResult<ResponseEntity<TaskResultDTO>> output =
                new DeferredResult<>();

        longPollingTaskThreads.execute(() -> {
            TaskResult taskResultEntity =
                    taskResultRepository.findById(resultId).orElse(null);
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
                        taskResultEntity =
                                taskResultRepository.findById(resultId).orElse(null);
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

    @Override
    public ResponseEntity<RunStaticAssessment200Response> runStaticAssessment(
            @Parameter(name = "taskId", description = "ID of task", required
                    = true) @PathVariable("taskId") Long taskId,
            @Parameter(name = "RunStaticAssessmentRequest", description =
                    "Submitted answer", required = true) @Valid @RequestBody RunStaticAssessmentRequest runStaticAssessmentRequest
    ) {
        Optional<Task> optTask = taskRepository.findById(taskId);
        if (optTask.isPresent()) {
            // compare answers
            String expectedAnswer = optTask.get().getAnswer();
            boolean result =
                    expectedAnswer.equals(runStaticAssessmentRequest.getAnswer());
            return ok(new RunStaticAssessment200Response().isAssessmentCorrect(result));
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
        Optional<Task> optTask = taskRepository.findById(taskId);
        if (optTask.isEmpty()) {
            return notFound();
        }

        Task task = optTask.get();

        // set up task result entity
        TaskResult taskResult = new TaskResult();
        taskResult.setTask(task);
        taskResult.setAnswer(answer);
        taskResult.setSubmissionDate(LocalDateTime.now().toString());

        // save to db
        long taskResultId = taskResultRepository.save(taskResult).getId();


        return ok(taskResultId);
    }
}
