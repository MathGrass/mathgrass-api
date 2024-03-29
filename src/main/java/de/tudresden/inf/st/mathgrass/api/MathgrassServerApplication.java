package de.tudresden.inf.st.mathgrass.api;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootApplication
@EnableWebMvc
public class MathgrassServerApplication {
    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger(MathgrassServerApplication.class);

    public static void main(String[] args) {
        // run spring application
        SpringApplication.run(MathgrassServerApplication.class, args);
        logger.info("Spring application started!");

    }

    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }

    @Bean
    public DockerClient dockerClient() {
        var config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerTlsVerify(false)
                .build();

        var apacheHttpClient = new ApacheDockerHttpClient.Builder()
                .sslConfig(config.getSSLConfig())
                .dockerHost(config.getDockerHost())
                .build();

        return DockerClientImpl.getInstance(config, apacheHttpClient);
    }
}
