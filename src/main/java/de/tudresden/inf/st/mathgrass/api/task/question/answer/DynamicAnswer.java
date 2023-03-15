package de.tudresden.inf.st.mathgrass.api.task.question.answer;

import de.tudresden.inf.st.mathgrass.api.evaluator.executor.Executor;

import javax.persistence.*;

@Entity
public class DynamicAnswer extends Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Executor executor;

    public Long getId() {
        return id;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public String acceptVisitor(AnswerVisitor visitor) {
        return visitor.visitDynamicAnswer(this);
    }

}
