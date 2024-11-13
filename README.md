T# Learning Spring Boot 

## Lesson 1 CRUD Operations for User Class

### Lessons Learned
- Instead of cloning the tutorial repository, I used Spring Initializer. Needed to edit properties to add database info (which wasn't mentioned in the tuorial) for the application to work properly

#### Tutorial Resources
- https://spring.io/guides/gs/accessing-data-mysql (their git repository - https://github.com/spring-guides/gs-accessing-data-mysql.git)
- Use of GitHub Copilot to generate UPDATE, DELETE opearations application.al)

## Lesson 2 Testing UserController

#### Tutorial Resources
- https://spring.io/guides/gs/testing-web 
- https://www.bezkoder.com/spring-boot-webmvctest/ (content is hard to read - there is max-width of 640px applied)
- https://www.baeldung.com/junit-datajpatest-repository (git source - https://github.com/eugenp/tutorials/tree/master/persistence-modules/spring-data-jpa-annotations-2)

## Lesson 3 Add Custom Respository Method

- Added UserStory class
- UserStory class needs a custom GET method, `getUserStoriesByUserId()`
- Despite the tutorial resources I found (below) instructs me to create a custom interface and implement the interface in the custom class, I was able to implment the GET method simply adding `@Query` & the method signature in UserStoryRepository interface directly. This code was suggested by Copilot. I wonder why this works.

#### Tutorial Resources

- https://docs.spring.io/spring-data/jpa/reference/repositories/custom-implementations.html
- https://stackoverflow.com/questions/11880924/how-to-add-custom-method-to-spring-data-jpa
- https://www.bezkoder.com/spring-jpa-query/


#### API Documentation
- http://localhost:8080/swagger-ui/index.html

#### Additional Resources and Examples

- https://mkyong.com/spring-boot/spring-boot-spring-data-jpa-mysql-example/
- https://docs.spring.io/spring-framework/reference/testing/unit.html 

