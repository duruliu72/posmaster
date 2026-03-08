package com.osudpotro.posmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class PosmasterApplication {
	public static void main(String[] args) {
		SpringApplication.run(PosmasterApplication.class, args);
	}
}
