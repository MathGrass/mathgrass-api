package de.tudresden.inf.st.mathgrass.api.evaluator.sage;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.model.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.Set;

@Profile("sage-evaluator")
@Component
public class SageEvaluator {
    private static final Logger logger = LogManager.getLogger(SageEvaluator.class);
    public static final String SAGE_EVALUATOR_IMAGE_NAME = "sage-evaluator";
    public static final String SAGE_EVALUATOR_TAG = "0.1c";
    public static final String SAGE_IMAGE_COMPLETE_TAG = SAGE_EVALUATOR_IMAGE_NAME + ":" + SAGE_EVALUATOR_TAG;
    public static final String SAGE_EVALUATOR_DOCKERFILE_PATH = "./evaluators/sage-evaluator/Dockerfile";
    private final DockerClient dockerClient;


    public SageEvaluator(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @PostConstruct
    public void buildSageImage() {
        ListImagesCmd listImagesCmd = dockerClient.listImagesCmd().withImageNameFilter(SAGE_IMAGE_COMPLETE_TAG);
        listImagesCmd.getFilters().put("reference", List.of(SAGE_IMAGE_COMPLETE_TAG));
        List<Image> filterImages = listImagesCmd.exec();
        if(!filterImages.isEmpty()){
            logger.info("Sage Evaluator image found ({}), skipping container build", SAGE_IMAGE_COMPLETE_TAG);
            return;
        }
        logger.info("Building sage-evaluator image. This might take some time..");
        BuildImageResultCallback buildImageResultCallback = new BuildImageResultCallback();
        BuildImageCmd buildImageCmd =
                dockerClient.buildImageCmd(new File(SAGE_EVALUATOR_DOCKERFILE_PATH)).withTags(Set.of(SAGE_IMAGE_COMPLETE_TAG));
        buildImageCmd.exec(buildImageResultCallback);
        buildImageResultCallback.awaitImageId();
    }
}
