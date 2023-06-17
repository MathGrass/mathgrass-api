package de.tudresden.inf.st.mathgrass.api.admin;

import de.tudresden.inf.st.mathgrass.api.admin.model.UserRegistrationRequest;
import de.tudresden.inf.st.mathgrass.api.admin.service.AdminServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RegistraionDefault implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(RegistraionDefault.class);

    private final AdminServices adminServices;

    public RegistraionDefault(AdminServices adminServices) {
        this.adminServices = adminServices;
    }
    @Override
    public void run(String... args) throws Exception {
        UserRegistrationRequest adminRequest = new UserRegistrationRequest("Test", "Admin", "test.admin@tu-dresden.de", "admin");
        adminServices.adminRegistration(adminRequest);
        logger.info("Data Initialized for admin Registration - email - {} and password - {}",adminRequest.getEmail(),adminRequest.getPassword());

        UserRegistrationRequest studentRequest = new UserRegistrationRequest("Test", "Student", "test.student@tu-dresden.de", "student");
        adminServices.studentRegistration(studentRequest);
        logger.info("Data Initialized for Student Registration - email - {} and password - {}",studentRequest.getEmail(),studentRequest.getPassword());

    }
}
