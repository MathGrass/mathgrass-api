package de.tudresden.inf.st.mathgrass.api.feedback;

import de.tudresden.inf.st.mathgrass.api.model.FeedbackDTO;
import de.tudresden.inf.st.mathgrass.api.transform.ModelTransformer;

/**
 * This class can convert {@link FeedbackDTO} to {@link Feedback} and vice versa.
 */
public class FeedbackTransformer extends ModelTransformer<FeedbackDTO, Feedback> {
    /**
     * {@inheritDoc}
     */
    @Override
    public FeedbackDTO toDto(Feedback entity) {
        FeedbackDTO feedback = new FeedbackDTO();
        feedback.setId(entity.getId());
        feedback.setContent(entity.getContent());

        return feedback;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Feedback toEntity(FeedbackDTO dto) {
        Feedback entity = new Feedback();
        entity.setId(dto.getId());
        entity.setContent(dto.getContent());

        return entity;
    }
}
