package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TaskApi;
import de.tudresden.inf.st.mathgrassserver.model.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskApiImpl implements TaskApi {

    @Override
    public ResponseEntity<Void> addTaskFeedback(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> createTask(Task body) {
        return null;
    }

    @Override
    public ResponseEntity<Task> getTaskById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateTask(Long id, Task task) {
        return null;
    }

}
