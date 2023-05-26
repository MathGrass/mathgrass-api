package de.tudresden.inf.st.mathgrass.api.admin.model;

public class TokenValidationRequest {

    private String token;

    public TokenValidationRequest(String token) {
        this.token = token;
    }

    public TokenValidationRequest() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenValidationRequest{" +
                "token='" + token + '\'' +
                '}';
    }
}
