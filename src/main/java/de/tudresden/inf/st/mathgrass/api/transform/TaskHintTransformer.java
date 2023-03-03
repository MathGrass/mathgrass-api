package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.hint.TaskHintEntity;
import de.tudresden.inf.st.mathgrass.api.model.TaskHint;

/**
 * This class can convert {@link TaskHint} to {@link TaskHintEntity} and vice versa.
 */
public class TaskHintTransformer extends ModelTransformer<TaskHint, TaskHintEntity> {
    /**
     * {@inheritDoc}
     */
    @Override
    public TaskHint toDto(TaskHintEntity entity) {
        TaskHint dto = new TaskHint();
        dto.setId(entity.getId());
        dto.setLabel(entity.getLabel());
        dto.setContent(entity.getContent());

        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskHintEntity toEntity(TaskHint dto) {
        TaskHintEntity entity = new TaskHintEntity();
        entity.setId(dto.getId());
        entity.setContent(dto.getContent());
        entity.setLabel(dto.getLabel());

        return entity;
    }
}
