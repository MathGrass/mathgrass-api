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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class EvaluatorApiImpl extends AbstractApiElement implements EvaluatorApi {

    private final ExecutorService longPollingTaskThreads = Executors.newFixedThreadPool(5);
    final TaskRepository taskRepository;
    final TaskResultRepository taskResultRepository;

    public EvaluatorApiImpl(TaskRepository taskRepository, TaskResultRepository taskResultRepository) {
        this.taskRepository = taskRepository;
        this.taskResultRepository = taskResultRepository;
    }


    @Override
    public ResponseEntity<TaskResult> getTaskResult(Long id) {
        TaskResultEntity taskResultEntity = taskResultRepository.findById(id).get();
        return ok(new TaskResultTransformer(taskRepository).toDto(taskResultEntity));
    }

    @ApiOperation(value = "", notes = "Long poll the result for an evaluation process", response = TaskResult.class, tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = TaskResult.class) })
    @RequestMapping(value = "/evaluator/longPollTaskResult/{resultId}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    DeferredResult<ResponseEntity<TaskResult>> longPollTaskResult(@ApiParam(value = "ID of task",required=true ) @PathVariable("resultId") Long resultId){
        // TODO - this is "rapid prototyping" for simulating a WebSocket-connection which notifies the client of a new result
        // change asap, integrate in OpenAPI-spec
        DeferredResult<ResponseEntity<TaskResult>> output = new DeferredResult<>();

        longPollingTaskThreads.execute( () -> {
            TaskResultEntity taskResultEntity = taskResultRepository.findById(resultId).orElse(null);
            if(taskResultEntity == null){
                return;
            }
            if(taskResultEntity.getEvaluationDate() == null){
                int retries = 0;
                final int MAX_RETRIES = 20;
                final int SLEEP_TIME_BETWEEN_DB_LOOKUPS = 200;

                while(taskResultEntity.getEvaluationDate() == null && retries < MAX_RETRIES){
                    try {
                        Thread.sleep(SLEEP_TIME_BETWEEN_DB_LOOKUPS);
                        taskResultEntity = taskResultRepository.findById(resultId).orElse(null);
                        retries++;
                        System.out.println("in here");
                        if(taskResultEntity != null && taskResultEntity.getEvaluationDate() != null){
                            output.setResult(ok(new TaskResultTransformer(taskRepository).toDto(taskResultEntity)));
                            break;
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        return output;
    }

    @Override
    public ResponseEntity<Long> runTask(Long taskId, String answer) {

        checkExistence(taskId,taskRepository);

        TaskEntity task = taskRepository.findById(taskId).get();

        // save result to db
        TaskResultEntity taskResult = new TaskResultEntity();
        taskResult.setTask(task);
        taskResult.setAnswer(answer);

        taskResult.setSubmissionDate(LocalDateTime.now().toString());

        long taskResuldId = taskResultRepository.save(taskResult).getId();

        boolean isDynamicAnswer = task.getTaskTemplate() != null;

        if (isDynamicAnswer) {
            new TaskManager().runTask(taskResuldId,task.getId(),answer);
        }

        return ok(taskResuldId);
    }
}
