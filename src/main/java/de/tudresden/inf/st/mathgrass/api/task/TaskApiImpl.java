package de.tudresden.inf.st.mathgrass.api.task;

import de.tudresden.inf.st.mathgrass.api.apiModel.TaskApi;
import de.tudresden.inf.st.mathgrass.api.common.AbstractApiElement;
import de.tudresden.inf.st.mathgrass.api.feedback.results.TaskResultRepository;
import de.tudresden.inf.st.mathgrass.api.graph.GraphRepository;
import de.tudresden.inf.st.mathgrass.api.model.HintDTO;
import de.tudresden.inf.st.mathgrass.api.model.TaskDTO;
import de.tudresden.inf.st.mathgrass.api.model.TaskIdLabelTupleDTO;
import de.tudresden.inf.st.mathgrass.api.task.hint.Hint;
import de.tudresden.inf.st.mathgrass.api.task.hint.TaskHintTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * This class contains functionality to manage {@link TaskDTO}s.
 */
@RestController
public class TaskApiImpl extends AbstractApiElement implements TaskApi {
    /**
     * Task repository.
     */
    final TaskRepository taskRepository;

    /**
     * Graph repository.
     */
    final GraphRepository graphRepository;

    /**
     * Task result repository.
     */
    final TaskResultRepository taskResultRepository;

    /**
     * Constructor.
     *
     * @param taskRepository  task repository
     * @param graphRepository graph repository
     * @param taskResultRepository task result repository
     */
    public TaskApiImpl(TaskRepository taskRepository, GraphRepository graphRepository,
                       TaskResultRepository taskResultRepository) {
        this.taskRepository = taskRepository;
        this.graphRepository = graphRepository;
        this.taskResultRepository = taskResultRepository;
    }

    /**
     * Add a hint to a task.
     *
     * @param taskId   ID of task
     * @param taskHint hint to add to task
     * @return Response
     */
    @Override
    public ResponseEntity<Void> addTaskHint(Long taskId, HintDTO taskHint) {
        // get task entity
        Optional<Task> optTaskEntity = taskRepository.findById(taskId);
        if (optTaskEntity.isPresent()) {
            Task taskEntity = optTaskEntity.get();

            // add feedback and save
            taskEntity.getHints().add(new TaskHintTransformer().toEntity(taskHint));
            taskRepository.save(taskEntity);

            return ok();
        } else {
            return notFound();
        }
    }

    /**
     * Create a new task and save to database.
     *
     * @param body task to create
     * @return Response with task ID
     */
    @Override
    public ResponseEntity<Long> createTask(TaskDTO body) {
        Task taskEntity = taskRepository.save(new TaskTransformer(graphRepository).toEntity(body));

        return ok(taskEntity.getId());
    }

    /**
     * Get a hint for a task.
     *
     * @param taskId    ID of task
     * @param hintLevel level of hint
     * @return Response with hint
     */
    @Override
    public ResponseEntity<HintDTO> getHintForTask(Long taskId,
                                                  Integer hintLevel) {
        // get task entity
        Optional<Task> optTaskEntity = taskRepository.findById(taskId);
        if (optTaskEntity.isPresent()) {
            // load hints and get hint of specified level
            List<Hint> taskHints = optTaskEntity.get().getHints();
            if (taskHints.size() <= hintLevel) {
                return notFound();
            }

            return ok(new TaskHintTransformer().toDto(taskHints.get(hintLevel)));
        } else {
            return notFound();
        }
    }

    /**
     * Get a list of the IDs of all tasks.
     *
     * @return Response with list of IDs
     */
    @Override
    public ResponseEntity<List<TaskIdLabelTupleDTO>> getIdsOfAllTasks() {
        // find all tasks and extract IDs
        List<TaskIdLabelTupleDTO> taskIds = taskRepository.findAll().stream()
                .map(taskEntity -> new TaskIdLabelTupleDTO().label(taskEntity.getLabel()).id(taskEntity.getId()))
                .toList();

        return ok(taskIds);
    }

    /**
     * Get a task by its ID.
     *
     * @param taskId ID of task to get
     * @return Response with task
     */
    @Override
    public ResponseEntity<TaskDTO> getTaskById(Long taskId) {
        Optional<Task> optTaskEntity = taskRepository.findById(taskId);
        if (optTaskEntity.isPresent()) {
            TaskDTO task = new TaskTransformer(graphRepository)
                    .toDto(optTaskEntity.get());

            return ok(task);
        } else {
            return notFound();
        }
    }

    /**
     * Update a task.
     *
     * @param taskId ID of task
     * @param task   new task to replace old task with
     * @return Response
     */
    @Override
    public ResponseEntity<Void> updateTask(Long taskId, TaskDTO task) {
        Optional<Task> optTaskEntity = taskRepository.findById(taskId);
        if (optTaskEntity.isPresent()) {
            // create task entity
            Task taskEntity = new TaskTransformer(graphRepository).toEntity(task);
            taskEntity.setId(taskId);

            // save to database
            taskRepository.save(taskEntity);

            return ok();
        } else {
            return notFound();
        }
    }
}
