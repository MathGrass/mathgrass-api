package de.tudresden.inf.st.mathgrass.api.task;

import de.tudresden.inf.st.mathgrass.api.graph.GraphRepository;
import de.tudresden.inf.st.mathgrass.api.graph.GraphTransformer;
import de.tudresden.inf.st.mathgrass.api.label.LabelRepository;
import de.tudresden.inf.st.mathgrass.api.graph.Graph;
import de.tudresden.inf.st.mathgrass.api.model.QuestionDTO;
import de.tudresden.inf.st.mathgrass.api.model.TaskDTO;
import de.tudresden.inf.st.mathgrass.api.model.GraphDTO;
import de.tudresden.inf.st.mathgrass.api.task.question.Question;
import de.tudresden.inf.st.mathgrass.api.transform.ModelTransformer;

import java.util.Optional;

/**
 * This class can convert {@link TaskDTO} to {@link Task} and vice versa.
 */
public class TaskTransformer extends ModelTransformer<TaskDTO, Task> {
    /**
     * Graph repository.
     */
    GraphRepository graphRepository;

    /**
     * Tag repository.
     */
    LabelRepository labelRepository;

    /**
     * Constructor.
     *
     * @param taskSolverRepository task solver repository
     * @param graphRepository      graph repository
     * @param labelRepository      tag repository
     */
    public TaskTransformer(GraphRepository graphRepository,
                           LabelRepository labelRepository) {
        this.graphRepository = graphRepository;
        this.labelRepository = labelRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskDTO toDto(Task entity) {
        TaskDTO dto = new TaskDTO();

        dto.setId(entity.getId());

        // graph
        GraphDTO graph =
                new GraphTransformer(labelRepository).toDto(entity.getGraph());
        dto.setGraph(graph);

        Question question = entity.getQuestion();
        if (question != null) {
            dto.setQuestion(new QuestionDTO().question(question.getQuestionText()));
        }

        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task toEntity(TaskDTO dto) {
        Task taskEntity = new Task();
        taskEntity.setId(dto.getId());

        // graph
        Graph graphEntity;
        Long graphId = dto.getGraph().getId();
        if (!graphRepository.existsById(graphId)) {
            graphEntity =
                    new GraphTransformer(labelRepository).toEntity(dto.getGraph());
            graphRepository.save(graphEntity);
        } else {
            Optional<Graph> optGraphEntity = graphRepository.findById(graphId);
            if (optGraphEntity.isPresent()) {
                graphEntity = optGraphEntity.get();
            } else {
                throw new IllegalArgumentException("Couldn't find graph with " +
                        "ID " + graphId);
            }
        }
        taskEntity.setGraph(graphEntity);

        return taskEntity;
    }
}