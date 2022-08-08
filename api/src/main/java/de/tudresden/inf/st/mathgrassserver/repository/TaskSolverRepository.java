package de.tudresden.inf.st.mathgrassserver.repository;

import de.tudresden.inf.st.mathgrassserver.dbmodel.TaskSolverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskSolverRepository extends JpaRepository<TaskSolverEntity,Long> {
}
