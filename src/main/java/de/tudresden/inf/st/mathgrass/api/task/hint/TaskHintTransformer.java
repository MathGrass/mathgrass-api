package de.tudresden.inf.st.mathgrass.api.task.hint;

import de.tudresden.inf.st.mathgrass.api.model.HintDTO;
import de.tudresden.inf.st.mathgrass.api.transform.ModelTransformer;

/**
 * This class can convert {@link HintDTO} to {@link Hint} and vice versa.
 */
public class TaskHintTransformer extends ModelTransformer<HintDTO, Hint> {
    /**
     * {@inheritDoc}
     */
    @Override
    public HintDTO toDto(Hint entity) {
        HintDTO dto = new HintDTO();
        dto.setId(entity.getId());
        dto.setLabel(entity.getLabel());
        dto.setContent(entity.getContent());

        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Hint toEntity(HintDTO dto) {
        Hint entity = new Hint();
        entity.setId(dto.getId());
        entity.setContent(dto.getContent());
        entity.setLabel(dto.getLabel());

        return entity;
    }
}
