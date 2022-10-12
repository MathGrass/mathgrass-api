package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.TagEntity;
import de.tudresden.inf.st.mathgrassserver.model.Tag;

public class TagTransformer extends ModelTransformer<Tag, TagEntity> {


    @Override
    public Tag toDto(TagEntity entity) {
        Tag tag = new Tag();
        tag.setId(entity.getId());
        tag.setLabel(entity.getLabel());
        return tag;
    }

    @Override
    public TagEntity toEntity(Tag dto) {
        TagEntity tagEntity = new TagEntity();
        tagEntity.setId(dto.getId());
        tagEntity.setLabel(dto.getLabel());
        return tagEntity;
    }
}
