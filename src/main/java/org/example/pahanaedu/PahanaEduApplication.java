package org.example.pahanaedu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class PahanaEduApplication {

	public static void main(String[] args) {
		SpringApplication.run(PahanaEduApplication.class, args);
	}

}
