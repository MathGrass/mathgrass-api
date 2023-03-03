package de.tudresden.inf.st.mathgrass.api.task.hint;

import de.tudresden.inf.st.mathgrass.api.transform.TaskEntity;

import javax.persistence.*;

/**
 * This class represents a hint in a {@link TaskEntity}, which can give information to the tasks answer.
 */
@Table(name = "taskhints")
@Entity
public class Hint {
    /**
     * ID of hint.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Label of hint.
     */
    @Column
    private String label = null;

    /**
     * Content of hint.
     */
    @Column
    private String content = null;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
