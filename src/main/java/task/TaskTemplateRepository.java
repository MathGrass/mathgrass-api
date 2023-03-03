package task;

import de.tudresden.inf.st.mathgrass.api.transform.TaskTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTemplateRepository extends JpaRepository<TaskTemplateEntity,Long> {
}
