package com.bookwise.bookwise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableScheduling
@SpringBootApplication
public class BookwiseApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookwiseApplication.class, args);
	}

}
