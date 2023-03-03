package de.tudresden.inf.st.mathgrass.api.database.repository;

import de.tudresden.inf.st.mathgrass.api.database.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity,Long> {
}
