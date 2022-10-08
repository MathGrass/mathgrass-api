package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TaskCollectionApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskCollectionEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskSolverEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskCollectionRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskRepository;
import de.tudresden.inf.st.mathgrassserver.model.TaskCollection;
import de.tudresden.inf.st.mathgrassserver.transform.TaskCollectionTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskCollectionApiImpl extends AbsApi implements TaskCollectionApi {

    @Autowired
    TaskCollectionRepository taskCollectionRepository;

    @Autowired
    TaskRepository taskRepository;

    @Override
    public ResponseEntity<Void> createTaskCollection(TaskCollection taskCollection) {
        this.taskCollectionRepository.save(new TaskCollectionTransformer().toEntity(taskCollection));
        return ok();
    }

    @Override
    public ResponseEntity<List<TaskCollection>> getTaskCollections() {
        List<TaskCollectionEntity> collections = taskCollectionRepository.findAll();
        return ok(new TaskCollectionTransformer().toDtoList(collections));
    }
}
