package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.TaskSolverEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskTemplateEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskSolverRepository;
import de.tudresden.inf.st.mathgrassserver.model.TaskTemplate;

public class TaskTemplateTransformer extends ModelTransformer<TaskTemplate, TaskTemplateEntity> {


    TaskSolverRepository taskSolverRepository;

    public TaskTemplateTransformer(TaskSolverRepository taskSolverRepository) {
        this.taskSolverRepository = taskSolverRepository;
    }

    @Override
    public TaskTemplate toDto(TaskTemplateEntity entity) {
        TaskTemplate dto = new TaskTemplate();
        if(entity == null){
            return dto;
        }

        dto.setId(entity.getId());

        dto.setLabel(entity.getLabel());

        //TODO: check role
        if (getUsedRole()!=null) {
            dto.setHints(new TaskHintTransformer().toDtoList(entity.getHints()));
        }

        dto.setQuestion(entity.getQuestion());
        dto.setTaskSolver(entity.getTaskSolver().getId());

        //Tags
        dto.setTags(new TagTransformer().toDtoList(entity.getTags()));

        return dto;
    }

    @Override
    public TaskTemplateEntity toEntity(TaskTemplate dto) {
        TaskTemplateEntity entity = new TaskTemplateEntity();
        entity.setId(dto.getId());

        entity.setLabel(dto.getLabel());
        entity.setHints(new TaskHintTransformer().toEntityList(dto.getHints()));
        entity.setQuestion(dto.getQuestion());

        //TaskSolver
        TaskSolverEntity solverEntity = taskSolverRepository.findById(dto.getTaskSolver()).get();
        entity.setTaskSolver(solverEntity);

        entity.setTags(new TagTransformer().toEntityList(dto.getTags()));

        return entity;
    }
}
