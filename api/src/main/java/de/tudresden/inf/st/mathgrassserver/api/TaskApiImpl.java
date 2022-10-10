package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TaskApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.GraphRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskSolverRepository;
import de.tudresden.inf.st.mathgrassserver.model.Feedback;
import de.tudresden.inf.st.mathgrassserver.model.InputAnswer;
import de.tudresden.inf.st.mathgrassserver.model.Task;
import de.tudresden.inf.st.mathgrassserver.model.TaskHint;
import de.tudresden.inf.st.mathgrassserver.transform.FeedbackTransformer;
import de.tudresden.inf.st.mathgrassserver.transform.TaskHintTransformer;
import de.tudresden.inf.st.mathgrassserver.transform.TaskTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskApiImpl extends AbsApi implements TaskApi {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskSolverRepository taskSolverRepository;

    @Autowired
    GraphRepository graphRepository;


    @Override
    public ResponseEntity<Void> addTaskFeedback(Long taskId, Feedback feedback) {
        checkExistence(taskId,taskRepository);
        TaskEntity taskEntity = taskRepository.findById(taskId).get();
        taskEntity.getFeedbacks().add(new FeedbackTransformer().toEntity(feedback));
        return ok();
    }

    @Override
    public ResponseEntity<Void> addTaskHint(Long taskId, TaskHint feedback) {
        checkExistence(taskId,taskRepository);
        TaskEntity taskEntity = taskRepository.findById(taskId).get();
        taskEntity.getHints().add(new TaskHintTransformer().toEntity(feedback));
        return ok();
    }

    @Override
    public ResponseEntity<Long> createTask(Task body) {
        TaskEntity taskEntity = taskRepository.save(new TaskTransformer(taskSolverRepository,graphRepository).toEntity(body));
        return ok(taskEntity.getId());
    }

    @Override
    public ResponseEntity<Task> getTaskById(Long id) {
        checkExistence(id,taskRepository);

        TaskEntity taskEntity = taskRepository.findById(id).get();
        Task task = new TaskTransformer(taskSolverRepository,graphRepository).toDto(taskEntity);
        return ok(task);
    }

    @Override
    public ResponseEntity<List<Feedback>> getTaskFeedback(Long taskId) {
        checkExistence(taskId,taskRepository);
        TaskEntity taskEntity = taskRepository.findById(taskId).get();
        List<Feedback> out = new FeedbackTransformer().toDtoList(taskEntity.getFeedbacks());
        return ok(out);
    }

    @Override
    public ResponseEntity<Void> submitAnswer(Long taskId, InputAnswer inputAnswer) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateTask(Long id, Task task) {
        checkExistence(id,taskRepository);

        TaskEntity taskEntity = new TaskTransformer(taskSolverRepository,graphRepository).toEntity(task);
        taskEntity.setId(id);
        taskRepository.save(taskEntity);
        return ok();
    }



}
