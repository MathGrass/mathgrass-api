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

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Label> labels;
    @OneToMany(cascade = CascadeType.ALL)
    private List<SourceFile> sourceFiles;
    private String containerImage;
    private String customEntrypoint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public String getContainerImage() {
        return containerImage;
    }

    public void setContainerImage(String containerImage) {
        this.containerImage = containerImage;
    }

    public String getCustomEntrypoint() {
        return customEntrypoint;
    }

    public void setCustomEntrypoint(String customEntrypoint) {
        this.customEntrypoint = customEntrypoint;
    }

    public List<SourceFile> getSourceFiles() {
        return sourceFiles;
    }

    public void setSourceFiles(List<SourceFile> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }
}
