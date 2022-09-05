package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TagApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TagEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TagRepository;
import de.tudresden.inf.st.mathgrassserver.model.Tag;
import de.tudresden.inf.st.mathgrassserver.transform.TagTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagApiImpl extends AbsApi implements TagApi {

    @Autowired
    TagRepository tagRepository;

    @Override
    public ResponseEntity<Void> createTag(Tag tag) {
        TagEntity tagEntity = new TagTransformer().toEntity(tag);
        tagRepository.save(tagEntity);
        return ok();

    }

    @Override
    public ResponseEntity<Tag> getTagById(Long id) {
        checkExistence(id,tagRepository);

        Tag tag = new TagTransformer().toDto(tagRepository.getReferenceById(id));
        return ok(tag);
    }
}
