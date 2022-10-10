package de.tudresden.inf.st.mathgrassserver.api;

import com.google.gson.Gson;
import de.tudresden.inf.st.mathgrassserver.apiModel.EvaluatorApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskResultEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskSolverEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskResultRepository;
import de.tudresden.inf.st.mathgrassserver.evaluator.EvaluationRequestMessage;
import de.tudresden.inf.st.mathgrassserver.evaluator.MessageBrokerConn;
import de.tudresden.inf.st.mathgrassserver.evaluator.Queue;
import de.tudresden.inf.st.mathgrassserver.evaluator.TaskManager;
import de.tudresden.inf.st.mathgrassserver.model.TaskResult;
import de.tudresden.inf.st.mathgrassserver.transform.TaskResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Random;

@RestController
public class EvaluatorApiImpl extends AbsApi implements EvaluatorApi {


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

    @Override
    public ResponseEntity<Long> runTask(Long taskId, String answer) {

        checkExistence(taskId,taskRepository);

        TaskEntity task = taskRepository.findById(taskId).get();

        // save result to db
        TaskResultEntity taskResult = new TaskResultEntity();
        taskResult.setTask(task);
        taskResult.setAnswer(answer);
        taskResult.setDate(Instant.now().toString());
        long taskResuldId = taskResultRepository.save(taskResult).getId();

        boolean isDynamicAnswer = task.getTaskTemplate() != null;

        if (isDynamicAnswer) {
            new TaskManager().runTask(taskResuldId,task.getId(),answer);
        }

        return ok(taskResuldId);
    }
}
