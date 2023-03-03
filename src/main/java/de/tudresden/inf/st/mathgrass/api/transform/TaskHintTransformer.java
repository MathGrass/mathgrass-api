package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.task.hint.Hint;
import de.tudresden.inf.st.mathgrass.api.model.TaskHint;

/**
 * This class can convert {@link TaskHint} to {@link Hint} and vice versa.
 */
public class TaskHintTransformer extends ModelTransformer<TaskHint, Hint> {
    /**
     * {@inheritDoc}
     */
    @Override
    public TaskHint toDto(Hint entity) {
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
    public Hint toEntity(TaskHint dto) {
        Hint entity = new Hint();
        entity.setId(dto.getId());
        entity.setContent(dto.getContent());
        entity.setLabel(dto.getLabel());

        return entity;
    }
}
