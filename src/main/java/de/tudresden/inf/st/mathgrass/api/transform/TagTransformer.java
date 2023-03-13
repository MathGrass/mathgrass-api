package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.label.LabelEntity;
import de.tudresden.inf.st.mathgrass.api.model.Label;

/**
 * This class can convert {@link Label} to {@link LabelEntity} and vice versa.
 */
public class TagTransformer extends ModelTransformer<Label, LabelEntity> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Label toDto(LabelEntity entity) {
        Label label = new Label();
        label.setId(entity.getId());
        label.setLabel(entity.getLabel());
        return label;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LabelEntity toEntity(Label dto) {
        LabelEntity labelEntity = new LabelEntity();
        labelEntity.setId(dto.getId());
        labelEntity.setLabel(dto.getLabel());
        return labelEntity;
    }
}
