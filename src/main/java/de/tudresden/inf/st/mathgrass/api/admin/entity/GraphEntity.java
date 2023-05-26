package de.tudresden.inf.st.mathgrass.api.admin.entity;

import javax.persistence.*;


@Entity
@Table(name = "graph_data")
public class GraphEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "CLOB")
    private String graphData;

    public GraphEntity() {
    }

    @Override
    public String toString() {
        return "GraphEntity{" +
                "id=" + id +
                ", graphData='" + graphData + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGraphData() {
        return graphData;
    }

    public void setGraphData(String graphData) {
        this.graphData = graphData;
    }

    public GraphEntity(Long id, String graphData) {
        this.id = id;
        this.graphData = graphData;
    }
}
