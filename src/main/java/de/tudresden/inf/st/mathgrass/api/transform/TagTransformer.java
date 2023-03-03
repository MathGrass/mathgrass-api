package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.label.LabelEntity;
import de.tudresden.inf.st.mathgrass.api.model.LabelDTO;

/**
 * This class can convert {@link LabelDTO} to {@link LabelEntity} and vice versa.
 */
public class TagTransformer extends ModelTransformer<LabelDTO, LabelEntity> {
    /**
     * {@inheritDoc}
     */
    @Override
    public LabelDTO toDto(LabelEntity entity) {
        LabelDTO label = new LabelDTO();
        label.setId(entity.getId());
        label.setLabel(entity.getValue());
        return label;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LabelEntity toEntity(LabelDTO dto) {
        LabelEntity labelEntity = new LabelEntity();
        labelEntity.setId(dto.getId());
        labelEntity.setValue(dto.getLabel());
        return labelEntity;
    }
}
