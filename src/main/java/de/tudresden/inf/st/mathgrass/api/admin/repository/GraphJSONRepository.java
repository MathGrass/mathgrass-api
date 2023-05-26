package de.tudresden.inf.st.mathgrass.api.admin.repository;

import de.tudresden.inf.st.mathgrass.api.admin.entity.GraphEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GraphJSONRepository extends JpaRepository<GraphEntity,Long> {
}
