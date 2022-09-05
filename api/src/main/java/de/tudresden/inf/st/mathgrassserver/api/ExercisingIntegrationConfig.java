package de.tudresden.inf.st.mathgrassserver.api;

import de.tudresden.inf.st.mathgrassserver.apiModel.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExercisingIntegrationConfig {

    @Bean
    public EvaluatorApi runTaskApi() {
        return new EvaluatorApiImpl();
    }

    @Bean
    public GraphApi graphApi() {
        return new GraphApiImpl();
    }

    @Bean
    public TagApi tagApi() {
        return new TagApiImpl();
    }


    @Bean
    public TaskSolverApi taskSolverApi() {
        return new TaskSolverApiImpl();
    }

    @Bean
    public TaskTemplateApi taskTemplateApi() {
        return new TaskTemplateApiImpl();
    }

    @Bean
    public TaskApi tasksApi() {
        return new TaskApiImpl();
    }



}