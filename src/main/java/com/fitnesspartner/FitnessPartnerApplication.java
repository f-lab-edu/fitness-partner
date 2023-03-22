package com.fitnesspartner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FitnessPartnerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitnessPartnerApplication.class, args);
	}
}
