# spring-boot-programs
Sample Spring-boot-programs

### db-service 
  - uses h2 in memory instance with some data loaded using the data.sql and schema.sql.
  - add and get service controller created which will be invoked from another service for demo puropose.
  - this service is configured as Eureka client, at application.properties has the  configuration, so this will be discovered on server and registered.
  
### subject-service
   - This service is used to call the db-service, using the Erukea-server url explicit (http://db-service/api/subjects)
   - This service is also configured as Eureka client refer the application.properties, where this will be discovered by eureka server and registered.
   
### eureka-service
   - This service is exposed as eureka server, and configured with not to act as client.
   - This service is also uses Zuul (acts as API gateway) for proxing the incoming request and disciver the Eureka service.
      - check the application.properties for redirection of incoming url to db-service or subject-service

### Using `Eureka discovery` fetch the url, registered to server using `Ribbon`
   - student-service will be invoking the db-service and uses the eurkea regeistered url.
   - Note: When there are many number of instance of application is running in differeng ports.
   - Set the `applicaton.properties` with below content on all the service projects (in eclipse run the project multiple times with different port#)
        - `spring.application.name=student-service`
        - `eureka.instance.instanceId=${spring.application.name}.${random.value}
   - in pom.xml, use dependency
```
    <dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-ribbon</artifactId>
    </dependency>
```
   - in student-service controller class on spring boot consumer use LoadBalancer object
   - with `ServiceInstance` class we can get the base URL and fire the required end point to db-service.
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
   
   #### Using Ribbion for client side load balancing.
   -  Ribbon can be used for Client side load-balancing, when using without Eureka server we might need to create Ribbon configuration java class.
   ```java
   //RibbinConfig.ajava
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
     -------------------------------------------------------------
     // Application.java
     // in the main class @SpringBootApplication annotated class use
     @RibbonClient(name="appclient",configuration=RibbonConfig.class)
     ...
     
     @Bean
     @LoadBalanced
     public RestTemplate restTemplate(){
       return new RestTemplate();
      }
      -------------------------------------------------------------
      //controller java class
      //AppController.java
      @Restcontroller
      @RequestMapping("/api")
      @AutoWired
      RestTemplate template;
      
      @Value("${server.port}")
      @GetMapping("message")
      public String getMessage(){
      // use the @Ribbonclient name in the url -> which is invoking another message service.
      // assume another service is exposed at that specific url.
        String url= "http://appclient/api/message"
	return template.getForObject(url,String.class);
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
  
