package de.tudresden.inf.st.mathgrassserver.dbmodel;


import javax.persistence.*;

@Table
@Entity(name = "tasksolver")
public class TaskSolverEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    String label;

    @Column
    String executionDescriptor;

    public TaskSolverEntity() {

    }

    public TaskSolverEntity(String label, String executionDescriptor) {
        this.label = label;
        this.executionDescriptor = executionDescriptor;
    }



    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }




}
