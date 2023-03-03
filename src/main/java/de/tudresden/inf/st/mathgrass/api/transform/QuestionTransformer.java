package de.tudresden.inf.st.mathgrass.api.transform;

import de.tudresden.inf.st.mathgrass.api.model.Question;

public class QuestionTransformer extends ModelTransformer<Question, QuestionEntity>{
    @Override
    public Question toDto(QuestionEntity entity) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public QuestionEntity toEntity(Question dto) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
