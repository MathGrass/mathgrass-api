package de.tudresden.inf.st.mathgrass.api.feedback;

import de.tudresden.inf.st.mathgrass.api.feedback.TaskResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskResultRepository extends JpaRepository<TaskResultEntity,Long> {
}
