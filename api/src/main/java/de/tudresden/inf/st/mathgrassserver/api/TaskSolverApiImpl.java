package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TaskSolverApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskSolverEntity;
import de.tudresden.inf.st.mathgrassserver.model.TaskSolver;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskSolverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class TaskSolverApiImpl extends AbsApi implements TaskSolverApi {

    @Autowired
    TaskSolverRepository taskSolverRepository;

    @Override
    public ResponseEntity<Long> createTaskSolver(TaskSolver body) {
        TaskSolverEntity entity = this.taskSolverRepository.save(new TaskSolverEntity(body.getLabel(),body.getExecutionDescriptor() ));
        return ok(entity.getId());
    }

    @Override
    public ResponseEntity<TaskSolver> getTaskSolverById(Long id) {
        Optional<TaskSolverEntity> result = this.taskSolverRepository.findById(id);
        if (result.isPresent()) {
            TaskSolverEntity entity = result.get();
            TaskSolver out = new TaskSolver();
            out.setId(entity.getId());
            out.setLabel(entity.getLabel());
            out.setExecutionDescriptor(entity.getExecutionDescriptor());
            return ResponseEntity.ok().body(out);
        }
        return null;

    }

}
