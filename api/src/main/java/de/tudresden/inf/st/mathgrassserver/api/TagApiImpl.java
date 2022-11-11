package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TagApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TagEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TagRepository;
import de.tudresden.inf.st.mathgrassserver.model.Tag;
import de.tudresden.inf.st.mathgrassserver.transform.TagTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * This class can create and load {@link Tag}s.
 */
@RestController
public class TagApiImpl extends AbstractApiElement implements TagApi {
    /**
     * Tag repository.
     */
    final TagRepository tagRepository;

    /**
     * Constructor.
     *
     * @param tagRepository tag repository
     */
    public TagApiImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Create a tag and save to database.
     *
     * @param tag tag instance to save
     * @return Response containing tag
     */
    @Override
    public ResponseEntity<Tag> createTag(Tag tag) {
        TagEntity tagEntity = tagRepository.save(new TagTransformer().toEntity(tag));
        tag.setId(tagEntity.getId());

        return ok(tag);
    }

    /**
     * Get a Tag from the database from specified ID.
     *
     * @param id ID of tag to get
     * @return Response containing tag
     */
    @Override
    public ResponseEntity<Tag> getTagById(Long id) {
        // check if tag exists
        Optional<TagEntity> optTag = tagRepository.findById(id);

        if (optTag.isPresent()) {
            Tag tag = new TagTransformer().toDto(optTag.get());
            return ok(tag);
        } else {
            return notFound();
        }
    }

    /**
     * Get all tags available in the database.
     *
     * @return Response with list of tags
     */
    @Override
    public ResponseEntity<List<Tag>> getTags() {
        return ok(new TagTransformer().toDtoList(tagRepository.findAll()));
    }
}
