package de.tudresden.inf.st.mathgrassserver.transform;

import de.tudresden.inf.st.mathgrassserver.database.entity.QuestionEntity;
import de.tudresden.inf.st.mathgrassserver.model.Question;

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
