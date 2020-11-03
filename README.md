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
