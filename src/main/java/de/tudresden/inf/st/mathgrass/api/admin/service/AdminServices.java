package de.tudresden.inf.st.mathgrass.api.admin.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.tudresden.inf.st.mathgrass.api.admin.config.JWTService;
import de.tudresden.inf.st.mathgrass.api.admin.entity.*;
import de.tudresden.inf.st.mathgrass.api.admin.model.UserAuthenticationRequest;
import de.tudresden.inf.st.mathgrass.api.admin.model.UserAuthenticationResponse;
import de.tudresden.inf.st.mathgrass.api.admin.model.UserRegistrationRequest;
import de.tudresden.inf.st.mathgrass.api.admin.repository.HintsCollectionRepository;
import de.tudresden.inf.st.mathgrass.api.admin.repository.QuestionAndAnswerRepository;
import de.tudresden.inf.st.mathgrass.api.admin.repository.UserRepository;
import de.tudresden.inf.st.mathgrass.api.graph.Edge;
import de.tudresden.inf.st.mathgrass.api.graph.Graph;
import de.tudresden.inf.st.mathgrass.api.graph.GraphRepository;
import de.tudresden.inf.st.mathgrass.api.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServices {

    private static final Logger logger = LogManager.getLogger(AdminServices.class);

    private static final String SUCCESS_RESPONSE = "Saved Successfully";
    private final UserRepository repository;
    private final HintsCollectionRepository hintsRepository;

    private final GraphRepository graphRepository;

    private final QuestionAndAnswerRepository questionAndAnswerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    private final AuthenticationManager authenticationManager;

    public AdminServices(UserRepository repository, HintsCollectionRepository hintsRepository, GraphRepository graphRepository, QuestionAndAnswerRepository questionAndAnswerRepository, PasswordEncoder passwordEncoder, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.hintsRepository = hintsRepository;
        this.graphRepository = graphRepository;
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
            logger.error("Error in saving the user data {}",e.getMessage());
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
            logger.error("Error in saving the user data {}",e.getMessage());
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
            logger.error("Error in authenticating the user {}",e.getMessage());
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
            logger.error("Token Expired {}", e.getMessage());
        }
        logger.info(jwtExpiration);
        return jwtExpiration;
    }

    public String saveHintsFromUser(HintsCollectionEntity saveHints) {
        try{
            hintsRepository.save(saveHints);
        }
        catch (Exception e){
            logger.error("Exception occurred in saving the hints {}",e.getMessage());
        }
        return SUCCESS_RESPONSE;
    }

    public String saveQuestionAnswer(QuestionAnswerEntity questionAnswerEntity) {
        try{
            questionAndAnswerRepository.save(questionAnswerEntity);
        }
        catch (Exception e){
            logger.error("Exception occurred in saving the Questions and Answer {}",e.getMessage());
        }
        return SUCCESS_RESPONSE;
    }

    public String buildGraphAndSave(JsonArray cellsArray, String studentLogin) {

        Graph graph = new Graph();
        try{
            List<Vertex> vertices = new ArrayList<>();
            List<Edge> edges = new ArrayList<>();
            for (JsonElement element : cellsArray) {
                JsonObject cellObject = element.getAsJsonObject();
                String cellType = cellObject.get("type").getAsString();

                if (cellType.equals("standard.Rectangle") || cellType.equals("standard.Circle")) {

                    Vertex vertex = new Vertex();
                    JsonObject attrsObject = cellObject.getAsJsonObject("attrs");
                    JsonObject bodyObject = attrsObject.getAsJsonObject("body");
                    JsonObject labelObject = attrsObject.getAsJsonObject("label");
                    JsonObject positionObject = cellObject.getAsJsonObject("position");
                    int x = positionObject.get("x").getAsInt();
                    int y = positionObject.get("y").getAsInt();
                    String stroke = String.valueOf(bodyObject.get("stroke"));
                    String label = String.valueOf(labelObject.get("text"));
                    vertex.setX(x);
                    vertex.setY(y);
                    vertex.setLabel(label);
                    vertex.setVertexId(cellObject.get("id").getAsString());
                    vertex.setVertexStroke(stroke);
                    vertex.setVertexType(cellObject.get("type").getAsString());

                    vertices.add(vertex);
                } else if (cellType.equals("standard.Link")) {
                    // Create a new Edge object
                    Edge edge = new Edge();

                    JsonObject sourceObject = cellObject.getAsJsonObject("source");
                    String sourceId = sourceObject.get("id").getAsString();
                    Vertex sourceVertex = findVertexById(vertices, sourceId);
                    edge.setSourceVertex(sourceVertex);

                    JsonObject targetObject = cellObject.getAsJsonObject("target");
                    String targetId = targetObject.get("id").getAsString();
                    Vertex targetVertex = findVertexById(vertices, targetId);
                    edge.setTargetVertex(targetVertex);
                    JsonArray labelsArray = cellObject.getAsJsonArray("labels");
                    if (labelsArray != null && labelsArray.size() > 0) {
                        JsonObject labelObject = labelsArray.get(0).getAsJsonObject()
                                .getAsJsonObject("attrs")
                                .getAsJsonObject("text");
                        String labelText = labelObject.get("text").getAsString();
                        edge.setLabel(labelText);
                    }
                    edge.setEdgeId(cellObject.get("id").getAsString());
                    edges.add(edge);
                }
            }
            graph.setVertices(vertices);
            graph.setEdges(edges);
            graph.setIsStudentLoggedId(studentLogin);
            graphRepository.save(graph);
            logger.error(graph.getVertices());
        }
        catch (Exception e){
            logger.error("Exception in json parsing {}",e.getMessage());

        }
        return String.valueOf(graph.getId());
    }
    private static Vertex findVertexById(List<Vertex> vertices, String sourceId) {
        for (Vertex vertex : vertices) {
            if (vertex.getVertexId().equals(sourceId)) {
                return vertex;
            }
        }
        return null;
    }

    public String deleteSaveGraphById(Long id) {

        try{
            graphRepository.deleteById(id);
        }
        catch (Exception e){
            logger.error("Exception occurred in Deleting the Graph JSON {}",e.getMessage());
        }

        return "Deleted Successfully";
    }


    public List<HintsCollectionEntity> getAllHints() {

        return  hintsRepository.findAll();
    }

    public List<QuestionAnswerEntity> getAllQuestionAndAnswer() {
        return  questionAndAnswerRepository.findAll();
    }


    public List<Graph> getGraphJson() {

        return  graphRepository.findAll();
    }
}
