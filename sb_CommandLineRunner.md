- Below is the example of consuming a web sevice using CommandLineRunner.
- This is mostly like running a batch job.

Main Application class
  - Note: We added the spring-web dependency to use the RestTemplate
```java
package com.commandline.app;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import com.commandline.app.model.Product;

@SpringBootApplication
public class ClrappApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ClrappApplication.class, args);
	}
  // Rest template can be injected from bean here or using a configuration java class
	@Autowired
	RestTemplate restTemplate;
	
	final static String URL ="http://localhost:8080/api/products"; 
	
	@Override
	public void run(String... args) throws Exception {
	  Product[] products=restTemplate.getForObject(URL, Product[].class);
	  
	  List<Product> productLists = Arrays.asList(products);
	  //using lombok
	  productLists.forEach(item -> {System.out.println(item.getName()+" - "+item.toString());});
	}
}
```
- Configuration classs
```java
package com.commandline.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
```

 - Model Product class
```java
package com.commandline.app.model;

import lombok.Data;

@Data
public class Product {
	private long id;
	private String name;
	private int quantity;
}
```
- pom.xml dependencies
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.3.4.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.commandline.api</groupId>
	<artifactId>crlapp</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>clrapp</name>
	<description>Demo project for Spring Boot</description>

	<properties>
		<java.version>11</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>
		<dependency>
		    <groupId>org.springframework</groupId>
		    <artifactId>spring-web</artifactId>
		</dependency>
		<dependency>
		    <groupId>com.fasterxml.jackson.core</groupId>
		    <artifactId>jackson-databind</artifactId>
		</dependency>
		<dependency>
		    <groupId>com.fasterxml.jackson.core</groupId>
		    <artifactId>jackson-annotations</artifactId>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.16</version>
			<scope>provided</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
			<exclusions>
				<exclusion>
					<groupId>org.junit.vintage</groupId>
					<artifactId>junit-vintage-engine</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
	</dependencies>

	<build>
	
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>

```

- Output: when product service is up
```
2020-11-08 09:39:23.473  INFO 4228 --- [           main] com.commandline.app.ClrappApplication    : Started ClrappApplication in 1.562 seconds (JVM running for 3.257)
product-1 - Product(id=1, name=product-1, quantity=3)
product-2 - Product(id=2, name=product-2, quantity=2)
product-3 - Product(id=3, name=product-3, quantity=5)
product-4 - Product(id=4, name=product-4, quantity=1)
product-5 - Product(id=5, name=product-5, quantity=4)
```
