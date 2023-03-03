package de.tudresden.inf.st.mathgrass.api.label;

import javax.persistence.*;

/**
 * This class represent tags, which can be used to add additional information to graphs.
 */
@Table(name = "tags")
@Entity
public class Label {
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
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
