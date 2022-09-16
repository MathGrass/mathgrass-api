package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.TagEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskSolverEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskTemplateEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskSolverRepository;
import de.tudresden.inf.st.mathgrassserver.model.Tag;
import de.tudresden.inf.st.mathgrassserver.model.TaskTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class TaskTemplateTransformer extends ModelTransformer<TaskTemplate, TaskTemplateEntity> {


    @Autowired
    TaskSolverRepository taskSolverRepository;

    @Override
    public TaskTemplate toDto(TaskTemplateEntity entity) {
        TaskTemplate dto = new TaskTemplate();
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
        entity.setQuestion(entity.getQuestion());

        //TaskSolver
        TaskSolverEntity solverEntity = taskSolverRepository.getReferenceById(dto.getTaskSolver());
        entity.setTaskSolver(solverEntity);

        entity.setTags(new TagTransformer().toEntityList(dto.getTags()));

        return entity;
    }
}
