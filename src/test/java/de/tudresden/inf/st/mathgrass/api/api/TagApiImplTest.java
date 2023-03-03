package de.tudresden.inf.st.mathgrass.api.api;

import de.tudresden.inf.st.mathgrass.api.label.LabelApiImpl;
import de.tudresden.inf.st.mathgrass.api.label.LabelEntity;
import de.tudresden.inf.st.mathgrass.api.label.LabelRepository;
import de.tudresden.inf.st.mathgrass.api.model.Label;
import de.tudresden.inf.st.mathgrass.api.transform.TagTransformer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TagApiImplTest {

    @Autowired
    LabelApiImpl labelApiImpl;

    @Autowired
    LabelRepository labelRepository;

    @Test
    void createLabel() {
        //create
        Label label = TestHelper.createLabel();

        //get
        long labelId = labelApiImpl.createLabel(label).getBody().getId();
        LabelEntity newEntity = labelRepository.findById(labelId).orElse(null);


        //check
        assertNotNull(newEntity);
        assertEquals(label.getLabel(), newEntity.getLabel());

    }

    @Test
    void getTagById() {
        //create
        Label label = TestHelper.createLabel();
        long labelId = labelRepository.save(new TagTransformer().toEntity(label)).getId();

        //get
        Label gotLabel = labelApiImpl.getLabelById(labelId).getBody();

        //check
        assertNotNull(gotLabel);
        assertEquals(label.getLabel(), gotLabel.getLabel());

    }

    @Test
    void getTags() {
        //create
        Label label1 = TestHelper.createLabel();
        Label label2 = TestHelper.createLabel();

        long tag1Id = labelRepository.save(new TagTransformer().toEntity(label1)).getId();
        long tag2Id = labelRepository.save(new TagTransformer().toEntity(label2)).getId();


        //get
        List<Label> tags = labelApiImpl.getLabels().getBody();


        //check
        assertNotNull(tags);
        int foundMatchingTags = 0;
        for (Label tag : tags) {
            if (tag.getId()==tag1Id) {
                assertEquals(label1.getLabel(), tag.getLabel());
                foundMatchingTags++;
            } else if (tag.getId()==tag2Id) {
                assertEquals(label2.getLabel(), tag.getLabel());
                foundMatchingTags++;
            }
        }

        if (foundMatchingTags!=2) {
            fail("Not all tags were found");
        }



    }
}
