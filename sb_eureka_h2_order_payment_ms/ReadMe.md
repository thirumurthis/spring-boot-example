
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
   
   - Use below configuration for cloud gateway api forwarding.
      - Once the service is up, use `http://localhost:8801/order/addOrder` to post order request. 
      - Note: we are using gateway service port 8801
   
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
       
   - Below configuration on the the cloud gateway service, helps to setup the fallback using circuit breaker and hystrix.
       
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
        filters:
          - name: CircuitBreaker
            args:
              name: order-service-failback
              fallbackUri: forward:/orderServiceFailOver
      - id: payment-service
        uri: lb://PAYMENT-SERVICE
        predicates:
          - Path=/payment/**
        filters:
          - name: CircuitBreaker
            args:
              name: payment-service-failback
              fallbackUri: forward:/paymentServiceFailOver  

 # after 5 seconds if there is no response stop the process
 hystrix.command.fallbackcmd.execution.isolation.thread.timeoutInMilliseconds: 5000
 
 management:
  endpoints:
    web:
      exposure:
        include:
        - hystrix.stream
```

   - Once the gateway api Service starts successfully, use the url `http://localhost:8801/actuator/hystrix.stream` to check if service is up
   - The Circuit breaker configuration in the filters, invokes the fallback url in case if service is down.

#####  Another way to configure hystrix properties is via `@Hystrixcommand` annoation on the method which is actually calling another service.
  ```
  @HystrixCommand(fallbackMethod="methodToFallBack",
      commandProperties = {
           @HystrixProperties(name= "execution.isolation.thread.timeoutInMilliseconds", value= "2000"),
	   @HystrixProperties(name= "circuitBreaker.requestVolumeThreshold", value= "5"),
	   @HystrixProperties(name= "circuitBreaker.errorThresholdPercentage", value= "50"),
	   @HystrixProperties(name= "circuitBreaker.sleepWindowInMilliseconds", value= "5000")
	   }) 
  public Order getOrder(){
  ....
  }
  ```
