package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TaskSolverApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskSolverEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskSolverRepository;
import de.tudresden.inf.st.mathgrassserver.model.TaskSolver;
import de.tudresden.inf.st.mathgrassserver.transform.TaskSolverTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * This class contains functionality to create and get {@link TaskSolver}s.
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
    public ResponseEntity<Long> createTaskSolver(TaskSolver body) {
        TaskSolverEntity entity = this.taskSolverRepository.save(new TaskSolverTransformer().toEntity(body));

        return ok(entity.getId());
    }

    /**
     * Get a task solver by its ID.
     *
     * @param taskSolverId ID of task solver
     * @return Response with task solver
     */
    @Override
    public ResponseEntity<TaskSolver> getTaskSolverById(Long taskSolverId) {
        Optional<TaskSolverEntity> optSolver = taskSolverRepository.findById(taskSolverId);
        if (optSolver.isPresent()) {
            TaskSolver taskSolver = new TaskSolverTransformer().toDto(optSolver.get());
            return ResponseEntity.ok(taskSolver);
        } else {
            return notFound();
        }
    }
}
