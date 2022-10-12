package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TaskSolverApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskSolverEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskSolverRepository;
import de.tudresden.inf.st.mathgrassserver.model.TaskSolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskSolverApiImpl extends AbstractApiElement implements TaskSolverApi {

    final TaskSolverRepository taskSolverRepository;

    public TaskSolverApiImpl(TaskSolverRepository taskSolverRepository) {
        this.taskSolverRepository = taskSolverRepository;
    }

    @Override
    public ResponseEntity<Long> createTaskSolver(TaskSolver body) {
        TaskSolverEntity entity = this.taskSolverRepository.save(new TaskSolverEntity(body.getLabel(),body.getExecutionDescriptor() ));
        return ok(entity.getId());
    }

    @Override
    public ResponseEntity<TaskSolver> getTaskSolverById(Long id) {
        TaskSolverEntity entity = this.taskSolverRepository.findById(id).get();
        TaskSolver out = new TaskSolver();
        out.setId(entity.getId());
        out.setLabel(entity.getLabel());
        out.setExecutionDescriptor(entity.getExecutionDescriptor());
        return ResponseEntity.ok().body(out);

    }

}
