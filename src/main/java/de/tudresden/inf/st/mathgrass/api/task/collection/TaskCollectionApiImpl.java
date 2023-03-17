package de.tudresden.inf.st.mathgrass.api.task.collection;

import de.tudresden.inf.st.mathgrass.api.apiModel.TaskCollectionApi;
import de.tudresden.inf.st.mathgrass.api.common.AbstractApiElement;
import de.tudresden.inf.st.mathgrass.api.model.TaskCollectionDTO;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * This class contains functionality to create and get {@link TaskCollectionDTO}s.
 */
@RestController
public class TaskCollectionApiImpl extends AbstractApiElement implements TaskCollectionApi {
    /**
     * Task collection repository.
     */
    final TaskCollectionRepository taskCollectionRepository;

    /**
     * Task repository.
     */
    final TaskRepository taskRepository;

    /**
     * Constructor.
     *
     * @param taskCollectionRepository task collection repository
     * @param taskRepository task repository
     */
    public TaskCollectionApiImpl(TaskCollectionRepository taskCollectionRepository, TaskRepository taskRepository) {
        this.taskCollectionRepository = taskCollectionRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Create a new task collection.
     *
     * @param taskCollection task collection to create
     * @return Response with created task collection
     */
    @Override
    public ResponseEntity<TaskCollectionDTO> createTaskCollection(TaskCollectionDTO taskCollection) {
        // create task collection and save
        TaskCollection entity = new TaskCollectionTransformer(taskRepository).toEntity(taskCollection);
        taskCollectionRepository.save(entity);

        // set ID with new ID of entity
        taskCollection.setId(entity.getId());

        return ok(taskCollection);
    }

    /**
     * Get a task collection by its ID.
     * @param taskCollectionId ID of task collection to get
     * @return Response with task collection
     */
    @Override
    public ResponseEntity<TaskCollectionDTO> getTaskCollectionById(Long taskCollectionId) {
        Optional<TaskCollection> optTaskCollection = taskCollectionRepository.findById(taskCollectionId);
        if (optTaskCollection.isPresent()) {
            return ok(new TaskCollectionTransformer(taskRepository).toDto(optTaskCollection.get()));
        } else {
            return notFound();
        }
    }

    /**
     * Get a list of all task collections.
     *
     * @return Response with list of task collections
     */
    @Override
    public ResponseEntity<List<TaskCollectionDTO>> getTaskCollections() {
        List<TaskCollection> collections = taskCollectionRepository.findAll();

        return ok(new TaskCollectionTransformer(this.taskRepository).toDtoList(collections));
    }
}
