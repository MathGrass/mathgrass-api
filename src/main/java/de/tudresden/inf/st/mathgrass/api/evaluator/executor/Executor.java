package de.tudresden.inf.st.mathgrass.api.evaluator.executor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Executor {
    public static String DEFAULT_GRAPH_PATH = "/evaluation/graph.json";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SourceFile> sourceFiles = new ArrayList<>();
    private String containerImage;
    private String customEntrypoint;
    private String graphPath = DEFAULT_GRAPH_PATH;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getGraphPath() {
        return graphPath;
    }

    public void setGraphPath(String graphPath) {
        this.graphPath = graphPath;
    }
}
