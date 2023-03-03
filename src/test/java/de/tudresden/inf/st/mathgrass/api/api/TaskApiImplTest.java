package de.tudresden.inf.st.mathgrass.api.api;

import de.tudresden.inf.st.mathgrass.api.database.entity.FeedbackEntity;
import de.tudresden.inf.st.mathgrass.api.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrass.api.database.repository.*;
import de.tudresden.inf.st.mathgrass.api.database.repository.*;
import de.tudresden.inf.st.mathgrass.api.model.Feedback;
import de.tudresden.inf.st.mathgrass.api.model.Question;
import de.tudresden.inf.st.mathgrass.api.model.Task;
import de.tudresden.inf.st.mathgrass.api.model.TaskHint;
import de.tudresden.inf.st.mathgrass.api.transform.TaskTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TaskApiImplTest {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    GraphRepository graphRepository;

    @Autowired
    TaskTemplateRepository taskTemplateRepository;

    @Autowired
    TaskSolverRepository taskSolverRepository;

    @Autowired
    LabelRepository labelRepository;

    @Autowired
    TaskApiImpl taskApiImpl;

    TestHelper testHelper;

    @BeforeEach
    void setUp() {
        assertThat(taskApiImpl).isNotNull();
        testHelper = new TestHelper().setGraphRepository(graphRepository)
                .setTaskRepository(taskRepository)
                .setTaskSolverRepository(taskSolverRepository)
                .setTaskTemplateRepository(taskTemplateRepository)
                .setTagRepository(labelRepository);
    }

    @Test
    void addTaskFeedback() {

    }

    @Test
    void addTaskHint() {
    }


    @Test
    void createStaticTask() {

        Task task = this.testHelper.prepareExampleStaticTask();



        //create one
        long taskId = taskApiImpl.createTask(task).getBody();

        //get one that is there and look into database
        TaskEntity taskEntity1 = taskRepository.findById(taskId).get();

        //check out
        assertEquals(task.getLabel(),taskEntity1.getLabel());
        assertEquals(task.getQuestion(),taskEntity1.getQuestion());
        //TODO: check more
    }

    @Test
    void createDynamicTask() {


        Task task = testHelper.prepareExampleDynamicTask();


        //create one
        long taskId = taskApiImpl.createTask(task).getBody();

        //get one that is there and look into database
        TaskEntity taskEntity2 = taskRepository.findById(taskId).get();

        //check out
        assertEquals(task.getLabel(),taskEntity2.getLabel());
        assertEquals(task.getQuestion(),taskEntity2.getQuestion());
        //TODO: check more


    }

    @Test
    void getHintForTask() {
        String label = "label1";
        String content = "hint to solution";
        Task staticTask = this.testHelper.prepareExampleStaticTask();
        TaskHint taskHint = new TaskHint();
        taskHint.setLabel(label);
        taskHint.setContent(content);
        staticTask.setHints(Arrays.asList(taskHint));
        TaskEntity entity = taskRepository.save(new TaskTransformer(taskSolverRepository,graphRepository, labelRepository,taskTemplateRepository).toEntity(staticTask));

        ResponseEntity<TaskHint> response = taskApiImpl.getHintForTask(entity.getId(),0);
        assertEquals(response.getStatusCodeValue(),200);
        assertEquals(response.getBody().getContent(),content);
        assertEquals(response.getBody().getLabel(),label);

    }

    @Test
    void getIdsOfAllTasks() {
    }

    @Test
    void getTaskById() {
        //get one that is not there
    }

    @Test
    void getTaskFeedback() {
        Task task = this.testHelper.prepareExampleStaticTask();
        FeedbackEntity f1 = new FeedbackEntity();
        f1.setContent("feedback1");

        FeedbackEntity f2 = new FeedbackEntity();
        f2.setContent("feedback2");


        List<FeedbackEntity> feedbacks = Arrays.asList(f1,f2);
        TaskEntity entity = new TaskTransformer(taskSolverRepository,graphRepository, labelRepository,taskTemplateRepository).toEntity(task);
        entity.setFeedbacks(feedbacks);
        taskRepository.save(entity);

        ResponseEntity<List<Feedback>> response = taskApiImpl.getTaskFeedback(entity.getId());
        for (int i = 0; i < response.getBody().size(); i++) {
            assertEquals(response.getBody().get(i).getContent(),feedbacks.get(i).getContent());
        }


    }

    @Test
    void updateTask() {
        String newQuestion = "new question?";
        String newLabel = "new label";
        Task task = this.testHelper.prepareExampleDynamicTask();
        TaskEntity entity = taskRepository.save(new TaskTransformer(taskSolverRepository,graphRepository, labelRepository,taskTemplateRepository).toEntity(task));
        task.setQuestion(new Question().question(newQuestion));
        task.setLabel(newLabel);

        //trigger
        taskApiImpl.updateTask(entity.getId(),task);

        //check
        entity = taskRepository.save(new TaskTransformer(taskSolverRepository,graphRepository, labelRepository,taskTemplateRepository).toEntity(task));
        assertEquals(entity.getQuestion(),newQuestion);
        assertEquals(entity.getLabel(),newLabel);
    }
}
