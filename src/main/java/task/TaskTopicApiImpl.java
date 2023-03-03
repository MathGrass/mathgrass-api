package task;

import de.tudresden.inf.st.mathgrass.api.common.AbstractApiElement;
import de.tudresden.inf.st.mathgrass.api.transform.TaskTopicEntity;
import task.TaskRepository;
import task.TaskTopicRepository;
import de.tudresden.inf.st.mathgrass.api.apiModel.TaskTopicApi;
import de.tudresden.inf.st.mathgrass.api.model.TaskTopic;
import de.tudresden.inf.st.mathgrass.api.transform.TaskTopicTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class contains functionality to create and get {@link TaskTopic}s.
 */
@RestController
public class TaskTopicApiImpl extends AbstractApiElement implements TaskTopicApi {
    /**
     * Task topic repository.
     */
    final TaskTopicRepository taskTopicRepository;
    /**
     * Task repository.
     */
    final TaskRepository taskRepository;

    /**
     * Constructor.
     *
     * @param taskTopicRepository task topic repository
     * @param taskRepository task repository
     */
    public TaskTopicApiImpl(TaskTopicRepository taskTopicRepository, TaskRepository taskRepository) {
        this.taskTopicRepository = taskTopicRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Create a task topic.
     *
     * @param taskTopic task topic to create
     * @return Response with task topic
     */
    @Override
    public ResponseEntity<TaskTopic> createTaskTopic(TaskTopic taskTopic) {
        TaskTopicEntity entity = this.taskTopicRepository.save(
                new TaskTopicTransformer(this.taskRepository).toEntity(taskTopic));
        taskTopic.setId(entity.getId());

        return ok(taskTopic);
    }

    /**
     * Get all task topics.
     *
     * @return Response with list of task topics
     */
    @Override
    public ResponseEntity<List<TaskTopic>> getTaskTopics() {
        List<TaskTopicEntity> collections = taskTopicRepository.findAll();

        return ok(new TaskTopicTransformer(this.taskRepository).toDtoList(collections));
    }
}
