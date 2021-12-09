package it.unimib.bdf.greenbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@EntityScan("it.unimib.bdf.greenbook.models")
@EnableJpaRepositories(basePackages="it.unimib.bdf.greenbook.repositories")
public class GreenBookApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(GreenBookApplication.class, args);
	}

}
