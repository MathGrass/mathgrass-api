package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.TaskSolverEntity;
import de.tudresden.inf.st.mathgrassserver.model.TaskSolver;

public class TaskSolverTransformer extends ModelTransformer<TaskSolver, TaskSolverEntity> {
    @Override
    public TaskSolver toDto(TaskSolverEntity entity) {
        TaskSolver out = new TaskSolver();
        out.setId(entity.getId());
        out.setLabel(entity.getLabel());
        out.setExecutionDescriptor(entity.getExecutionDescriptor());
        return out;
    }

    @Override
    public TaskSolverEntity toEntity(TaskSolver dto) {
        return new TaskSolverEntity(dto.getLabel(),dto.getExecutionDescriptor() );
    }
}
