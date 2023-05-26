package de.tudresden.inf.st.mathgrass.api;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import de.tudresden.inf.st.mathgrass.api.admin.config.JWTAuthenticationFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

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

    @EnableWebSecurity
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        //        Sreeram Change for Admin Interface - Starts
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.cors().and().csrf().disable();
//        }

        private final JWTAuthenticationFilter jwtAuthFilter;

        private final AuthenticationProvider authenticationProvider;

        public WebSecurityConfig(JWTAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
            this.jwtAuthFilter = jwtAuthFilter;
            this.authenticationProvider = authenticationProvider;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf()
                    .disable()
                    .authorizeHttpRequests()
                    .antMatchers("/api/admin/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authenticationProvider(authenticationProvider)
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        }
        //        Sreeram Change for Admin Interface - Ends
        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(List.of("*"));
            corsConfiguration.setAllowedMethods(List.of("*"));
            corsConfiguration.setAllowedHeaders(List.of("*"));
            corsConfiguration.setAllowCredentials(false);
            source.registerCorsConfiguration("/**", corsConfiguration);
            return source;
        }
    }
}
