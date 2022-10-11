package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TaskCollectionApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskCollectionEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskCollectionRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskRepository;
import de.tudresden.inf.st.mathgrassserver.model.TaskCollection;
import de.tudresden.inf.st.mathgrassserver.transform.TaskCollectionTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskCollectionApiImpl extends AbsApi implements TaskCollectionApi {

    final TaskCollectionRepository taskCollectionRepository;

    final TaskRepository taskRepository;

    public TaskCollectionApiImpl(TaskCollectionRepository taskCollectionRepository, TaskRepository taskRepository) {
        this.taskCollectionRepository = taskCollectionRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public ResponseEntity<Void> createTaskCollection(TaskCollection taskCollection) {
        this.taskCollectionRepository.save(new TaskCollectionTransformer(this.taskRepository).toEntity(taskCollection));
        return ok();
    }

    @Override
    public ResponseEntity<TaskCollection> getTaskCollectionById(Long taskCollectionId) {
        return ok(new TaskCollectionTransformer(taskRepository).toDto(taskCollectionRepository.findById(taskCollectionId).get()));
    }

    @Override
    public ResponseEntity<List<TaskCollection>> getTaskCollections() {
        List<TaskCollectionEntity> collections = taskCollectionRepository.findAll();
        return ok(new TaskCollectionTransformer(this.taskRepository).toDtoList(collections));
    }
}
