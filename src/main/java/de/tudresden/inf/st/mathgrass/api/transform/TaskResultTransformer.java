package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.feedback.TaskResultEntity;
import de.tudresden.inf.st.mathgrass.api.task.Task;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import de.tudresden.inf.st.mathgrass.api.model.TaskResult;

import java.util.Optional;

/**
 * This class can convert {@link TaskResult} to {@link TaskResultEntity} and vice versa.
 */
public class TaskResultTransformer extends ModelTransformer<TaskResult, TaskResultEntity> {
    /**
     * Task repository.
     */
    TaskRepository taskRepository;

    /**
     * Constructor.
     *
     * @param taskRepository task repository
     */
    public TaskResultTransformer(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskResult toDto(TaskResultEntity entity) {
        TaskResult dto = new TaskResult();
        dto.setTask(entity.getTask().getId());
        dto.setId(entity.getId());
        dto.setSubmissionDate(entity.getSubmissionDate());
        dto.setEvaluationDate(entity.getEvaluationDate());
        dto.setAnswer(entity.getAnswer());
        dto.setAnswerTrue(entity.isAnswerTrue());

        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskResultEntity toEntity(TaskResult dto) {
        TaskResultEntity entity = new TaskResultEntity();

        Optional<Task> optTask = taskRepository.findById(dto.getTask());
        if (optTask.isPresent()) {
            entity.setTask(optTask.get());
            entity.setId(dto.getId());
            entity.setEvaluationDate(dto.getEvaluationDate());
            entity.setSubmissionDate(dto.getSubmissionDate());
            entity.setAnswerTrue(dto.getAnswerTrue());

            return entity;
        } else {
            throw new IllegalArgumentException("Couldn't find task result with ID " + dto.getTask());
        }
    }
}
