package de.tudresden.inf.st.mathgrass.api.admin.model;

public class UserAuthenticationResponse {

    private String token;
    private String userType;

    public UserAuthenticationResponse(String token, String userType) {
        this.token = token;
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "token='" + token + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public UserAuthenticationResponse(String token) {
        this.token = token;
    }

    public UserAuthenticationResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
