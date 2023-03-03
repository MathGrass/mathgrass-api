package de.tudresden.inf.st.mathgrass.api.database.repository;

import de.tudresden.inf.st.mathgrass.api.database.entity.TaskCollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCollectionRepository extends JpaRepository<TaskCollectionEntity,Long> {
}
