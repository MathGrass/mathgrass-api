package de.tudresden.inf.st.mathgrass.api.evaluator.sage;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Set;

@Profile("sage-evaluator")
@Component
public class SageEvaluator {
    public static final String SAGE_EVALUATOR_IMAGE_NAME = "sage-evaluator";
    private static final Logger logger = LogManager.getLogger(SageEvaluator.class);
    private final DockerClient dockerClient;


    public SageEvaluator(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @PostConstruct
    public void buildSageImage() {
        logger.info("Building sage-evaluator image. This might take some time..");
        BuildImageResultCallback buildImageResultCallback = new BuildImageResultCallback();
        BuildImageCmd buildImageCmd =
                dockerClient.buildImageCmd(new File("./evaluators/sage-evaluator/Dockerfile")).withTags(Set.of(SAGE_EVALUATOR_IMAGE_NAME));
        buildImageCmd.exec(buildImageResultCallback);
        buildImageResultCallback.awaitImageId();
        System.out.println();
    }
}
