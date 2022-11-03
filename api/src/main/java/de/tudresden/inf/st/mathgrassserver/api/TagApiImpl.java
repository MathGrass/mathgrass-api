package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TagApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TagEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TagRepository;
import de.tudresden.inf.st.mathgrassserver.model.Tag;
import de.tudresden.inf.st.mathgrassserver.transform.TagTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagApiImpl extends AbstractApiElement implements TagApi {

    final TagRepository tagRepository;

    public TagApiImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public ResponseEntity<Tag> createTag(Tag tag) {
        TagEntity tagEntity = tagRepository.save(new TagTransformer().toEntity(tag));
        tag.setId(tagEntity.getId());
        return ok(tag);

    }

    @Override
    public ResponseEntity<Tag> getTagById(Long id) {
        checkExistence(id,tagRepository);

        Tag tag = new TagTransformer().toDto(tagRepository.findById(id).get());
        return ok(tag);
    }

    @Override
    public ResponseEntity<List<Tag>> getTags() {
        return ok(new TagTransformer().toDtoList(tagRepository.findAll()));
    }
}
