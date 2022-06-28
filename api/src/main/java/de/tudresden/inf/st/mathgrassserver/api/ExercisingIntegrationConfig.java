package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.RunTaskApi;
import de.tudresden.inf.st.mathgrassserver.api.RunTaskApiImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExercisingIntegrationConfig {

    @Bean
    public RunTaskApi taskApi() {
        return new RunTaskApiImpl();
    }
/*
    @Bean
    public GraphApi graphApi() {
        return new GraphApi();
    }

    @Bean
    public TagApi tagApi() {
        return new GraphApi();
    }


    @Bean
    public TaskSolverApi taskSolverApi() {
        return new TaskSolverApi();
    }

    @Bean
    public TaskTemplateApi taskTemplateApi() {
        return new TaskTemplateApi();
    }

    @Bean
    public GraphApi graphApi() {
        return new GraphApi();
    }
    */





}