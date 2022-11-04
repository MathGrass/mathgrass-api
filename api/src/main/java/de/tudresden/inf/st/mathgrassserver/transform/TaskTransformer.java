package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.*;
import de.tudresden.inf.st.mathgrassserver.database.repository.GraphRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TagRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskSolverRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskTemplateRepository;
import de.tudresden.inf.st.mathgrassserver.model.Graph;
import de.tudresden.inf.st.mathgrassserver.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskTransformer extends ModelTransformer<Task, TaskEntity> {

    TaskSolverRepository taskSolverRepository;
    GraphRepository graphRepository;
    TagRepository tagRepository;
    TaskTemplateRepository taskTemplateRepository;


    public TaskTransformer(TaskSolverRepository taskSolverRepository, GraphRepository graphRepository, TagRepository tagRepository, TaskTemplateRepository taskTemplateRepository) {
        this.taskSolverRepository = taskSolverRepository;
        this.graphRepository = graphRepository;
        this.tagRepository = tagRepository;
        this.taskTemplateRepository = taskTemplateRepository;
    }

    @Override
    public Task toDto(TaskEntity entity) {

        Task dto = new Task();
        dto.setId(entity.getId());

        dto.setQuestion(entity.getQuestion());

        dto.setLabel(entity.getLabel());

        // answer should not be sent to student
        if (getUsedRole()!=null) {
            dto.setAnswer(entity.getAnswer());
        }

        ArrayList<Long> feedbackIds = new ArrayList<>();
        for (FeedbackEntity feedbackEntity : entity.getFeedbacks() ) {
            feedbackIds.add(feedbackEntity.getId());
        }
        dto.setFeedback(feedbackIds);

        Graph graph = new GraphTransformer(tagRepository).toDto(entity.getGraph());
        dto.setGraph(graph);

        // hints
        if (getUsedRole()!=null) {
            dto.setHints(new TaskHintTransformer().toDtoList(entity.getHints()));
        }

        //template
        dto.setTemplate(new TaskTemplateTransformer(taskSolverRepository).toDto(entity.getTaskTemplate()));
        return dto;
    }


    @Override
    public TaskEntity toEntity(Task dto) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(dto.getId());

        taskEntity.setQuestion(dto.getQuestion());
        taskEntity.setLabel(dto.getLabel());
        taskEntity.setAnswer(dto.getAnswer());


        //Feedback will not be present when creating the task (that's why the following is commented out)
        // ArrayList<FeedbackEntity> feedbacks = new ArrayList<>();
        // for (Long feedbackId : dto.getFeedback() ) {
        //     FeedbackEntity feedbackEntity = feedbackRepository.findById(feedbackId).get();
        //     feedbacks.add(feedbackEntity);
        // }
        // taskEntity.setFeedbacks(feedbacks);

        GraphEntity graphEntity;
        if (dto.getGraph().getId() != null) {
            graphEntity = graphRepository.findById(dto.getGraph().getId()).get();
        }
        else {
            graphEntity = new GraphTransformer(tagRepository).toEntity(dto.getGraph());
            graphRepository.save(graphEntity);
        }


        taskEntity.setGraph(graphEntity);

        // hints
        if (dto.getHints().size()!=0) {
            List<TaskHintEntity> hintEntities = new TaskHintTransformer().toEntityList(dto.getHints());
            //for (TaskHintEntity hintEntity : hintEntities) {
            //    task
            //}
            taskEntity.setHints(hintEntities);
        }


        if (dto.getTemplate() != null) {
            TaskTemplateEntity taskTemplateEntity =new TaskTemplateTransformer(taskSolverRepository).toEntity(dto.getTemplate());
            taskTemplateRepository.save(taskTemplateEntity);
            taskEntity.setTaskTemplate(taskTemplateEntity);
        }


        return taskEntity;
    }
}
