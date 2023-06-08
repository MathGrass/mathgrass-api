package de.tudresden.inf.st.mathgrass.api.admin.model;

public class SaveGraphRequest {


    private String graphJSON;
    private String studentLogin;

    public SaveGraphRequest() {
    }

    public SaveGraphRequest(String graphJSON, String studentLogin) {
        this.graphJSON = graphJSON;
        this.studentLogin = studentLogin;
    }

    @Override
    public String toString() {
        return "SaveGraphRequest{" +
                "graphJSON='" + graphJSON + '\'' +
                ", studentLogin='" + studentLogin + '\'' +
                '}';
    }

    public String getGraphJSON() {
        return graphJSON;
    }

    public void setGraphJSON(String graphJSON) {
        this.graphJSON = graphJSON;
    }

    public String getStudentLogin() {
        return studentLogin;
    }

    public void setStudentLogin(String studentLogin) {
        this.studentLogin = studentLogin;
    }

}
