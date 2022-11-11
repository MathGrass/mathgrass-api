package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TaskTemplateApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskHintEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskTemplateEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskSolverRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskTemplateRepository;
import de.tudresden.inf.st.mathgrassserver.model.TaskHint;
import de.tudresden.inf.st.mathgrassserver.model.TaskTemplate;
import de.tudresden.inf.st.mathgrassserver.transform.TaskHintTransformer;
import de.tudresden.inf.st.mathgrassserver.transform.TaskTemplateTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * This class contains functionality to manage {@link TaskTemplate}s.
 */
@RestController
public class TaskTemplateApiImpl extends AbstractApiElement implements TaskTemplateApi {
    /**
     * Task template repository.
     */
    final TaskTemplateRepository taskTemplateRepository;

    /**
     * Task solver repository.
     */
    final TaskSolverRepository taskSolverRepository;

    /**
     * Constructor.
     *
     * @param taskTemplateRepository task template repository
     * @param taskSolverRepository task solver repository
     */
    public TaskTemplateApiImpl(TaskTemplateRepository taskTemplateRepository, TaskSolverRepository taskSolverRepository) {
        this.taskTemplateRepository = taskTemplateRepository;
        this.taskSolverRepository = taskSolverRepository;
    }

    /**
     * Create a task template.
     * @param body task template to create
     * @return Response with ID of task template
     */
    @Override
    public ResponseEntity<Long> createTaskTemplate(TaskTemplate body) {
        TaskTemplateEntity entity = taskTemplateRepository.save(
                new TaskTemplateTransformer(taskSolverRepository).toEntity(body));
        return ok(entity.getId());
    }

    /**
     * Get a task template by its ID.
     *
     * @param id ID of task template
     * @return Response with task template
     */
    @Override
    public ResponseEntity<TaskTemplate> getTaskTemplateById(Long id) {
        Optional<TaskTemplateEntity> optTaskTemplateEntity = taskTemplateRepository.findById(id);
        if (optTaskTemplateEntity.isPresent()) {
            TaskTemplate taskTemplate = new TaskTemplateTransformer(taskSolverRepository)
                    .toDto(optTaskTemplateEntity.get());

            return ok(taskTemplate);
        } else {
            return notFound();
        }
    }

    /**
     * Get a hint for a task template.
     *
     * @param taskTemplateId ID of task template
     * @param hintIndex index of hint to get
     * @return Response with task hint
     */
    @Override
    public ResponseEntity<TaskHint> getTaskTemplateHint(Long taskTemplateId, Integer hintIndex) {
        if (hintIndex < 0) {
            illegalArgs();
        }

        // get task template
        checkExistence(taskTemplateId, taskTemplateRepository);
        TaskTemplateEntity entity = new TaskTemplateEntity();

        // TODO: remember that student asked for hint
        // get hints
        List<TaskHintEntity> hints = entity.getHints();
        if (hints == null || hints.size() < hintIndex+1) {
            return notFound();
        } else {
            TaskHintEntity hintEntity = hints.get(hintIndex);
            TaskHint hintDto = new TaskHintTransformer().toDto(hintEntity);

            return ok(hintDto);
        }
    }

    /**
     * Set label for a task template.
     *
     * @param taskTemplateId ID of task template
     * @param label label to set
     * @return Response
     */
    @Override
    public ResponseEntity<Void> setTaskTemplateLabel(Long taskTemplateId, String label) {
        Optional<TaskTemplateEntity> optTaskTemplate = taskTemplateRepository.findById(taskTemplateId);
        if (optTaskTemplate.isPresent()) {
            TaskTemplateEntity taskTemplate = optTaskTemplate.get();
            taskTemplate.setLabel(label);
            taskTemplateRepository.save(taskTemplate);

            return ok();
        } else {
            return notFound();
        }
    }

    /**
     * Set question for a task template.
     *
     * @param taskTemplateId ID of task template
     * @param question question to set
     * @return Response
     */
    @Override
    public ResponseEntity<Void> setTaskTemplateQuestion(Long taskTemplateId, String question) {
        Optional<TaskTemplateEntity> optTaskTemplate = taskTemplateRepository.findById(taskTemplateId);
        if (optTaskTemplate.isPresent()) {
            TaskTemplateEntity taskTemplate = optTaskTemplate.get();
            taskTemplate.setQuestion(question);
            taskTemplateRepository.save(taskTemplate);

            return ok();
        } else {
            return notFound();
        }
    }

}
