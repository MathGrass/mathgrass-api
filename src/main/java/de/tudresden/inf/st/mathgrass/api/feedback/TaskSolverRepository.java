package de.tudresden.inf.st.mathgrass.api.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskSolverRepository extends JpaRepository<TaskSolver,Long> {
}
