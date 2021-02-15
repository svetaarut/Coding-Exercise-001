package com.transunion.test.codingexercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.transunion.test.codingexercise.service",
		"com.transunion.test.codingexercise.config",
		"com.transunion.test.codingexercise.utils",
		"com.transunion.test.codingexercise.model",
		"com.transunion.test.codingexercise.controller"})
public class CodingexerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodingexerciseApplication.class, args);
	}

}
