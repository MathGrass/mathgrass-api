package de.tudresden.inf.st.mathgrass.api.feedback;

import de.tudresden.inf.st.mathgrass.api.feedback.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity,Long> {
}
