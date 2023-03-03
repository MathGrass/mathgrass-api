package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.feedback.Feedback;

/**
 * This class can convert {@link de.tudresden.inf.st.mathgrass.api.model.Feedback} to {@link Feedback} and vice versa.
 */
public class FeedbackTransformer extends ModelTransformer<de.tudresden.inf.st.mathgrass.api.model.Feedback, Feedback> {
    /**
     * {@inheritDoc}
     */
    @Override
    public de.tudresden.inf.st.mathgrass.api.model.Feedback toDto(Feedback entity) {
        de.tudresden.inf.st.mathgrass.api.model.Feedback feedback = new de.tudresden.inf.st.mathgrass.api.model.Feedback();
        feedback.setId(entity.getId());
        feedback.setContent(entity.getContent());

        return feedback;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Feedback toEntity(de.tudresden.inf.st.mathgrass.api.model.Feedback dto) {
        Feedback entity = new Feedback();
        entity.setId(dto.getId());
        entity.setContent(dto.getContent());

        return entity;
    }
}
