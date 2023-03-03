package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.model.QuestionDTO;
import de.tudresden.inf.st.mathgrass.api.task.question.Question;

public class QuestionTransformer extends ModelTransformer<QuestionDTO, Question>{
    @Override
    public QuestionDTO toDto(Question entity) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Question toEntity(QuestionDTO dto) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
