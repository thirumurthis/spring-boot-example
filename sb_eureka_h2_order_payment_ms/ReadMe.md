
### Project details within this folder

- Order service
   - Microservice using h2 database, to store order info.
   - This serivce, also invokes, the payment service with the generated order id to store the payment info in payment service.
   - This service is enabled as Eureka client, so it will be registered with application name, ORDER-SERVICE.
   - The payment service is invoked using RestTemplate postForObject(), the URL used is `http://PAYMENT-SERVICE/payment/pay`.
   - when creating the RestTemplate bean, use the `@LoadBalance` annotation.
  
- Payment service
   - Microservice using h2 database, to store payment info.
   - this service is registered to Eureka server for discovery.
   
- Eureka service
   - Microservice configured as Eureka server.
   
- Cloud gateway service
   - Microservice configured using spring cloud gateway, acts as an gateway api to access the order and payment service.
   - This service also registered as Eureka client.
   - The configuration in set in the application.yml file, which will redirect any requests to appropriate service
   - The serive is exposed in 8801 port.
   - Using below configuration, once the service is up, use `http://localhost:8801/order/addOrder` to post order request. (Note: we are using gateway service port)
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
         ```
    - Adding **hystrix** circuit breaker logic to the cloud gateway api, so in case of any failure the order and payment service will use fallback method.
    - Below steps is needed to configure hystrix.
       - add dependency to pom.xml
       ```xml
       <dependency>
			    <groupId>org.springframework.cloud</groupId>
			    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
		   </dependency>
       ```
       
       - Add `@EnableHystrix ` annotation on the main application CloudGatewayApplication.java in this case.
       
       - Create a rest controller class for fallback method handling, like below.
       ```java
        package com.cloudgateway.resource;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.RestController;
        import reactor.core.publisher.Mono;

        @RestController
        public class FallBackController {
          @GetMapping("/orderServiceFailOver")
          public Mono<String> orderServiceDown(){
            return Mono.just("Order Service is down. try latter.");
          }

          @GetMapping("/paymentServiceFailOver")
          public Mono<String> paymentServiceDown(){
            return Mono.just("Payment Service is down. try latter.");
          }
        }
       ```
       - Add configuration over the cloud gateway configuration.
       ```yaml
       
       ```
