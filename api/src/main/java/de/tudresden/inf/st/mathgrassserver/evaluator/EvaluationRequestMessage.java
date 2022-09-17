package de.tudresden.inf.st.mathgrassserver.evaluator;

public class EvaluationRequestMessage {
    Long requestId;
    Long taskSolverId;
    String inputAnswer;


    public EvaluationRequestMessage(Long requestId, Long taskSolverId, String inputAnswer) {
        this.requestId = requestId;
        this.taskSolverId = taskSolverId;
        this.inputAnswer = inputAnswer;
    }
}
