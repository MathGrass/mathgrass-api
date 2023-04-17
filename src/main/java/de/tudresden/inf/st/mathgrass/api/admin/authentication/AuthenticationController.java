package de.tudresden.inf.st.mathgrass.api.admin.authentication;

import de.tudresden.inf.st.mathgrass.api.admin.authentication.entitiy.LoginAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/authentication")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/get-auth")
    public LoginAuthentication fetchLoginDetails (@RequestParam("email")String email,@RequestParam("password")String password){
        return authenticationService.fetchUserByEmailAndPassword(email,password);
    }

    @PostMapping("/save-auth")
    public String saveLoginDetails (@RequestBody LoginAuthentication loginAuthentication){
        return authenticationService.saveUser(loginAuthentication);
    }
}
