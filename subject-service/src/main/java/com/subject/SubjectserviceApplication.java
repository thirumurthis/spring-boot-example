package com.subject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SubjectserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubjectserviceApplication.class, args);
	}

}
