# spring-boot-programs
Sample Spring-boot-programs

### db-service 
  - h2 instance with some data loaded.
  - Eureka client configured, application.properties which configuration to set discover client on server and register.
  
### subject-service
   - This calls the db-service, using the Erukea-server explicit (http://db-service/api/subjects)
   - Eureka client configured, application.properties
   
### eureka-service
   - This project uses Zuul and Eureka service (a server part, with configurations)


### Using `Eureka discovery` for getting the registered instances using `Ribbon`
   - Set the `applicaton.properties` with below content
        - `spring.application.name=student-service`
        - `eureka.instance.instanceId=${spring.application.name}.${random.value}
      - For runnning, the service instance, update the `server.port=8081` and different for each instance.
   - pom.xml, use dependency
```
    <dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-ribbon</artifactId>
    </dependency>
```
   - in the controller or configuration class on spring boot consumer use LoadBalanceer
```
....
	@Autowired
	private LoadBalancerClient loadBalancer;
	
	public void getEmployee() throws RestClientException, IOException {
		
		ServiceInstance serviceInstance=loadBalancer.choose("student-service"); // no actual url is hard coded here
		String baseUrl=serviceInstance.getUri().toString();
		baseUrl=baseUrl+"/students";
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response=null;
    ....
```
   
   - Ribbon can be used for Client side load-balancing, when using without Eureka server we might need to create Ribbon configuration java class.
   ```java
   public class RibbonConfig{
     @AutoWired
     IClientConfig config;
     
     @Bean
     public IPing ping(IclientConfig config){
     return new PingUrl();
     }
     
     @Bean
     public IRule rule(IClientCondfig config){
     return new AvailablityFilteringRuke();
     }
     
     // in the main class @SpringBootApplication annotated class use
     @RibbonClient(name="appclient",configuration=RibbonConfig.class)
     ...
     
     @Bean
     @LoadBalanced
     public RestTemplate restTemplate(){
       return new RestTemplate();
      }
   ```
   ```yaml
   # application.yml
   appclient  #name of the client used in the @RibbonClient
      ribbon:
         eureka:
	   enabled: false
	 listOfServers: localhost:8080,localhost:8081
	 ServerListRefershInterval: 2000
   ```
#### API Gateway
  - Two options,  `Zuul` vs `spring cloud gateway` used as API gate way
     - Zuul 1.0 used in spring which blocking or synchornized, servlet based.
     - Spring cloud gateway- spring framework 5 based Non-blocking or async.
  
