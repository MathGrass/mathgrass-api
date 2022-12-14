package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.TaskSolverEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskTemplateEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskSolverRepository;
import de.tudresden.inf.st.mathgrassserver.model.TaskTemplate;

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
            dto.setHints(new TaskHintTransformer().toDtoList(entity.getHints()));
        }

        dto.setQuestion(entity.getQuestion());
        dto.setTaskSolver(entity.getTaskSolver().getId());

        // tags
        dto.setTags(new TagTransformer().toDtoList(entity.getTags()));

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
            entity.setTags(new TagTransformer().toEntityList(dto.getTags()));

            return entity;
        } else {
            throw new IllegalArgumentException("Couldn't find Task Solver with ID " + dto.getTaskSolver());
        }
    }
}
