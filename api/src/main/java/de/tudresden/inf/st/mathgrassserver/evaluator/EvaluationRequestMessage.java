package de.tudresden.inf.st.mathgrassserver.evaluator;

public class EvaluationRequestMessage {
    Long requestId;
    Long taskId;
    String inputAnswer;


    public EvaluationRequestMessage(Long requestId, Long taskId, String inputAnswer) {
        this.requestId = requestId;
        this.taskId = taskId;
        this.inputAnswer = inputAnswer;
    }
}
