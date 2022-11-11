package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.TagEntity;
import de.tudresden.inf.st.mathgrassserver.model.Tag;

/**
 * This class can convert {@link Tag} to {@link TagEntity} and vice versa.
 */
public class TagTransformer extends ModelTransformer<Tag, TagEntity> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Tag toDto(TagEntity entity) {
        Tag tag = new Tag();
        tag.setId(entity.getId());
        tag.setLabel(entity.getLabel());
        return tag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagEntity toEntity(Tag dto) {
        TagEntity tagEntity = new TagEntity();
        tagEntity.setId(dto.getId());
        tagEntity.setLabel(dto.getLabel());
        return tagEntity;
    }
}
