package de.tudresden.inf.st.mathgrass.api.task.collection;

import de.tudresden.inf.st.mathgrass.api.model.TaskCollectionDTO;
import de.tudresden.inf.st.mathgrass.api.task.Task;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import de.tudresden.inf.st.mathgrass.api.transform.ModelTransformer;

import java.util.List;
import java.util.Optional;

/**
 * This class can convert {@link TaskCollectionDTO} to {@link TaskCollection} and vice versa.
 */
public class TaskCollectionTransformer extends ModelTransformer<TaskCollectionDTO, TaskCollection> {
    /**
     * Task repository.
     */
    private final TaskRepository taskRepository;

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
    public TaskCollectionDTO toDto(TaskCollection entity) {
        TaskCollectionDTO dto = new TaskCollectionDTO();
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
    public TaskCollection toEntity(TaskCollectionDTO dto) {
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
