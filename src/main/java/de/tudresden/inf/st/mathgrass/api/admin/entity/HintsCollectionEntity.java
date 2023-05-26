package de.tudresden.inf.st.mathgrass.api.admin.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "hints_collection")
public class HintsCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<Hint> textHints;

    @ElementCollection
    private List<Hint> scriptHints;

    @ElementCollection
    private List<Hint> graphicalHints;

    public HintsCollectionEntity() {
    }

    @Override
    public String toString() {
        return "HintsCollectionEntity{" +
                "id=" + id +
                ", textHints=" + textHints +
                ", scriptHints=" + scriptHints +
                ", graphicalHints=" + graphicalHints +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Hint> getTextHints() {
        return textHints;
    }

    public void setTextHints(List<Hint> textHints) {
        this.textHints = textHints;
    }

    public List<Hint> getScriptHints() {
        return scriptHints;
    }

    public void setScriptHints(List<Hint> scriptHints) {
        this.scriptHints = scriptHints;
    }

    public List<Hint> getGraphicalHints() {
        return graphicalHints;
    }

    public void setGraphicalHints(List<Hint> graphicalHints) {
        this.graphicalHints = graphicalHints;
    }

    public HintsCollectionEntity(Long id, List<Hint> textHints, List<Hint> scriptHints, List<Hint> graphicalHints) {
        this.id = id;
        this.textHints = textHints;
        this.scriptHints = scriptHints;
        this.graphicalHints = graphicalHints;
    }
}
