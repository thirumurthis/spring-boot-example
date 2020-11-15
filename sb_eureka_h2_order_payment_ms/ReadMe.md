
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
    - When the preidacte is matched, the uri is invoked.
    - The circuit breaker configuration in gateway servie only works well only when invoking the service URL directly. 
    - With below configuration when using, RestTemplate in Orderservice is not displaying the message as expected.
      - Include dependecy of hystrix, and add @EnableCircuitBreaker in the @service / @component class with the fallbackMethoed name, which needs to be invoked incase of exception.
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
Class to use circuit breaker
```java
package com.learn.sb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.learn.sb.model.Order;
import com.learn.sb.model.OrderStatus;
import com.learn.sb.model.Payment;
import com.learn.sb.repo.OrderRepo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
@EnableCircuitBreaker
public class OrderService {

	@Autowired
	OrderRepo orderRepo;
	
	@Autowired
	RestTemplate template;
	
	public final String URL= "http://PAYMENT-SERVICE/payment/pay";
	
	public Order getOrders(int orderId) {
		return orderRepo.findById(orderId).orElse(new Order());
	}
	
	@HystrixCommand(fallbackMethod = "fallbackOrderService")
	public OrderStatus addOrder(Order order) {
		OrderStatus orderStatus = new OrderStatus();
		Order persistedOrder = orderRepo.save(order);
		Payment payment = paymentprocess(persistedOrder);
		orderStatus.setOrder(persistedOrder);
		orderStatus.setPayment(payment);
		orderStatus.setStatus(payment.getStatus());
        return orderStatus;
	}
	
	public Payment paymentprocess(Order order) {
		Payment payment = new Payment();
		payment.setOrderId(order.getOrderId());
		payment.setAmount(order.getOrderAmount());
		HttpEntity<Payment> request = new HttpEntity<>(payment);
		return template.postForObject(URL, request, Payment.class);
	}

	public List<Order> getAllOrders() {
		return orderRepo.findAll();
	}
	
	public OrderStatus fallbackOrderService(Order order) {
		OrderStatus orderStatus = new OrderStatus();
		Order persistedOrder = orderRepo.save(order);
		orderStatus.setOrder(persistedOrder);
		orderStatus.setPayment(new Payment());
		orderStatus.setStatus("From orderService - Payment service not accessible.");
		return orderStatus;
	}
}

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
