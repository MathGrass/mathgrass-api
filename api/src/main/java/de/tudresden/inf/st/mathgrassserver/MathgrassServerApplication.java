package de.tudresden.inf.st.mathgrassserver;

import de.tudresden.inf.st.mathgrassserver.api.ExercisingIntegrationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ExercisingIntegrationConfig.class)
public class MathgrassServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MathgrassServerApplication.class, args);
	}

}
