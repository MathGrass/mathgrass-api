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
    TagRepository tagRepository;

    /**
     * Task template repository.
     */
    TaskTemplateRepository taskTemplateRepository;

    /**
     * Constructor.
     *
     * @param taskSolverRepository task solver repository
     * @param graphRepository graph repository
     * @param tagRepository tag repository
     * @param taskTemplateRepository task template repository
     */
    public TaskTransformer(TaskSolverRepository taskSolverRepository, GraphRepository graphRepository,
                           TagRepository tagRepository, TaskTemplateRepository taskTemplateRepository) {
        this.taskSolverRepository = taskSolverRepository;
        this.graphRepository = graphRepository;
        this.tagRepository = tagRepository;
        this.taskTemplateRepository = taskTemplateRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task toDto(TaskEntity entity) {
        Task dto = new Task();

        dto.setId(entity.getId());
        dto.setQuestion(entity.getQuestion());
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
        Graph graph = new GraphTransformer(tagRepository).toDto(entity.getGraph());
        dto.setGraph(graph);

        // hints
        if (getUsedRole() != null) {
            dto.setHints(new TaskHintTransformer().toDtoList(entity.getHints()));
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

        taskEntity.setQuestion(dto.getQuestion());
        taskEntity.setLabel(dto.getLabel());
        taskEntity.setAnswer(dto.getAnswer());

        // graph
        GraphEntity graphEntity;
        Long graphId = dto.getGraph().getId();
        if (dto.getGraph().getId() == null || !graphRepository.existsById(graphId)) {
            graphEntity = new GraphTransformer(tagRepository).toEntity(dto.getGraph());
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
        if (!dto.getHints().isEmpty()) {
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
