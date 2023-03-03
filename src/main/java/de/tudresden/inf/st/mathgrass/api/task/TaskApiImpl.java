package de.tudresden.inf.st.mathgrass.api.task;

import de.tudresden.inf.st.mathgrass.api.common.AbstractApiElement;
import de.tudresden.inf.st.mathgrass.api.label.LabelRepository;
import de.tudresden.inf.st.mathgrass.api.feedback.TaskSolverRepository;
import de.tudresden.inf.st.mathgrass.api.graph.GraphRepository;
import de.tudresden.inf.st.mathgrass.api.transform.TaskEntity;
import de.tudresden.inf.st.mathgrass.api.hint.TaskHintEntity;
import de.tudresden.inf.st.mathgrass.api.model.Feedback;
import de.tudresden.inf.st.mathgrass.api.model.Task;
import de.tudresden.inf.st.mathgrass.api.model.TaskHint;
import de.tudresden.inf.st.mathgrass.api.model.TaskIdLabelTuple;
import de.tudresden.inf.st.mathgrass.api.apiModel.TaskApi;
import de.tudresden.inf.st.mathgrass.api.transform.FeedbackTransformer;
import de.tudresden.inf.st.mathgrass.api.transform.TaskHintTransformer;
import de.tudresden.inf.st.mathgrass.api.transform.TaskTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * This class contains functionality to manage {@link Task}s.
 */
@RestController
public class TaskApiImpl extends AbstractApiElement implements TaskApi {
    /**
     * Task repository.
     */
    final TaskRepository taskRepository;

    /**
     * Task solver repository.
     */
    final TaskSolverRepository taskSolverRepository;

    /**
     * Graph repository.
     */
    final GraphRepository graphRepository;

    /**
     * Tag repository.
     */
    final LabelRepository labelRepository;

    /**
     * Task template repository.
     */


    /**
     * Constructor.
     *
     * @param taskRepository task repository
     * @param taskSolverRepository task solver repository
     * @param graphRepository graph repository
     * @param labelRepository tag repository
     */
    public TaskApiImpl(TaskRepository taskRepository, TaskSolverRepository taskSolverRepository,
                       GraphRepository graphRepository, LabelRepository labelRepository ) {
        this.taskRepository = taskRepository;
        this.taskSolverRepository = taskSolverRepository;
        this.graphRepository = graphRepository;
        this.labelRepository = labelRepository;
    }

    /**
     * Add feedback to a task.
     *
     * @param taskId ID of task
     * @param feedback feedback to add to task
     * @return Response
     */
    @Override
    public ResponseEntity<Void> addTaskFeedback(Long taskId, Feedback feedback) {
        // get task entity
        Optional<TaskEntity> optTaskEntity = taskRepository.findById(taskId);
        if (optTaskEntity.isPresent()) {
            TaskEntity taskEntity = optTaskEntity.get();

            // add feedback and save
            taskEntity.getFeedbacks().add(new FeedbackTransformer().toEntity(feedback));
            taskRepository.save(taskEntity);

            return ok();
        } else {
            return notFound();
        }
    }

    /**
     * Add a hint to a task.
     * @param taskId ID of task
     * @param taskHint hint to add to task
     * @return Response
     */
    @Override
    public ResponseEntity<Void> addTaskHint(Long taskId, TaskHint taskHint) {
        // get task entity
        Optional<TaskEntity> optTaskEntity = taskRepository.findById(taskId);
        if (optTaskEntity.isPresent()) {
            TaskEntity taskEntity = optTaskEntity.get();

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
    public ResponseEntity<Long> createTask(Task body) {
        TaskEntity taskEntity = taskRepository.save(
                new TaskTransformer(taskSolverRepository, graphRepository, labelRepository)
                        .toEntity(body));

        return ok(taskEntity.getId());
    }

    /**
     * Get a hint for a task.
     *
     * @param taskId ID of task
     * @param hintLevel level of hint
     * @return Response with hint
     */
    @Override
    public ResponseEntity<TaskHint> getHintForTask(Long taskId, Integer hintLevel) {
        // get task entity
        Optional<TaskEntity> optTaskEntity = taskRepository.findById(taskId);
        if (optTaskEntity.isPresent()) {
            // load hints and get hint of specified level
            List<TaskHintEntity> taskHints = optTaskEntity.get().getHints();
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
    public ResponseEntity<List<TaskIdLabelTuple>> getIdsOfAllTasks() {
        // find all tasks and extract IDs
        List<TaskIdLabelTuple> taskIds = taskRepository.findAll().stream()
                .map(taskEntity -> new TaskIdLabelTuple().label(taskEntity.getLabel()).id(taskEntity.getId()))
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
    public ResponseEntity<Task> getTaskById(Long taskId) {
        Optional<TaskEntity> optTaskEntity = taskRepository.findById(taskId);
        if (optTaskEntity.isPresent()) {
            Task task = new TaskTransformer(taskSolverRepository, graphRepository, labelRepository)
                    .toDto(optTaskEntity.get());

            return ok(task);
        } else {
            return notFound();
        }
    }

    /**
     * Get feedback for a task.
     *
     * @param taskId ID of task
     * @return Response with feedback
     */
    @Override
    public ResponseEntity<List<Feedback>> getTaskFeedback(Long taskId) {
        Optional<TaskEntity> optTaskEntity = taskRepository.findById(taskId);
        if (optTaskEntity.isPresent()) {
            TaskEntity taskEntity = optTaskEntity.get();
            List<Feedback> feedbacks = new FeedbackTransformer().toDtoList(taskEntity.getFeedbacks());

            return ok(feedbacks);
        } else {
            return notFound();
        }
    }

    /**
     * Update a task.
     *
     * @param taskId ID of task
     * @param task new task to replace old task with
     * @return Response
     */
    @Override
    public ResponseEntity<Void> updateTask(Long taskId, Task task) {
        Optional<TaskEntity> optTaskEntity = taskRepository.findById(taskId);
        if (optTaskEntity.isPresent()) {
            // create task entity
            TaskEntity taskEntity = new TaskTransformer(taskSolverRepository, graphRepository, labelRepository).toEntity(task);
            taskEntity.setId(taskId);

            // save to database
            taskRepository.save(taskEntity);

            return ok();
        } else {
            return notFound();
        }
    }


}
