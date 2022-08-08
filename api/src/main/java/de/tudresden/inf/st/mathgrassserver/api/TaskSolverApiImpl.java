package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TaskSolverApi;
import de.tudresden.inf.st.mathgrassserver.dbmodel.TaskSolverEntity;
import de.tudresden.inf.st.mathgrassserver.model.TaskSolver;
import de.tudresden.inf.st.mathgrassserver.repository.TaskSolverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskSolverApiImpl implements TaskSolverApi {

    @Autowired
    TaskSolverRepository taskSolverRepository;

    @Override
    public ResponseEntity<Void> createTaskSolver(TaskSolver body) {
        this.taskSolverRepository.save(new TaskSolverEntity(body.getLabel(),body.getExecutionDescriptor() ));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> getTaskSolverById(Long id) {
        return null;
    }

}
