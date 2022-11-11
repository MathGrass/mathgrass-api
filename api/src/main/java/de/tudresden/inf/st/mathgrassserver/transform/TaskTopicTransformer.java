package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskTopicEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskRepository;
import de.tudresden.inf.st.mathgrassserver.model.TaskTopic;

import java.util.List;
import java.util.Optional;

/**
 * This class can convert {@link TaskTopic} to {@link TaskTopicEntity} and vice versa.
 */
public class TaskTopicTransformer extends ModelTransformer<TaskTopic, TaskTopicEntity> {
    /**
     * Task repository.
     */
    TaskRepository taskRepository;

    /**
     * Constructor
     *
     * @param taskRepository task repository
     */
    public TaskTopicTransformer(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskTopic toDto(TaskTopicEntity entity) {
        TaskTopic dto = new TaskTopic();
        dto.setId(entity.getId());
        dto.setLabel(entity.getLabel());

        // tasks
        List<Long> tasks = entity.getTasks().stream().map(TaskEntity::getId).toList();

        dto.setTasks(tasks);

        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskTopicEntity toEntity(TaskTopic dto) {
        TaskTopicEntity entity = new TaskTopicEntity();
        entity.setId(dto.getId());
        entity.setLabel(dto.getLabel());

        // tasks
        List<TaskEntity> tasks = dto.getTasks().stream()
                .map(taskEntityId -> taskRepository.findById(taskEntityId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        entity.setTasks(tasks);

        return entity;
    }
}
