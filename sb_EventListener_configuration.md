- Below is how to get handle of the Events and perform some action

Using 
```
	@EventListener(ApplicationReadyEvent.class)
	public void initialize() {
		 // perform some action like initialize DB or some task.
	}
```

```java
package com.product.app;

import javax.annotation.PreDestroy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

// When the application is started up below message will be printed
// not the method name can be any
	@EventListener(ApplicationReadyEvent.class)
	public void initialize() {
		System.out.println("Application ready..");
	}
	
  @PreDestroy
	public void close() {
		System.out.println("Application Shutting down...");
	}
}

```

- Above spring boot service was using spring-boot-web-started project.
- output: From Eclipse console note was using jpa h2 db for this service.
```
...
2020-11-08 09:45:53.331  INFO 14500 --- [           main] com.product.app.DemoApplication          : Started DemoApplication in 6.595 seconds (JVM running for 8.386)
Application ready..
2020-11-08 09:46:04.028  INFO 14500 --- [on(6)-127.0.0.1] inMXBeanRegistrar$SpringApplicationAdmin : Application shutdown requested.
Application Shutting down...
2020-11-08 09:46:04.132  INFO 14500 --- [on(6)-127.0.0.1] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
```
