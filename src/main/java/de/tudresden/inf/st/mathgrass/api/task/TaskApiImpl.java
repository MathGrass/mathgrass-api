package de.tudresden.inf.st.mathgrass.api.task;

import de.tudresden.inf.st.mathgrass.api.common.AbstractApiElement;
import de.tudresden.inf.st.mathgrass.api.label.LabelRepository;
import de.tudresden.inf.st.mathgrass.api.evaluator.solver.TaskSolverRepository;
import de.tudresden.inf.st.mathgrass.api.graph.GraphRepository;
import de.tudresden.inf.st.mathgrass.api.model.*;
import de.tudresden.inf.st.mathgrass.api.task.hint.Hint;
import de.tudresden.inf.st.mathgrass.api.apiModel.TaskApi;
import de.tudresden.inf.st.mathgrass.api.task.hint.TaskHintTransformer;
import de.tudresden.inf.st.mathgrass.api.task.question.QuestionVisitor;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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
     * @param taskRepository       task repository
     * @param taskSolverRepository task solver repository
     * @param graphRepository      graph repository
     * @param labelRepository      tag repository
     */
    public TaskApiImpl(TaskRepository taskRepository,
                       TaskSolverRepository taskSolverRepository,
                       GraphRepository graphRepository,
                       LabelRepository labelRepository) {
        this.taskRepository = taskRepository;
        this.taskSolverRepository = taskSolverRepository;
        this.graphRepository = graphRepository;
        this.labelRepository = labelRepository;
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
        Task taskEntity = taskRepository.save(
                new TaskTransformer(taskSolverRepository, graphRepository,
                        labelRepository)
                        .toEntity(body));

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
            TaskDTO task = new TaskTransformer(taskSolverRepository,
                    graphRepository, labelRepository)
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
            Task taskEntity = new TaskTransformer(taskSolverRepository,
                    graphRepository, labelRepository).toEntity(task);
            taskEntity.setId(taskId);

            // save to database
            taskRepository.save(taskEntity);

            return ok();
        } else {
            return notFound();
        }
    }

    @Override
    public ResponseEntity<EvaluateAnswer200Response> evaluateAnswer(
            @Parameter(name = "taskId", description = "ID of task", required
                    = true) @PathVariable("taskId") Long taskId,
            @Parameter(name = "EvaluateAnswerRequest", description =
                    "Submitted answer", required = true) @Valid @RequestBody EvaluateAnswerRequest evaluateAnswerRequest
    ) {
        Optional<Task> optTask = taskRepository.findById(taskId);
        if (optTask.isPresent()) {
            String userAnswer = evaluateAnswerRequest.getAnswer();
            Task task = optTask.get();
            QuestionVisitor questionVisitor = new QuestionVisitor();
            boolean result = task.getQuestion().acceptQuestionVisitor(questionVisitor, userAnswer);

            return ok(new EvaluateAnswer200Response().isAssessmentCorrect(result));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
