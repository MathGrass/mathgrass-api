package de.tudresden.inf.st.mathgrass.api.graph;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphRepository extends JpaRepository<Graph,Long> {
}
