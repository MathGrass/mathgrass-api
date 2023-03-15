package de.tudresden.inf.st.mathgrass.api.task.question.answer;

public class AnswerVisitor {
    public boolean visitStaticAnswer(StaticAnswer answer, String userAnswer){
        return answer.getAnswer().equals(userAnswer);
    }

    public boolean visitDynamicAnswer(DynamicAnswer answer, String userAnswer){
        // start evaluation and run
        return false;
    }
}
