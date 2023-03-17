package de.tudresden.inf.st.mathgrass.api.task.question.answer;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.IOException;

@Entity
public abstract class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public abstract boolean acceptAnswerVisitor(AnswerVisitor visitor, Long taskId, String userAnswer) throws IOException,
            InterruptedException;
}
