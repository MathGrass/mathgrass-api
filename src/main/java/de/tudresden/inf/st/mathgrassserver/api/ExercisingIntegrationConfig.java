package de.tudresden.inf.st.mathgrassserver.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExercisingApi {

    @Bean
    public PetApi graphApi() {
        return new GraphApi();
    }

    @Bean
    public ApiClient apiClient() {
        return new ApiClient();
    }
}