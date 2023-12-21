package com.webnongsan.greenshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GreenshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenshopApplication.class, args);
	}

}
