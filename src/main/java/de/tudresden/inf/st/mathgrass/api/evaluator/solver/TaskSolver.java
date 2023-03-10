package de.tudresden.inf.st.mathgrass.api.evaluator.solver;

import javax.persistence.*;

/**
 * This class represents a task solver, containing an execution descriptor which can be used to evaluate a task result.
 */
@Table(name = "tasksolvers")
@Entity
public class TaskSolver {
    /**
     * ID of task solver.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;



    /**
     * Label of task solver.
     */
    @Column
    String label;

    /**
     * Execution script.
     */
    @Column
    String executionDescriptor;

    public TaskSolver() {

    }

    public TaskSolver(String label, String executionDescriptor) {
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
