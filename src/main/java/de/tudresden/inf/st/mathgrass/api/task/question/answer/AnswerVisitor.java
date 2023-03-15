package de.tudresden.inf.st.mathgrass.api.task.question.answer;

public class AnswerVisitor {
    public String visitStaticAnswer(StaticAnswer answer){
        return answer.getAnswer();
    }

    public String visitDynamicAnswer(DynamicAnswer answer){
        return "doing something with the dynamiy answer";
    }
}
