package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.TaskCollectionEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskRepository;
import de.tudresden.inf.st.mathgrassserver.model.TaskCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TaskCollectionTransformer extends ModelTransformer<TaskCollection, TaskCollectionEntity> {

    @Autowired
    TaskRepository taskRepository;


    @Override
    public TaskCollection toDto(TaskCollectionEntity entity) {
        TaskCollection dto = new TaskCollection();
        dto.setId(entity.getId());
        dto.setLabel(entity.getLabel());

        //Tasks
        List<Long> tasks = new ArrayList<>();
        for (TaskEntity te : entity.getTasks())
            tasks.add(te.getId());

        dto.setTasks(tasks);
        return dto;
    }

    @Override
    public TaskCollectionEntity toEntity(TaskCollection dto) {
        TaskCollectionEntity entity = new TaskCollectionEntity();
        entity.setId(dto.getId());
        entity.setLabel(dto.getLabel());

        //Tasks
        List<TaskEntity> tasks = new ArrayList<>();
        for (Long t : dto.getTasks()) {
            TaskEntity taskEntity = taskRepository.findById(t).get();
            tasks.add(taskEntity);
        }
        entity.setTasks(tasks);

        return entity;
    }
}
