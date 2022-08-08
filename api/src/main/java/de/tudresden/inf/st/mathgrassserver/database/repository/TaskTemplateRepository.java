package de.tudresden.inf.st.mathgrassserver.database.repository;

import de.tudresden.inf.st.mathgrassserver.database.entity.TaskTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTemplateRepository extends JpaRepository<TaskTemplateEntity,Long> {
}
