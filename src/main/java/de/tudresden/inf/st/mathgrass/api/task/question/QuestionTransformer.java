package de.tudresden.inf.st.mathgrass.api.task.question;

import de.tudresden.inf.st.mathgrass.api.model.QuestionDTO;
import de.tudresden.inf.st.mathgrass.api.task.question.QuestionLegacy;
import de.tudresden.inf.st.mathgrass.api.transform.ModelTransformer;

public class QuestionTransformer extends ModelTransformer<QuestionDTO, QuestionLegacy> {
    @Override
    public QuestionDTO toDto(QuestionLegacy entity) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public QuestionLegacy toEntity(QuestionDTO dto) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
