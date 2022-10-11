package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.FeedbackEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.GraphEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.GraphRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TagRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskSolverRepository;
import de.tudresden.inf.st.mathgrassserver.model.Graph;
import de.tudresden.inf.st.mathgrassserver.model.Task;

import java.util.ArrayList;

public class TaskTransformer extends ModelTransformer<Task, TaskEntity> {

    TaskSolverRepository taskSolverRepository;
    GraphRepository graphRepository;
    TagRepository tagRepository;


    public TaskTransformer( TaskSolverRepository taskSolverRepository, GraphRepository graphRepository, TagRepository tagRepository) {
        this.taskSolverRepository = taskSolverRepository;
        this.graphRepository = graphRepository;
        this.tagRepository = tagRepository;
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

        GraphEntity graphEntity = new GraphTransformer(tagRepository).toEntity(dto.getGraph());
        taskEntity.setGraph(graphEntity);

        taskEntity.setHints(new TaskHintTransformer().toEntityList(dto.getHints()));

        taskEntity.setTaskTemplate(new TaskTemplateTransformer(taskSolverRepository).toEntity(dto.getTemplate()));

        return taskEntity;
    }
}
