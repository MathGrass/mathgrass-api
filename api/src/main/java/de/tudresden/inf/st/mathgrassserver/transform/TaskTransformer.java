package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.FeedbackEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.GraphEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.FeedbackRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.GraphRepository;
import de.tudresden.inf.st.mathgrassserver.model.Feedback;
import de.tudresden.inf.st.mathgrassserver.model.Graph;
import de.tudresden.inf.st.mathgrassserver.model.GraphEdges;
import de.tudresden.inf.st.mathgrassserver.model.Task;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class TaskTransformer extends ModelTransformer<Task, TaskEntity> {

    @Autowired
    GraphRepository graphRepository;

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

        Graph graph = new GraphTransformer().toDto(entity.getGraph());
        dto.setGraph(graph);

        // hints
        if (getUsedRole()!=null) {
            dto.setHints(new TaskHintTransformer().toDtoList(entity.getHints()));
        }

        //template
        dto.setTemplate(new TaskTemplateTransformer().toDto(entity.getTaskTemplate()));
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
        //     FeedbackEntity feedbackEntity = feedbackRepository.getReferenceById(feedbackId);
        //     feedbacks.add(feedbackEntity);
        // }
        // taskEntity.setFeedbacks(feedbacks);

        GraphEntity graphEntity = new GraphTransformer().toEntity(dto.getGraph());
        taskEntity.setGraph(graphEntity);

        taskEntity.setHints(new TaskHintTransformer().toEntityList(dto.getHints()));

        taskEntity.setTaskTemplate(new TaskTemplateTransformer().toEntity(dto.getTemplate()));

        return taskEntity;
    }
}
