package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.TaskCollectionEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskRepository;
import de.tudresden.inf.st.mathgrassserver.model.TaskCollection;

import java.util.List;
import java.util.Optional;

/**
 * This class can convert {@link TaskCollection} to {@link TaskCollectionEntity} and vice versa.
 */
public class TaskCollectionTransformer extends ModelTransformer<TaskCollection, TaskCollectionEntity> {
    /**
     * Task repository.
     */
    TaskRepository taskRepository;

    /**
     * Constructor.
     *
     * @param taskRepository task repository
     */
    public TaskCollectionTransformer(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskCollection toDto(TaskCollectionEntity entity) {
        TaskCollection dto = new TaskCollection();
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
    public TaskCollectionEntity toEntity(TaskCollection dto) {
        TaskCollectionEntity entity = new TaskCollectionEntity();
        entity.setId(dto.getId());
        entity.setLabel(dto.getLabel());

        // tasks
        List<TaskEntity> tasks = dto.getTasks().stream()
                .map(task -> taskRepository.findById(task))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        entity.setTasks(tasks);

        return entity;
    }
}
