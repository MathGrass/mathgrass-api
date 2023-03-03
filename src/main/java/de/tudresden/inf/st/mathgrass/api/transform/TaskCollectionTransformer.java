package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.task.Task;
import de.tudresden.inf.st.mathgrass.api.task.collection.TaskCollection;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;

import java.util.List;
import java.util.Optional;

/**
 * This class can convert {@link de.tudresden.inf.st.mathgrass.api.model.TaskCollection} to {@link TaskCollection} and vice versa.
 */
public class TaskCollectionTransformer extends ModelTransformer<de.tudresden.inf.st.mathgrass.api.model.TaskCollection, TaskCollection> {
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
    public de.tudresden.inf.st.mathgrass.api.model.TaskCollection toDto(TaskCollection entity) {
        de.tudresden.inf.st.mathgrass.api.model.TaskCollection dto = new de.tudresden.inf.st.mathgrass.api.model.TaskCollection();
        dto.setId(entity.getId());
        dto.setLabel(entity.getLabel());

        // tasks
        List<Long> tasks = entity.getTasks().stream().map(Task::getId).toList();
        dto.setTasks(tasks);

        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskCollection toEntity(de.tudresden.inf.st.mathgrass.api.model.TaskCollection dto) {
        TaskCollection entity = new TaskCollection();
        entity.setId(dto.getId());
        entity.setLabel(dto.getLabel());

        // tasks
        List<Task> tasks = dto.getTasks().stream()
                .map(task -> taskRepository.findById(task))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        entity.setTasks(tasks);

        return entity;
    }
}
