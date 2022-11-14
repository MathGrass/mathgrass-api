package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.database.entity.TaskHintEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskTemplateEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskSolverRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskTemplateRepository;
import de.tudresden.inf.st.mathgrassserver.model.TaskHint;
import de.tudresden.inf.st.mathgrassserver.model.TaskTemplate;
import de.tudresden.inf.st.mathgrassserver.transform.TaskTemplateTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TaskTemplateApiImplTest {

    @Autowired
    TaskTemplateApiImpl taskTemplateApiImpl;

    @Autowired
    TaskSolverRepository taskSolverRepository;

    @Autowired
    TaskTemplateRepository taskTemplateRepository;

    TestHelper testHelper;

    @BeforeEach
    void setUp() {
        testHelper = new TestHelper().setTaskSolverRepository(taskSolverRepository);
    }

    @Test
    void createTaskTemplate() {
        // create
        TaskTemplate taskTemplate = testHelper.prepareTaskTemplate();
        long taskTemplateId = taskTemplateApiImpl.createTaskTemplate(taskTemplate).getBody();

        // get
        TaskTemplateEntity taskTemplateEntity = taskTemplateRepository.findById(taskTemplateId).orElse(null);

        //check
        assertNotNull(taskTemplateEntity);
        assertEquals(taskTemplate.getLabel(), taskTemplateEntity.getLabel());

        //TODO: check task solver
    }

    @Test
    void getTaskTemplateById() {
        // create
        TaskTemplate taskTemplateOrig = testHelper.prepareTaskTemplate();
        long taskTemplateId = taskTemplateRepository.save(new TaskTemplateTransformer(taskSolverRepository).toEntity(taskTemplateOrig)).getId();

        // get
        TaskTemplate taskTemplate = taskTemplateApiImpl.getTaskTemplateById(taskTemplateId).getBody();

        //check
        assertNotNull(taskTemplateOrig);
        assertEquals(taskTemplate.getLabel(), taskTemplateOrig.getLabel());
    }

    @Test
    void getTaskTemplateHint() {
        // create
        TaskTemplate taskTemplateOrig = testHelper.prepareTaskTemplate();
        TaskTemplateEntity entity = new TaskTemplateTransformer(taskSolverRepository).toEntity(taskTemplateOrig);

        TaskHintEntity taskHintEntity = new TaskHintEntity();
        taskHintEntity.setLabel("hint 1");
        taskHintEntity.setContent("this is the content of hint 1");

        entity.getHints().add(taskHintEntity);

        long taskTemplateId = taskTemplateRepository.save(entity).getId();

        // get
        TaskHint hint = taskTemplateApiImpl.getTaskTemplateHint(taskTemplateId,0).getBody();

        //check
        assertNotNull(hint);
        assertEquals( taskTemplateOrig.getHints().get(0).getLabel(),hint.getLabel());
        assertEquals( taskTemplateOrig.getHints().get(0).getContent(),hint.getContent());
    }

    @Test
    void setTaskTemplateLabel() {
        // create
        TaskTemplate taskTemplateOrig = testHelper.prepareTaskTemplate();
        long taskTemplateId = taskTemplateRepository.save(new TaskTemplateTransformer(taskSolverRepository).toEntity(taskTemplateOrig)).getId();

        // set
        String newLabel = "new label";
        taskTemplateApiImpl.setTaskTemplateLabel(taskTemplateId, newLabel);

        // get
        TaskTemplate taskTemplate = taskTemplateApiImpl.getTaskTemplateById(taskTemplateId).getBody();

        //check
        assertNotNull(taskTemplate);
        assertEquals(taskTemplate.getLabel(), newLabel);
    }

    @Test
    void setTaskTemplateQuestion() {
        // create
        TaskTemplate taskTemplateOrig = testHelper.prepareTaskTemplate();
        long taskTemplateId = taskTemplateRepository.save(new TaskTemplateTransformer(taskSolverRepository).toEntity(taskTemplateOrig)).getId();

        // set
        String newQuestion = "new question??";
        taskTemplateApiImpl.setTaskTemplateQuestion(taskTemplateId, newQuestion);

        // get
        TaskTemplate taskTemplate = taskTemplateApiImpl.getTaskTemplateById(taskTemplateId).getBody();

        //check
        assertNotNull(taskTemplate);
        assertEquals(taskTemplate.getQuestion(), newQuestion);
    }
}