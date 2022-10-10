package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskResultEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskRepository;
import de.tudresden.inf.st.mathgrassserver.model.TaskResult;



public class TaskResultTransformer extends ModelTransformer<TaskResult, TaskResultEntity> {
    TaskRepository taskRepository;

    public TaskResultTransformer(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResult toDto(TaskResultEntity entity) {
        TaskResult dto = new TaskResult();
        dto.setTask(entity.getTask().getId());
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setAnswer(entity.getAnswer());
        return dto;
    }

    @Override
    public TaskResultEntity toEntity(TaskResult dto) {
        TaskResultEntity entity = new TaskResultEntity();

        TaskEntity task = taskRepository.findById(dto.getTask()).get();
        entity.setTask(task);
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setAnswer(dto.getAnswer());
        return entity;
    }
}
