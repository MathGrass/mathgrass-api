package de.tudresden.inf.st.mathgrassserver.database.repository;

import de.tudresden.inf.st.mathgrassserver.database.entity.TaskCollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCollectionRepository extends JpaRepository<TaskCollectionEntity,Long> {
}
