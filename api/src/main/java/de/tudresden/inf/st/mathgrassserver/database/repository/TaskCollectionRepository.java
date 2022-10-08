package de.tudresden.inf.st.mathgrassserver.database.repository;

import de.tudresden.inf.st.mathgrassserver.database.entity.FeedbackEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.TaskCollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCollectionRepository extends JpaRepository<TaskCollectionEntity,Long> {
}
