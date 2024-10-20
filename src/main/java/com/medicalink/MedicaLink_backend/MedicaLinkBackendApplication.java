package com.medicalink.MedicaLink_backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MedicaLinkBackendApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
		System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
		System.setProperty("AUTH_KEY", dotenv.get("AUTH_KEY"));
		System.setProperty("FHIR_URL", dotenv.get("FHIR_URL"));

		SpringApplication.run(MedicaLinkBackendApplication.class, args);
	}

}
