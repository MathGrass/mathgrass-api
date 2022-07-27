package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TaskSolverApi;
import de.tudresden.inf.st.mathgrassserver.model.TaskSolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskSolverApiImpl implements TaskSolverApi {
    @Override
    public ResponseEntity<Void> createTaskSolver(TaskSolver body) {
        return null;
    }

    @Override
    public ResponseEntity<Void> getTaskSolverById(String id) {
        return null;
    }
}
