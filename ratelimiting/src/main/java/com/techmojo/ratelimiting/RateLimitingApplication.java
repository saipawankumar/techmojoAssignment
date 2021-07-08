package com.techmojo.ratelimiting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.techmojo.ratelimiting", exclude = {
		DataSourceAutoConfiguration.class,
		SecurityAutoConfiguration.class,
})
public class RateLimitingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RateLimitingApplication.class, args);
	}

}
