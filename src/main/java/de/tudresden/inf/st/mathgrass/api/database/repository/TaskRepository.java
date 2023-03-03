package de.tudresden.inf.st.mathgrass.api.database.repository;

import de.tudresden.inf.st.mathgrass.api.database.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity,Long> {

}
