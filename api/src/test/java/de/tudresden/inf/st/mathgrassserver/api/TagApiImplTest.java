package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.database.entity.TagEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TagRepository;
import de.tudresden.inf.st.mathgrassserver.model.Tag;
import de.tudresden.inf.st.mathgrassserver.transform.TagTransformer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TagApiImplTest {

    @Autowired
    TagApiImpl tagApiImpl;

    @Autowired
    TagRepository tagRepository;

    @Test
    void createTag() {
        //create
        Tag tag = TestHelper.createTag();

        //get
        long tagId = tagApiImpl.createTag(tag).getBody().getId();
        TagEntity newEntity = tagRepository.findById(tagId).orElse(null);


        //check
        assertNotNull(newEntity);
        assertEquals(tag.getLabel(), newEntity.getLabel());

    }

    @Test
    void getTagById() {
        //create
        Tag tag = TestHelper.createTag();
        long tagId = tagRepository.save(new TagTransformer().toEntity(tag)).getId();

        //get
        Tag gotTag = tagApiImpl.getTagById(tagId).getBody();

        //check
        assertNotNull(gotTag);
        assertEquals(tag.getLabel(), gotTag.getLabel());

    }

    @Test
    void getTags() {
        //create
        Tag tag1 = TestHelper.createTag();
        Tag tag2 = TestHelper.createTag();

        long tag1Id = tagRepository.save(new TagTransformer().toEntity(tag1)).getId();
        long tag2Id = tagRepository.save(new TagTransformer().toEntity(tag2)).getId();


        //get
        List<Tag> tags = tagApiImpl.getTags().getBody();


        //check
        assertNotNull(tags);
        int foundMatchingTags = 0;
        for (Tag tag : tags) {
            if (tag.getId()==tag1Id) {
                assertEquals(tag1.getLabel(), tag.getLabel());
                foundMatchingTags++;
            } else if (tag.getId()==tag2Id) {
                assertEquals(tag2.getLabel(), tag.getLabel());
                foundMatchingTags++;
            }
        }

        if (foundMatchingTags!=2) {
            fail("Not all tags were found");
        }



    }
}