package de.tudresden.inf.st.mathgrass.api.admin.RestAPI;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.tudresden.inf.st.mathgrass.api.admin.entity.HintsCollectionEntity;
import de.tudresden.inf.st.mathgrass.api.admin.entity.QuestionAnswerEntity;
import de.tudresden.inf.st.mathgrass.api.admin.model.*;
import de.tudresden.inf.st.mathgrass.api.admin.service.AdminServices;
import de.tudresden.inf.st.mathgrass.api.graph.Graph;
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
    public String saveGraphJson(@RequestBody SaveGraphRequest saveGraphRequest) {
        logger.info("saveGraphJson Service called from AdminController with Input {} Student Login {}",saveGraphRequest.getGraphJSON(),saveGraphRequest.getStudentLogin());
        try{
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(saveGraphRequest.getGraphJSON()).getAsJsonObject();
            JsonArray cellsArray = jsonObject.getAsJsonArray("cells");
            String saveResponse = adminServices.buildGraphAndSave(cellsArray,saveGraphRequest.getStudentLogin());
            logger.info("The result of the save {}",saveResponse);
            return saveResponse;
        }
        catch (Exception e){
            logger.error("Json parsing exception {}",e.getMessage());
        }
        return "Saved Successfully";
    }

    @DeleteMapping("deleteGraphById/{id}")
    public String cancelSavingConfirmation(@PathVariable Long id){
        logger.info("Graph saving is cancelled by the user cancelSavingConfirmation by id {}",id);
        return adminServices.deleteSaveGraphById(id);
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
    public List<Graph> getGraphJson(){
        return adminServices.getGraphJson();
    }


}
