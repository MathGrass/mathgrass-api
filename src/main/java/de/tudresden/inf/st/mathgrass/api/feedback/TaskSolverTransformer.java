package de.tudresden.inf.st.mathgrass.api.feedback;

import de.tudresden.inf.st.mathgrass.api.feedback.TaskSolver;
import de.tudresden.inf.st.mathgrass.api.model.TaskSolverDTO;
import de.tudresden.inf.st.mathgrass.api.transform.ModelTransformer;

/**
 * This class can convert {@link TaskSolverDTO} to {@link TaskSolver} and vice versa.
 */
public class TaskSolverTransformer extends ModelTransformer<TaskSolverDTO, TaskSolver> {
    /**
     * {@inheritDoc}
     */
    @Override
    public TaskSolverDTO toDto(TaskSolver entity) {
        TaskSolverDTO out = new TaskSolverDTO();
        out.setId(entity.getId());
        out.setLabel(entity.getLabel());
        out.setExecutionDescriptor(entity.getExecutionDescriptor());
        return out;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskSolver toEntity(TaskSolverDTO dto) {
        return new TaskSolver(dto.getLabel(),dto.getExecutionDescriptor() );
    }
}
