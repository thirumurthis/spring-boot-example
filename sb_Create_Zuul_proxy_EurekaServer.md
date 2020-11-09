- Create a spring book project with eureka-server and Zuul dependencies.

```xml
    <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-zuul</artifactId>
		</dependency>
```

- application.properties configuration for zuul proxy
```
spring.application.name=eureka-service
server.port=8761

# configuration to specify, that this is server not to register as client
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false

# Note that db-service and subject-service are different microservice used.
# so now using http://localhost:8761/subject-service/api -> will be routed to corresponding url configured below
zuul.prefix=/api
zuul.routes.db-service.path=/db-service/**
zuul.routes.db-service.url=http://localhost:8090
zuul.routes.subject-service.path=/subject-service/**
zuul.routes.subject-service.url=http://localhost:8091

```
- java application to use the annotation
```java
package com.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}

}

```
