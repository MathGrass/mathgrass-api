package de.tudresden.inf.st.mathgrassserver.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExercisingIntegrationConfig {

    @Bean
    public GraphApi graphApi() {
        return new GraphApi();
    }
}