package de.tudresden.inf.st.mathgrass.api.graph;

import de.tudresden.inf.st.mathgrass.api.graph.GraphEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphRepository extends JpaRepository<GraphEntity,Long> {
}
