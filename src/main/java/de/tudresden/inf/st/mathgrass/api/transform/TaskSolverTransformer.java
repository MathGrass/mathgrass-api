package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.feedback.TaskSolver;

/**
 * This class can convert {@link de.tudresden.inf.st.mathgrass.api.model.TaskSolver} to {@link TaskSolver} and vice versa.
 */
public class TaskSolverTransformer extends ModelTransformer<de.tudresden.inf.st.mathgrass.api.model.TaskSolver, TaskSolver> {
    /**
     * {@inheritDoc}
     */
    @Override
    public de.tudresden.inf.st.mathgrass.api.model.TaskSolver toDto(TaskSolver entity) {
        de.tudresden.inf.st.mathgrass.api.model.TaskSolver out = new de.tudresden.inf.st.mathgrass.api.model.TaskSolver();
        out.setId(entity.getId());
        out.setLabel(entity.getLabel());
        out.setExecutionDescriptor(entity.getExecutionDescriptor());
        return out;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskSolver toEntity(de.tudresden.inf.st.mathgrass.api.model.TaskSolver dto) {
        return new TaskSolver(dto.getLabel(),dto.getExecutionDescriptor() );
    }
}
