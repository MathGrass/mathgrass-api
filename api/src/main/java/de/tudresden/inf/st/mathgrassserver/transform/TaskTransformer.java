package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.TaskEntity;
import de.tudresden.inf.st.mathgrassserver.model.Task;

public class TaskTransformer extends ModelTransformer<Task, TaskEntity> {
    @Override
    public Task toDto(TaskEntity entity) {
        return null;
    }

    @Override
    public TaskEntity toEntity(Task dto) {
        return null;
    }
}
