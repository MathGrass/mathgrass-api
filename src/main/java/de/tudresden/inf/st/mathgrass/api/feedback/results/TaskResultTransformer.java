package de.tudresden.inf.st.mathgrass.api.feedback.results;

import de.tudresden.inf.st.mathgrass.api.model.TaskResultDTO;
import de.tudresden.inf.st.mathgrass.api.task.Task;
import de.tudresden.inf.st.mathgrass.api.task.TaskRepository;
import de.tudresden.inf.st.mathgrass.api.transform.ModelTransformer;

import java.util.Optional;

/**
 * This class can convert {@link TaskResultDTO} to {@link TaskResult} and vice versa.
 */
public class TaskResultTransformer extends ModelTransformer<TaskResultDTO, TaskResult> {
    /**
     * Task repository.
     */
    private final TaskRepository taskRepository;

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
    public TaskResultDTO toDto(TaskResult entity) {
        TaskResultDTO dto = new TaskResultDTO();
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
    public TaskResult toEntity(TaskResultDTO dto) {
        TaskResult entity = new TaskResult();

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
