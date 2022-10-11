package de.tudresden.inf.st.mathgrassserver.api;
import de.tudresden.inf.st.mathgrassserver.apiModel.TaskTemplateApi;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskHintEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskTemplateEntity;
import de.tudresden.inf.st.mathgrassserver.database.repository.TagRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskSolverRepository;
import de.tudresden.inf.st.mathgrassserver.database.repository.TaskTemplateRepository;
import de.tudresden.inf.st.mathgrassserver.model.TaskHint;
import de.tudresden.inf.st.mathgrassserver.model.TaskTemplate;
import de.tudresden.inf.st.mathgrassserver.transform.TaskHintTransformer;
import de.tudresden.inf.st.mathgrassserver.transform.TaskTemplateTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;


@RestController
public class TaskTemplateApiImpl extends AbsApi implements TaskTemplateApi {

    final TaskTemplateRepository taskTemplateRepository;

    final TaskSolverRepository taskSolverRepository;

    public TaskTemplateApiImpl(TaskTemplateRepository taskTemplateRepository, TaskSolverRepository taskSolverRepository) {
        this.taskTemplateRepository = taskTemplateRepository;
        this.taskSolverRepository = taskSolverRepository;
    }


    @Override
    public ResponseEntity<Long> createTaskTemplate(TaskTemplate body) {
        TaskTemplateEntity entity = taskTemplateRepository.save(new TaskTemplateTransformer(taskSolverRepository).toEntity(body));
        return ok(entity.getId());
    }

    @Override
    public ResponseEntity<TaskTemplate> getTaskTemplateById(Long id) {
        checkExistence(id,taskTemplateRepository);
        TaskTemplateEntity taskTemplateEntity = taskTemplateRepository.findById(id).get();
        TaskTemplate taskTemplate = new TaskTemplateTransformer(taskSolverRepository).toDto(taskTemplateEntity);
        return ok(taskTemplate);
    }

    //For students during exam (because they won't receive the hints in the tasks)
    @Override
    public ResponseEntity<TaskHint> getTaskTemplateHint(Long taskTemplateId, Integer index) {
        if (index<0) {
            illegalArgs();
        }
        checkExistence(taskTemplateId,taskTemplateRepository);
        TaskTemplateEntity entity = new TaskTemplateEntity();
        // TODO: remember that student asked for hint
        List<TaskHintEntity> hints = entity.getHints();
        if (hints==null || hints.size()<index+1) {
            notFound();
        }
        TaskHintEntity hintEntity = Objects.requireNonNull(hints).get(index);
        TaskHint hintDto = new TaskHintTransformer().toDto(hintEntity);
        return ok(hintDto);
    }


    @Override
    public ResponseEntity<Void> setTaskTemplateLabel(Long id, String label) {
        checkExistence(id,taskTemplateRepository);
        TaskTemplateEntity entity = taskTemplateRepository.findById(id).get();
        entity.setLabel(label);
        taskTemplateRepository.save(entity);
        return ok();
    }

    @Override
    public ResponseEntity<Void> setTaskTemplateQuestion(Long id, String question) {
        checkExistence(id,taskTemplateRepository);
        TaskTemplateEntity entity = taskTemplateRepository.findById(id).get();
        entity.setQuestion(question);
        taskTemplateRepository.save(entity);
        return ok();
    }

}
