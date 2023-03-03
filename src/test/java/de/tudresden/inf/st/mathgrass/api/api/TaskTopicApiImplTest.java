package de.tudresden.inf.st.mathgrass.api.api;

import de.tudresden.inf.st.mathgrass.api.transform.TaskEntity;
import de.tudresden.inf.st.mathgrass.api.transform.TaskTopicEntity;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import de.tudresden.inf.st.mathgrass.api.task.TaskTopicApiImpl;
import de.tudresden.inf.st.mathgrass.api.task.TaskTopicRepository;
import de.tudresden.inf.st.mathgrass.api.model.TaskTopic;
import de.tudresden.inf.st.mathgrass.api.transform.TaskTopicTransformer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static de.tudresden.inf.st.mathgrass.api.api.TestHelper.getExampleTaskTopic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TaskTopicApiImplTest {

    TestHelper testHelper;

    @Autowired
    TaskTopicApiImpl taskTopicApiImpl;

    @Autowired
    TaskTopicRepository taskTopicRepository;

    @Autowired
    TaskRepository taskRepository;

    @Test
    void createTaskTopic() {
        //create
        TaskTopic taskTopic = getExampleTaskTopic();
        long taskTopicId = taskTopicApiImpl.createTaskTopic(taskTopic).getBody().getId();

        //get
        TaskTopicEntity taskTopicEntity = taskTopicRepository.findById(taskTopicId).orElse(null);

        //check
        assertNotNull(taskTopicEntity);
        assertEquals(taskTopic.getLabel(), taskTopicEntity.getLabel());

        //size
        int counter = 0;
        for (TaskEntity task : taskTopicEntity.getTasks()) {
            counter++;
        }
        assertEquals(taskTopic.getTasks().size(), counter);



    }

    @Test
    void getTaskTopics() {
        //create
        TaskTopic taskTopic1 = getExampleTaskTopic();
        TaskTopic taskTopic2 = getExampleTaskTopic();
        TaskTopicEntity taskTopicEntity1 =new TaskTopicTransformer(taskRepository).toEntity(taskTopic1);
        TaskTopicEntity taskTopicEntity2 =new TaskTopicTransformer(taskRepository).toEntity(taskTopic2);

        taskTopicRepository.save(taskTopicEntity1);
        taskTopicRepository.save(taskTopicEntity2);

        // get
        int counter = 0;
        for (TaskTopic curEntity : taskTopicApiImpl.getTaskTopics().getBody()) {
            //check
            if (Objects.equals(curEntity.getId(), taskTopicEntity1.getId()) || Objects.equals(curEntity.getId(), taskTopicEntity2.getId())) {
                counter++;
            }
        }
        assertEquals(2, counter);

    }
}