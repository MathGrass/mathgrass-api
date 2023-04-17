package de.tudresden.inf.st.mathgrass.api.admin.authentication;

import de.tudresden.inf.st.mathgrass.api.MathgrassServerApplication;
import de.tudresden.inf.st.mathgrass.api.admin.authentication.entitiy.LoginAuthentication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);
    @Autowired
    private final AuthenticationRepository authenticationRepository;

    public AuthenticationServiceImpl(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public LoginAuthentication fetchUserByEmailAndPassword(String email, String password) {
        logger.info("fetchUserByEmailAndPassword the email {} and the password {}",email,password);
        return authenticationRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public String saveUser(LoginAuthentication loginAuthentication) {
        logger.info("saveUser with the details loginAuthentication ");
        authenticationRepository.save(loginAuthentication);
        return "Saved Successfully";
    }



}
