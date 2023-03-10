package de.tudresden.inf.st.mathgrass.api.evaluator.executor;

import de.tudresden.inf.st.mathgrass.api.label.Label;

import javax.persistence.*;
import java.util.List;

@Entity
public class Executor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToMany
    List<Label> labels;

    private String containerImage;
    private String sourcePath;
    private String customEntrypoint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
