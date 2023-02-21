package de.tudresden.inf.st.mathgrassserver;

import de.tudresden.inf.st.mathgrassserver.evaluator.MessageBrokerConn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@SpringBootApplication
@EnableSwagger2
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

		// instantiate connection for communication with evaluator message broker
		MessageBrokerConn brokerConn = MessageBrokerConn.getInstance();
		try {
			brokerConn.connect();
			logger.info("Connection to message broker established!");
		} catch (Exception e) {
			logger.error("No message queue available. Aborting...");

			// TODO - shutdown application more gracefully
			System.exit(0);
		}
	}

	@Bean
	public InternalResourceViewResolver defaultViewResolver() {
		return new InternalResourceViewResolver();
	}

//	@EnableWebSecurity
//	public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			http.cors().and().csrf().disable();
//		}
//
//		@Bean
//		CorsConfigurationSource corsConfigurationSource() {
//			UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//			CorsConfiguration corsConfiguration = new CorsConfiguration();
//			corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
//			corsConfiguration.setAllowedMethods(Arrays.asList("*"));
//			corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
//			corsConfiguration.setAllowCredentials(false);
//			source.registerCorsConfiguration("/**", corsConfiguration);
//			return source;
//		}
//	}
}
