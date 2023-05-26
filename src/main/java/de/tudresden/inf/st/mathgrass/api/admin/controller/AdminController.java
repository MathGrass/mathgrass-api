package de.tudresden.inf.st.mathgrass.api.admin.controller;


import de.tudresden.inf.st.mathgrass.api.admin.entity.GraphEntity;
import de.tudresden.inf.st.mathgrass.api.admin.entity.HintsCollectionEntity;
import de.tudresden.inf.st.mathgrass.api.admin.entity.QuestionAnswerEntity;
import de.tudresden.inf.st.mathgrass.api.admin.model.TokenValidationRequest;
import de.tudresden.inf.st.mathgrass.api.admin.model.UserAuthenticationRequest;
import de.tudresden.inf.st.mathgrass.api.admin.model.UserAuthenticationResponse;
import de.tudresden.inf.st.mathgrass.api.admin.model.UserRegistrationRequest;
import de.tudresden.inf.st.mathgrass.api.admin.service.AdminServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final Logger logger = LogManager.getLogger(AdminController.class);

    private final AdminServices adminServices;


    public AdminController(AdminServices adminServices) {
        this.adminServices = adminServices;
    }

    @PostMapping("/adminRegistration")
    public ResponseEntity<UserAuthenticationResponse> adminRegistration(@RequestBody UserRegistrationRequest request){
        logger.info("adminRegistration Service called from AdminController");
        return ResponseEntity.ok(adminServices.adminRegistration(request));
    }

    @PostMapping("/studentRegistration")
    public ResponseEntity<UserAuthenticationResponse> studentRegistration(@RequestBody UserRegistrationRequest request){
        logger.info("studentRegistration Service called from AdminController");
        return ResponseEntity.ok(adminServices.studentRegistration(request));
    }

    @PostMapping("/userAuthentication")
    public ResponseEntity<UserAuthenticationResponse> userAuthentication(@RequestBody UserAuthenticationRequest request){
        logger.info("userAuthentication Service called from AdminController");
        return ResponseEntity.ok(adminServices.userAuthentication(request));
    }

    @PostMapping("/tokenValidity")
    public boolean checkTokenValidity(@RequestBody TokenValidationRequest request){
        logger.info("checkTokenValidity Service called from AdminController with Input {}",request);
        return adminServices.checkTokenValidity(request.getToken());
    }
    @PostMapping("/saveGraph")
    public String saveGraphJson(@RequestBody String graphJson) {
        logger.info("saveGraphJson Service called from AdminController with Input {}",graphJson);
        GraphEntity graphEntity = new GraphEntity();
        graphEntity.setGraphData(graphJson);
        return adminServices.saveGraphJson(graphEntity);
     }

    @PostMapping("/saveHints")
    public String saveHintsFromUser(@RequestBody HintsCollectionEntity hintsCollectionEntity){
        logger.info("saveHintsFromUser Service called from AdminController with Input {}",hintsCollectionEntity);
        return adminServices.saveHintsFromUser(hintsCollectionEntity);
    }

    @PostMapping("/saveQuestionAnswer")
    public String saveQuestionAnswer(@RequestBody QuestionAnswerEntity questionAnswerEntity) {
        logger.info("saveQuestionAnswer Service called from AdminController with Input {}",questionAnswerEntity);
        return adminServices.saveQuestionAnswer(questionAnswerEntity);
//        ObjectMapper mapper = new ObjectMapper();
//        QuestionAnswerEntity question = mapper.readValue(questionAnswerEntity, QuestionAnswerEntity.class);
//        System.out.println("Check the mapped json - "+question);
    }

    @GetMapping("/getHints")
    public List<HintsCollectionEntity> getAllHints(){
        return adminServices.getAllHints();
    }

    @GetMapping("/getAllQuestionAndAnswer")
    public List<QuestionAnswerEntity> getAllQuestionAndAnswer(){
        return adminServices.getAllQuestionAndAnswer();
    }

    @GetMapping("/getGraphJson")
    public String getGraphJson(){
        return adminServices.getGraphJson();
    }


}
