package de.tudresden.inf.st.mathgrassserver.database.entity;

import javax.persistence.*;

@Table
@Entity(name = "inputanswerentity")
public class InputAnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
