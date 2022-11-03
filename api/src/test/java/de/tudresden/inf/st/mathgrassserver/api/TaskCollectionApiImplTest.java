package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.database.entity.TaskCollectionEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.*;
import de.tudresden.inf.st.mathgrassserver.model.Task;
import de.tudresden.inf.st.mathgrassserver.model.TaskCollection;
import de.tudresden.inf.st.mathgrassserver.transform.TaskCollectionTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskCollectionApiImplTest {

    @Autowired
    TaskCollectionRepository taskCollectionRepository;

    @Autowired
    TaskCollectionApiImpl taskCollectionApiImpl;

    @Autowired
    GraphRepository graphRepository;

    TestHelper testHelper;


    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskSolverRepository taskSolverRepository;

    @Autowired
    TaskTemplateRepository taskTemplateRepository;

    @BeforeEach
    void setUp() {
        testHelper = new TestHelper().setTaskRepository(taskRepository)
                .setGraphRepository(graphRepository)
                .setTaskSolverRepository(taskSolverRepository)
                .setTaskTemplateRepository(taskTemplateRepository);
    }

    @Test
    void createTaskCollection() {
        //create
        TaskCollection taskCollection = testHelper.prepareTaskCollection();
        long taskCollectionId = taskCollectionApiImpl.createTaskCollection(taskCollection).getBody().getId();

        //get
        TaskCollectionEntity taskCollectionEntity = taskCollectionRepository.findById(taskCollectionId).orElse(null);

        //check
        assertNotNull(taskCollectionEntity);
        assertEquals(taskCollection.getLabel(), taskCollectionEntity.getLabel());
        assertEquals(taskCollection.getTasks().size(), taskCollectionEntity.getTasks().size());


    }

    @Test
    void getTaskCollectionById() {
        //create
        TaskCollection taskCollectionOrig = testHelper.prepareTaskCollection();
        TaskCollectionEntity entity = new TaskCollectionTransformer(taskRepository).toEntity(taskCollectionOrig);
        long taskCollectionId = taskCollectionRepository.save(entity).getId();

        //get
        TaskCollection taskCollection = taskCollectionApiImpl.getTaskCollectionById(taskCollectionId).getBody();

        //check
        assertNotNull(taskCollection);
        assertEquals(taskCollectionOrig.getLabel(), taskCollection.getLabel());

        //check size
        int counter = 0;
        for (long id : taskCollection.getTasks()) {
            counter++;
        }
        assertEquals(taskCollectionOrig.getTasks().size(), counter);
    }

    @Test
    void getTaskCollections() {
        //create
        TaskCollection taskCollectionOrig1 = testHelper.prepareTaskCollection();
        TaskCollection taskCollectionOrig2 = testHelper.prepareTaskCollection();
        TaskCollectionEntity entity1 = new TaskCollectionTransformer(taskRepository).toEntity(taskCollectionOrig1);
        TaskCollectionEntity entity2 = new TaskCollectionTransformer(taskRepository).toEntity(taskCollectionOrig2);

        taskCollectionRepository.save(entity1);
        taskCollectionRepository.save(entity2);

        //get
        List<TaskCollection> taskCollections = taskCollectionApiImpl.getTaskCollections().getBody();

        //check
        assertNotNull(taskCollections);

        //check size
        int counter = 0;
        for (TaskCollection taskCollection : taskCollections) {
            if (taskCollection.getLabel().equals(taskCollectionOrig1.getLabel())) {
                counter++;
            }
            else if (taskCollection.getLabel().equals(taskCollectionOrig2.getLabel())) {
                counter++;
            }
        }
        assertEquals(2, counter);
    }
}