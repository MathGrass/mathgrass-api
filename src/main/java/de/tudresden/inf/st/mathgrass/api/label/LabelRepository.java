package de.tudresden.inf.st.mathgrass.api.label;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends JpaRepository<LabelEntity,Long> {
}
