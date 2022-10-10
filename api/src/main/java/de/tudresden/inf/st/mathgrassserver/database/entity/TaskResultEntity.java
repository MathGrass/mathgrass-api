package de.tudresden.inf.st.mathgrassserver.database.entity;

import de.tudresden.inf.st.mathgrassserver.model.Task;

import javax.persistence.*;

@Table(name = "tasksolvers")
@Entity
public class TaskResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private TaskEntity task;

    @Column
    private String answer;

    @Column
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
