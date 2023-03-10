package de.tudresden.inf.st.mathgrass.api.task.question.answer;


import javax.persistence.*;

@Entity
public abstract class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }
}
