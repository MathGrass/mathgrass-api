package de.tudresden.inf.st.mathgrass.api.admin.authentication;

import de.tudresden.inf.st.mathgrass.api.admin.authentication.entitiy.LoginAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<LoginAuthentication,Long> {


    LoginAuthentication findByEmailAndPassword(String email, String password);

    LoginAuthentication findByEmail(String email);
}


