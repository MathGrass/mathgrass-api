package de.tudresden.inf.st.mathgrass.api.feedback;

import de.tudresden.inf.st.mathgrass.api.feedback.TaskSolverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskSolverRepository extends JpaRepository<TaskSolverEntity,Long> {
}
