package de.tudresden.inf.st.mathgrass.api.task.question.answer;

import de.tudresden.inf.st.mathgrass.api.evaluator.executor.Executor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.IOException;

@Entity
public class DynamicAnswer extends Answer {

    @ManyToOne(cascade = CascadeType.ALL)
    private Executor executor;

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public boolean acceptAnswerVisitor(AnswerVisitor visitor, Long taskId, String userAnswer) throws IOException,
            InterruptedException {
        return visitor.visitDynamicAnswer(this, taskId, userAnswer);
    }

}
