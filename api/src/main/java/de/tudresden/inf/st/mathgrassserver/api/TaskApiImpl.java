package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TaskApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskHintEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.GraphRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TagRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskSolverRepository;
import de.tudresden.inf.st.mathgrassserver.model.Feedback;
import de.tudresden.inf.st.mathgrassserver.model.Task;
import de.tudresden.inf.st.mathgrassserver.model.TaskHint;
import de.tudresden.inf.st.mathgrassserver.model.TaskIdLabelTuple;
import de.tudresden.inf.st.mathgrassserver.transform.FeedbackTransformer;
import de.tudresden.inf.st.mathgrassserver.transform.TaskHintTransformer;
import de.tudresden.inf.st.mathgrassserver.transform.TaskTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TaskApiImpl extends AbstractApiElement implements TaskApi {

    final TaskRepository taskRepository;

    final TaskSolverRepository taskSolverRepository;

    final GraphRepository graphRepository;
    final TagRepository tagRepository;

    public TaskApiImpl(TaskRepository taskRepository, TaskSolverRepository taskSolverRepository, GraphRepository graphRepository, TagRepository tagRepository) {
        this.taskRepository = taskRepository;
        this.taskSolverRepository = taskSolverRepository;
        this.graphRepository = graphRepository;
        this.tagRepository = tagRepository;
    }


    @Override
    public ResponseEntity<Void> addTaskFeedback(Long taskId, Feedback feedback) {
        checkExistence(taskId, taskRepository);
        TaskEntity taskEntity = taskRepository.findById(taskId).get();
        taskEntity.getFeedbacks().add(new FeedbackTransformer().toEntity(feedback));
        taskRepository.save(taskEntity);
        return ok();
    }

    @Override
    public ResponseEntity<Void> addTaskHint(Long taskId, TaskHint feedback) {
        checkExistence(taskId, taskRepository);
        TaskEntity taskEntity = taskRepository.findById(taskId).get();
        taskEntity.getHints().add(new TaskHintTransformer().toEntity(feedback));
        taskRepository.save(taskEntity);
        return ok();
    }

    @Override
    public ResponseEntity<Long> createTask(Task body) {
        TaskEntity taskEntity = taskRepository.save(new TaskTransformer(taskSolverRepository, graphRepository, tagRepository).toEntity(body));
        return ok(taskEntity.getId());
    }

    @Override
    public ResponseEntity<TaskHint> getHintForTask(Long taskId, String hintLevel) {
        TaskEntity taskEntity = taskRepository.findById(taskId).get();
        List<TaskHintEntity> taskHints = taskEntity.getHints();
        for (TaskHintEntity taskHintEntity : taskHints) {
            if (taskHintEntity.getLabel().equals(hintLevel)) {
                return ok(new TaskHintTransformer().toDto(taskHintEntity));
            }
        }
        return this.notFound();

    }

    @Override
    public ResponseEntity<List<TaskIdLabelTuple>> getIdsOfAllTasks() {
        List<TaskIdLabelTuple> taskIds = taskRepository.findAll().stream().map(taskEntity -> new TaskIdLabelTuple().label(taskEntity.getLabel()).id(taskEntity.getId())).collect(Collectors.toList());
        return ok(taskIds);
    }

    @Override
    public ResponseEntity<Task> getTaskById(Long id) {
        checkExistence(id, taskRepository);

        TaskEntity taskEntity = taskRepository.findById(id).get();
        Task task = new TaskTransformer(taskSolverRepository, graphRepository, tagRepository).toDto(taskEntity);
        return ok(task);
    }

    @Override
    public ResponseEntity<List<Feedback>> getTaskFeedback(Long taskId) {
        checkExistence(taskId, taskRepository);
        TaskEntity taskEntity = taskRepository.findById(taskId).get();
        List<Feedback> out = new FeedbackTransformer().toDtoList(taskEntity.getFeedbacks());
        return ok(out);
    }


    @Override
    public ResponseEntity<Void> updateTask(Long id, Task task) {
        checkExistence(id, taskRepository);

        TaskEntity taskEntity = new TaskTransformer(taskSolverRepository, graphRepository, tagRepository).toEntity(task);
        taskEntity.setId(id);
        taskRepository.save(taskEntity);
        return ok();
    }


}
