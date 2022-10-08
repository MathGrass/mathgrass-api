package de.tudresden.inf.st.mathgrassserver.database.repository;

import de.tudresden.inf.st.mathgrassserver.database.entity.TaskTopicEntity;
import de.tudresden.inf.st.mathgrassserver.database.entity.VertexEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VertexRepository extends JpaRepository<VertexEntity,Long>  {
}
