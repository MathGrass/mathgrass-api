package de.tudresden.inf.st.mathgrass.api.task.question.answer;


import javax.persistence.*;
import java.io.IOException;

@Entity
public abstract class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public abstract boolean acceptAnswerVisitor(AnswerVisitor visitor, String userAnswer) throws IOException;
}
