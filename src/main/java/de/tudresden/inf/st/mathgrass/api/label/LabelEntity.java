package de.tudresden.inf.st.mathgrass.api.label;

import javax.persistence.*;

/**
 * This class represent tags, which can be used to add additional information to graphs.
 */
@Table(name = "tags")
@Entity
public class LabelEntity {
    /**
     * ID of tag.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Label of tag.
     */
    @Column
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
