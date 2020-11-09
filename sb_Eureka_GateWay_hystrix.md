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
