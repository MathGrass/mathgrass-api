package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.feedback.TaskSolverEntity;
import de.tudresden.inf.st.mathgrass.api.model.TaskSolver;

/**
 * This class can convert {@link TaskSolver} to {@link TaskSolverEntity} and vice versa.
 */
public class TaskSolverTransformer extends ModelTransformer<TaskSolver, TaskSolverEntity> {
    /**
     * {@inheritDoc}
     */
    @Override
    public TaskSolver toDto(TaskSolverEntity entity) {
        TaskSolver out = new TaskSolver();
        out.setId(entity.getId());
        out.setLabel(entity.getLabel());
        out.setExecutionDescriptor(entity.getExecutionDescriptor());
        return out;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskSolverEntity toEntity(TaskSolver dto) {
        return new TaskSolverEntity(dto.getLabel(),dto.getExecutionDescriptor() );
    }
}
