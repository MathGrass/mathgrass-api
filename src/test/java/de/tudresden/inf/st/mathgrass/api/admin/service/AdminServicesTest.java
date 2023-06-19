package de.tudresden.inf.st.mathgrass.api.admin.service;

import de.tudresden.inf.st.mathgrass.api.admin.model.TokenValidationRequest;
import de.tudresden.inf.st.mathgrass.api.admin.model.UserAuthenticationRequest;
import de.tudresden.inf.st.mathgrass.api.admin.model.UserAuthenticationResponse;
import de.tudresden.inf.st.mathgrass.api.admin.model.UserRegistrationRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(profiles = "dev")
class AdminServicesTest {

    private static final Logger logger = LogManager.getLogger(AdminServicesTest.class);

    @Autowired
    AdminServices adminServices;

    @Test
    public void runAdminTests(){
        try {
            UserRegistrationRequest adminRequest = new UserRegistrationRequest("TestSpring", "Test", "test.spring@tu-dresden.de", "spring");
            UserAuthenticationResponse registrationResponse = adminServices.adminRegistration(adminRequest);
            logger.info("Admin Registration Completed");
            if(!registrationResponse.getToken().equals("")){
                try {
                    UserAuthenticationRequest authenticationRequest = new UserAuthenticationRequest("test.spring@tu-dresden.de", "spring");
                    UserAuthenticationResponse authResponse = adminServices.userAuthentication(authenticationRequest);
                    logger.info("User Authentication Completed and the Returned with Token - {}",authResponse.getToken());
                    if(!authResponse.getToken().equals("")){
                        try {
                            TokenValidationRequest tokenValidationRequest = new TokenValidationRequest(authResponse.getToken());
                            boolean validityResponse = adminServices.checkTokenValidity(tokenValidationRequest.getToken());
                            logger.info("Is Token Expired - {}", validityResponse);
                        }catch (Exception e){
                            logger.error("Exception inside Checking Token Validity - {}",e.getMessage());
                        }

                    }
                }catch(Exception e){
                    logger.error("Exception inside User Authentication - {}",e.getMessage());
                }
            }
        }
        catch(Exception e){
            logger.error("Exception inside Admin Registration - {}",e.getMessage());
        }
    }

}