package de.tudresden.inf.st.mathgrassserver.database.entity;


import javax.persistence.*;

@Table
@Entity(name = "tasksolver")
public class TaskSolverEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getExecutionDescriptor() {
        return executionDescriptor;
    }

    public void setExecutionDescriptor(String executionDescriptor) {
        this.executionDescriptor = executionDescriptor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }




}
