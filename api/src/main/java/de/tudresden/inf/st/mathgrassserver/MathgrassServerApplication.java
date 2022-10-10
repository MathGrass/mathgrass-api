package de.tudresden.inf.st.mathgrassserver;

import de.tudresden.inf.st.mathgrassserver.api.ExercisingIntegrationConfig;
import de.tudresden.inf.st.mathgrassserver.evaluator.MessageBrokerConn;
import de.tudresden.inf.st.mathgrassserver.evaluator.TaskManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Import({ExercisingIntegrationConfig.class})
@EnableSwagger2
@EnableWebMvc
public class MathgrassServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MathgrassServerApplication.class, args);

		MessageBrokerConn brokerConn = MessageBrokerConn.getInstance();
		try {
			brokerConn.connect();
			// while (true) {
			// 	new TaskManager().runTask(123,143,"3");
			// }

		} catch (Exception e) {
			System.err.println("aborting");
			System.exit(0);
		}

	}

	@Bean
	public InternalResourceViewResolver defaultViewResolver() {
		return new InternalResourceViewResolver();
	}

}
