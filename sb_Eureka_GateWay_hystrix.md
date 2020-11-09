- Create payment-service.
- Create order-service. This service invokes the payment service so the order payment is stored in db.

### Eureka server configurations
<details>
  <summary>Eureka service configuration details</summary>
  
  ####  Main application java class
  ```java
package com.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}
}
  ```
 ##### application.properties
 
 ```
spring.application.name=eureka-service
server.port=8761

eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
 ```
</details>

### Eureka client configurations to done on the Micro service
<details>
  <summary>Eureka client configuration details</summary>
  
  ####  Main application java class
  ```java
  package com.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

//add this annoations
@EnableEurekaClient
@SpringBootApplication
public class DbserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbserviceApplication.class, args);
	}
}
  ```
 #### application.yml
 ```yml
 server:
    port: 8888
 spring: 
    application:
       name: db-service
 eureka:
    client:
      register-with-eureka: true
      fetch-registry: true
      service-url:
        defaultZone: http://localhost:8761/eureka/
    instance: 
      hostname: localhost
 ```
</details>


### spring cloud GATEWAY configurations, use `gateway` dependencies
<details>
  <summary>Spring cloud gateway configuration details</summary>
   - Create spring boot project with below dependencies
	```
	 - Gateway
	 - Actuator
	 - Eureka client (this service will also be registred in Eureka server)
	```
   - Use the above Eureka client configuration steps, for this project too
	- annotate main application class with `@EnableEurekaclient`
	- set configuration at `application.properties` file.
  ####  Main application java class
  ```java
  package com.cloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CloudGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudGatewayApplication.class, args);
	}
}
  ```
 - application.yaml file for cloud gateway config
 ```yaml
 spring:
  application:
    name: GATEWAY-SERVICE
  cloud:
    gateway:
      routes:
      - id: order-service
        uri: lb://ORDER-SERVICE
        predicates:
          - Path=/order/** 
      - id: payment-service
        uri: lb://PAYMENT-SERVICE
        predicates:
          - Path=/payment/**
 
server:
  port: 8801
 
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://localhost:8761/eureka
  instance:
    hostname: localhost
 ``` 
</details>
