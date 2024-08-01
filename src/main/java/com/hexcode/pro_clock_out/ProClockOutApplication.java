package com.hexcode.pro_clock_out;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ProClockOutApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProClockOutApplication.class, args);
	}

}
