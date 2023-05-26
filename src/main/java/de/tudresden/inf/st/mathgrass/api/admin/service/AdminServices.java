package de.tudresden.inf.st.mathgrass.api.admin.service;

import de.tudresden.inf.st.mathgrass.api.admin.config.JWTService;
import de.tudresden.inf.st.mathgrass.api.admin.entity.*;
import de.tudresden.inf.st.mathgrass.api.admin.model.UserAuthenticationRequest;
import de.tudresden.inf.st.mathgrass.api.admin.model.UserAuthenticationResponse;
import de.tudresden.inf.st.mathgrass.api.admin.model.UserRegistrationRequest;
import de.tudresden.inf.st.mathgrass.api.admin.repository.GraphJSONRepository;
import de.tudresden.inf.st.mathgrass.api.admin.repository.HintsCollectionRepository;
import de.tudresden.inf.st.mathgrass.api.admin.repository.QuestionAndAnswerRepository;
import de.tudresden.inf.st.mathgrass.api.admin.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServices {

    private static final Logger logger = LogManager.getLogger(AdminServices.class);

    private static final String SUCCESS_RESPONSE = "Saved Successfully";
    private final UserRepository repository;
    private final HintsCollectionRepository hintsRepository;

    private final GraphJSONRepository graphJSONRepository;

    private final QuestionAndAnswerRepository questionAndAnswerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    private final AuthenticationManager authenticationManager;

    public AdminServices(UserRepository repository, HintsCollectionRepository hintsRepository, GraphJSONRepository graphJSONRepository, QuestionAndAnswerRepository questionAndAnswerRepository, PasswordEncoder passwordEncoder, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.hintsRepository = hintsRepository;
        this.graphJSONRepository = graphJSONRepository;
        this.questionAndAnswerRepository = questionAndAnswerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public UserAuthenticationResponse adminRegistration(UserRegistrationRequest request) {
        User user = new User();
        UserAuthenticationResponse registerResponse = new UserAuthenticationResponse();
        try{
            user.setFirstname(request.getFirstName());
            user.setLastname(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.ADMIN);
            repository.save(user);
            var jwtToken = jwtService.generateToken(user);
            registerResponse.setToken(jwtToken);

         }
        catch (Exception e){
            logger.error("Error in saving the user data {}",e.getMessage().toString());
        }
        return registerResponse;

    }

    public UserAuthenticationResponse studentRegistration(UserRegistrationRequest request) {
        User user = new User();
        UserAuthenticationResponse registerResponse = new UserAuthenticationResponse();
        try{
            user.setFirstname(request.getFirstName());
            user.setLastname(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.STUDENT);
            repository.save(user);
            var jwtToken = jwtService.generateToken(user);
            registerResponse.setToken(jwtToken);

        }
        catch (Exception e){
            logger.error("Error in saving the user data {}",e.getMessage().toString());
        }
        return registerResponse;

    }

    public UserAuthenticationResponse userAuthentication(UserAuthenticationRequest request) {

        UserAuthenticationResponse authResponse = new UserAuthenticationResponse();

        try{
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
            User user = repository.findByEmail(request.getEmail())
                    .orElseThrow();

            var jwtToken = jwtService.generateToken(user);
            authResponse.setUserType(user.getRole().toString());
            authResponse.setToken(jwtToken);

        }
        catch (Exception e){
            logger.error("Error in authenticating the user {}",e.getMessage().toString());
        }

        return authResponse;
    }

    public boolean checkTokenValidity(String token) {
        boolean jwtExpiration;
        try{
            jwtExpiration = jwtService.isTokenExpired(token);
        }
        catch (Exception e){
            jwtExpiration = true;
            logger.error("Token Expired {}", e.getMessage().toString());
        }
        logger.info(jwtExpiration);
        return jwtExpiration;
    }

    public String saveHintsFromUser(HintsCollectionEntity saveHints) {
        try{
                hintsRepository.save(saveHints);
        }
        catch (Exception e){
            logger.error("Exception occurred in saving the hints {}",e.getMessage().toString());
        }
        return SUCCESS_RESPONSE;
    }

    public String saveQuestionAnswer(QuestionAnswerEntity questionAnswerEntity) {
        try{
            questionAndAnswerRepository.save(questionAnswerEntity);
        }
        catch (Exception e){
            logger.error("Exception occurred in saving the Questions and Answer {}",e.getMessage().toString());
        }
        return SUCCESS_RESPONSE;
    }

    public String saveGraphJson(GraphEntity graphEntity) {

        try{
            graphJSONRepository.save(graphEntity);
        }
        catch (Exception e){
            logger.error("Exception occurred in saving the Graph JSON {}",e.getMessage().toString());
        }

        return SUCCESS_RESPONSE;
    }


    public List<HintsCollectionEntity> getAllHints() {

        return  hintsRepository.findAll();
    }

    public List<QuestionAnswerEntity> getAllQuestionAndAnswer() {
        return  questionAndAnswerRepository.findAll();
    }


    public String getGraphJson() {
        List<GraphEntity> graphEntities = graphJSONRepository.findAll();
        GraphEntity graphEntity = graphEntities.get(0);
        String graphData = graphEntity.getGraphData();
        graphData = graphData.replaceAll("\\\\","");
        return  graphData;
    }
}
