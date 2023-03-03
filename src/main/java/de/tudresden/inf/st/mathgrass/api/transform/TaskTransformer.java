package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.database.entity.*;
import de.tudresden.inf.st.mathgrass.api.database.repository.GraphRepository;
import de.tudresden.inf.st.mathgrass.api.database.repository.LabelRepository;
import de.tudresden.inf.st.mathgrass.api.database.repository.TaskSolverRepository;
import de.tudresden.inf.st.mathgrass.api.database.repository.TaskTemplateRepository;
import de.tudresden.inf.st.mathgrass.api.model.Graph;
import de.tudresden.inf.st.mathgrass.api.model.Question;
import de.tudresden.inf.st.mathgrass.api.model.Task;
import de.tudresden.inf.st.mathgrass.api.model.TaskHint;
import de.tudresden.inf.st.mathgrass.api.database.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class can convert {@link Task} to {@link TaskEntity} and vice versa.
 */
public class TaskTransformer extends ModelTransformer<Task, TaskEntity> {
    /**
     * Task solver repository.
     */
    TaskSolverRepository taskSolverRepository;

    /**
     * Graph repository.
     */
    GraphRepository graphRepository;

    /**
     * Tag repository.
     */
    LabelRepository labelRepository;

    /**
     * Task template repository.
     */
    TaskTemplateRepository taskTemplateRepository;

    /**
     * Constructor.
     *
     * @param taskSolverRepository task solver repository
     * @param graphRepository graph repository
     * @param labelRepository tag repository
     * @param taskTemplateRepository task template repository
     */
    public TaskTransformer(TaskSolverRepository taskSolverRepository, GraphRepository graphRepository,
                           LabelRepository labelRepository, TaskTemplateRepository taskTemplateRepository) {
        this.taskSolverRepository = taskSolverRepository;
        this.graphRepository = graphRepository;
        this.labelRepository = labelRepository;
        this.taskTemplateRepository = taskTemplateRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task toDto(TaskEntity entity) {
        Task dto = new Task();

        dto.setId(entity.getId());
        //dto.setQuestion(entity.getQuestion());
        dto.setLabel(entity.getLabel());

        // answer should not be sent to student
        if (getUsedRole() != null) {
            dto.setAnswer(entity.getAnswer());
        }

        // feedbacks
        ArrayList<Long> feedbackIds = new ArrayList<>();
        for (FeedbackEntity feedbackEntity : entity.getFeedbacks() ) {
            feedbackIds.add(feedbackEntity.getId());
        }
        dto.setFeedback(feedbackIds);

        // graph
        Graph graph = new GraphTransformer(labelRepository).toDto(entity.getGraph());
        dto.setGraph(graph);

        // hints
        if (getUsedRole() != null) {
            List<TaskHintEntity> hints = entity.getHints();
            List<TaskHint> taskHints = new TaskHintTransformer().toDtoList(hints);
            dto.setHints(taskHints);
        }

        QuestionEntity question = entity.getQuestion();
        if(question != null){
            dto.setQuestion(new Question().question(question.getQuestion()).isDynamicQuestion(question.getDynamicQuestion()));
        }

        // template
        dto.setTemplate(new TaskTemplateTransformer(taskSolverRepository).toDto(entity.getTaskTemplate()));

        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskEntity toEntity(Task dto) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(dto.getId());

        //taskEntity.setQuestion(dto.getQuestion());
        taskEntity.setLabel(dto.getLabel());
        taskEntity.setAnswer(dto.getAnswer());

        // graph
        GraphEntity graphEntity;
        Long graphId = dto.getGraph().getId();
        if (dto.getGraph().getId() == null || !graphRepository.existsById(graphId)) {
            graphEntity = new GraphTransformer(labelRepository).toEntity(dto.getGraph());
            graphRepository.save(graphEntity);
        } else {
            Optional<GraphEntity> optGraphEntity = graphRepository.findById(graphId);
            if (optGraphEntity.isPresent()) {
                graphEntity = optGraphEntity.get();
            } else {
                throw new IllegalArgumentException("Couldn't find graph with ID " + graphId);
            }
        }
        taskEntity.setGraph(graphEntity);

        // hints
        if (dto.getHints() != null && !dto.getHints().isEmpty()) {
            List<TaskHintEntity> hintEntities = new TaskHintTransformer().toEntityList(dto.getHints());
            taskEntity.setHints(hintEntities);
        }

        // task template
        if (dto.getTemplate() != null) {
            TaskTemplateEntity taskTemplateEntity =new TaskTemplateTransformer(taskSolverRepository).toEntity(dto.getTemplate());
            taskTemplateRepository.save(taskTemplateEntity);
            taskEntity.setTaskTemplate(taskTemplateEntity);
        }

        return taskEntity;
    }
}
