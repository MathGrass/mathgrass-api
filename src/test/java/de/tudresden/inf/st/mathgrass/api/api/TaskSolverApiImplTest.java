package de.tudresden.inf.st.mathgrass.api.api;

import de.tudresden.inf.st.mathgrass.api.feedback.TaskSolverApiImpl;
import de.tudresden.inf.st.mathgrass.api.feedback.TaskSolverEntity;
import de.tudresden.inf.st.mathgrass.api.feedback.TaskSolverRepository;
import de.tudresden.inf.st.mathgrass.api.model.TaskSolver;
import de.tudresden.inf.st.mathgrass.api.transform.TaskSolverTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static de.tudresden.inf.st.mathgrass.api.api.TestHelper.getExampleTaskSolver;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TaskSolverApiImplTest {

    TestHelper testHelper;

    @Autowired
    TaskSolverRepository taskSolverRepository;

    @Autowired
    TaskSolverApiImpl taskSolverApiImpl;

    @BeforeEach
    void setUp() {
        TestHelper testHelper = new TestHelper();
    }

    @Test
    void createTaskSolver() {
        //create
        TaskSolver taskSolver = getExampleTaskSolver();
        long taskSolverId = taskSolverApiImpl.createTaskSolver(taskSolver).getBody();

        //check
        TaskSolverEntity taskSolverEntity = taskSolverRepository.findById(taskSolverId).orElse(null);

        assertNotNull(taskSolverEntity);
        assertEquals(taskSolver.getLabel(), taskSolverEntity.getLabel());
        assertEquals(taskSolver.getExecutionDescriptor(), taskSolverEntity.getExecutionDescriptor());


    }

    @Test
    void getTaskSolverById() {
        //create
        TaskSolver taskSolver = getExampleTaskSolver();
        long taskSolverId = taskSolverRepository.save(new TaskSolverTransformer().toEntity(taskSolver)).getId();

        //get
        TaskSolverEntity taskSolverEntity = taskSolverRepository.findById(taskSolverId).orElse(null);

        //check
        assertNotNull(taskSolverEntity);
        assertEquals(taskSolver.getLabel(), taskSolverEntity.getLabel());
        assertEquals(taskSolver.getExecutionDescriptor(), taskSolverEntity.getExecutionDescriptor());
    }
}
