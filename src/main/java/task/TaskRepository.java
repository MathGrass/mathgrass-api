package task;

import de.tudresden.inf.st.mathgrass.api.transform.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity,Long> {

}
