package com.tnx.posBilling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PosBillingApplication {

	public static void main(String[] args) {
		SpringApplication.run(PosBillingApplication.class, args);
	}

}
