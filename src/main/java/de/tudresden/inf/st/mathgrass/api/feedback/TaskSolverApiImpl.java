package de.tudresden.inf.st.mathgrass.api.feedback;

import de.tudresden.inf.st.mathgrass.api.apiModel.TaskSolverApi;
import de.tudresden.inf.st.mathgrass.api.common.AbstractApiElement;
import de.tudresden.inf.st.mathgrass.api.model.TaskSolverDTO;
import de.tudresden.inf.st.mathgrass.api.transform.TaskSolverTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * This class contains functionality to create and get {@link TaskSolverDTO}s.
 */
@RestController
public class TaskSolverApiImpl extends AbstractApiElement implements TaskSolverApi {
    /**
     * Task solver repository.
     */
    final TaskSolverRepository taskSolverRepository;

    /**
     * Constructor.
     *
     * @param taskSolverRepository task solver repository
     */
    public TaskSolverApiImpl(TaskSolverRepository taskSolverRepository) {
        this.taskSolverRepository = taskSolverRepository;
    }

    /**
     * Create a new task solver.
     *
     * @param body task solver to create
     * @return Response with ID of created task solver
     */
    @Override
    public ResponseEntity<Long> createTaskSolver(TaskSolverDTO body) {
        TaskSolver entity = this.taskSolverRepository.save(new TaskSolverTransformer().toEntity(body));

        return ok(entity.getId());
    }

    /**
     * Get a task solver by its ID.
     *
     * @param taskSolverId ID of task solver
     * @return Response with task solver
     */
    @Override
    public ResponseEntity<TaskSolverDTO> getTaskSolverById(Long taskSolverId) {
        Optional<TaskSolver> optSolver = taskSolverRepository.findById(taskSolverId);
        if (optSolver.isPresent()) {
            TaskSolverDTO taskSolver = new TaskSolverTransformer().toDto(optSolver.get());
            return ResponseEntity.ok(taskSolver);
        } else {
            return notFound();
        }
    }
}
