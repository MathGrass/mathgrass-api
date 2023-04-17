package de.tudresden.inf.st.mathgrass.api.admin.authentication;

import de.tudresden.inf.st.mathgrass.api.admin.authentication.entitiy.LoginAuthentication;

import java.util.List;

public interface AuthenticationService {

    public LoginAuthentication fetchUserByEmailAndPassword(String email,String password);


    public String saveUser(LoginAuthentication loginAuthentication);
}
