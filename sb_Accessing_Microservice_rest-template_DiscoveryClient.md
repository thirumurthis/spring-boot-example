- Create a Product microservice - registered to eureka server as `product-service`
- Create a Order microservice - registered to eureka server as `order-service`
- Create a Payment microservice - registered to eureka server as `payment-service`
- Both the service, include eureka server. The both service include eureka-discovery-client.

```java
@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	private RestTemplate restTemplate;
  
       //Using the discovery client
       //The zuul configuration is NOT applied to the eureka server
 	@Autowired
	private DiscoveryClient discoveryClient;
	
	@GetMapping("/products")
	public List<Subjects> getSubjects(){
		
   	   List<ServiceInstance> instances=discoveryClient.getInstances("order-service");
	   ServiceInstance serviceInstance=instances.get(0);
		
	   String baseUrl=serviceInstance.getUri().toString();
	   ParameterizedTypeReference<List<Order>> order = new ParameterizedTypeReference<List<Order>>() {};

	   ResponseEntity<List<Order>> response =  restTemplate.exchange(URL, HttpMethod.GET, null, order);
	   return response.getBody();
	}
}
```
 - To include the Eureka client in the service.
 - pom.xml
 ```xml
    <!-- // include below dependencies. -->
   <dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-eureka</artifactId>
   </dependency>
 ```
- add `application.yml` configuration

```yml
spring:
  application:
    name: product-service
server:
  port: 8091

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      # below is the eureka server url
      default-zone: http://localhost:8761/eureka
  instance:
    hostname: localhost
 ```
