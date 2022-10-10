package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.TaskCollectionApi;
import de.tudresden.inf.st.mathgrassserver.apiModel.TaskTopicApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskCollectionEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskSolverEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskTopicEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskCollectionRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskTopicRepository;
import de.tudresden.inf.st.mathgrassserver.model.TaskCollection;
import de.tudresden.inf.st.mathgrassserver.model.TaskTopic;
import de.tudresden.inf.st.mathgrassserver.transform.TaskCollectionTransformer;
import de.tudresden.inf.st.mathgrassserver.transform.TaskTopicTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskTopicApiImpl extends AbsApi implements TaskTopicApi {

    final TaskTopicRepository taskTopicRepository;

    final TaskRepository taskRepository;

    public TaskTopicApiImpl(TaskTopicRepository taskTopicRepository, TaskRepository taskRepository) {
        this.taskTopicRepository = taskTopicRepository;
        this.taskRepository = taskRepository;
    }


    @Override
    public ResponseEntity<Void> createTaskTopic(TaskTopic taskTopic) {
        this.taskTopicRepository.save(new TaskTopicTransformer(this.taskRepository).toEntity(taskTopic));
        return ok();
    }

    @Override
    public ResponseEntity<List<TaskTopic>> getTaskTopics() {
        List<TaskTopicEntity> collections = taskTopicRepository.findAll();
        return ok(new TaskTopicTransformer(this.taskRepository).toDtoList(collections));
    }
}
