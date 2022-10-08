package de.tudresden.inf.st.mathgrassserver.database.repository;

import de.tudresden.inf.st.mathgrassserver.database.entity.TaskCollectionEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskTopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTopicRepository extends JpaRepository<TaskTopicEntity,Long> {
}
