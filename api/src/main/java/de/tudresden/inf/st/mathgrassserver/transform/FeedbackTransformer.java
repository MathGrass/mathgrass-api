package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.FeedbackEntity;
import de.tudresden.inf.st.mathgrassserver.model.Feedback;

/**
 * This class can convert {@link Feedback} to {@link FeedbackEntity} and vice versa.
 */
public class FeedbackTransformer extends ModelTransformer<Feedback, FeedbackEntity> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Feedback toDto(FeedbackEntity entity) {
        Feedback feedback = new Feedback();
        feedback.setId(entity.getId());
        feedback.setContent(entity.getContent());

        return feedback;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeedbackEntity toEntity(Feedback dto) {
        FeedbackEntity entity = new FeedbackEntity();
        entity.setId(dto.getId());
        entity.setContent(dto.getContent());

        return entity;
    }
}
