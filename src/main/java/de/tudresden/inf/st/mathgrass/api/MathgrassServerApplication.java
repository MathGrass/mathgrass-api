package de.tudresden.inf.st.mathgrass.api;

import de.tudresden.inf.st.mathgrass.api.feedback.evaluator.MessageBrokerConn;
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
}
