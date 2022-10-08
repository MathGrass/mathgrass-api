package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.GraphEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TagEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TagRepository;
import de.tudresden.inf.st.mathgrassserver.model.Graph;
import de.tudresden.inf.st.mathgrassserver.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;

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
