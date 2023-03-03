package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.database.entity.TaskHintEntity;
import de.tudresden.inf.st.mathgrass.api.database.entity.TaskSolverEntity;
import de.tudresden.inf.st.mathgrass.api.database.entity.TaskTemplateEntity;
import de.tudresden.inf.st.mathgrass.api.database.repository.TaskSolverRepository;
import de.tudresden.inf.st.mathgrass.api.model.TaskHint;
import de.tudresden.inf.st.mathgrass.api.model.TaskTemplate;

import java.util.List;
import java.util.Optional;

/**
 * This class can convert {@link TaskTemplate} to {@link TaskTemplateEntity} and vice versa.
 */
public class TaskTemplateTransformer extends ModelTransformer<TaskTemplate, TaskTemplateEntity> {
    /**
     * Task solver repository.
     */
    TaskSolverRepository taskSolverRepository;

    /**
     * Constructor.
     *
     * @param taskSolverRepository task solver repository
     */
    public TaskTemplateTransformer(TaskSolverRepository taskSolverRepository) {
        this.taskSolverRepository = taskSolverRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskTemplate toDto(TaskTemplateEntity entity) {
        TaskTemplate dto = new TaskTemplate();
        if (entity == null){
            return dto;
        }

        dto.setId(entity.getId());
        dto.setLabel(entity.getLabel());

        // TODO: check role
        if (getUsedRole() != null) {
            List<TaskHintEntity> hints = entity.getHints();
            List<TaskHint> taskHints = new TaskHintTransformer().toDtoList(hints);
            dto.setHints(taskHints);
        }

        dto.setQuestion(entity.getQuestion());
        dto.setTaskSolver(entity.getTaskSolver().getId());

        // tags
        dto.setLabels(new TagTransformer().toDtoList(entity.getTags()));

        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskTemplateEntity toEntity(TaskTemplate dto) {
        // task solver
        Optional<TaskSolverEntity> optSolverEntity = taskSolverRepository.findById(dto.getTaskSolver());
        if (optSolverEntity.isPresent()) {
            TaskTemplateEntity entity = new TaskTemplateEntity();
            entity.setId(dto.getId());

            entity.setLabel(dto.getLabel());
            entity.setHints(new TaskHintTransformer().toEntityList(dto.getHints()));
            entity.setQuestion(dto.getQuestion());
            entity.setTaskSolver(optSolverEntity.get());
            entity.setTags(new TagTransformer().toEntityList(dto.getLabels()));

            return entity;
        } else {
            throw new IllegalArgumentException("Couldn't find Task Solver with ID " + dto.getTaskSolver());
        }
    }
}
