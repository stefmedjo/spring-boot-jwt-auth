# Springboot JWT Authentication

JSON Web Token (JWT) Authentication using Spring Boot, Spring Security, Spring Data JPA and MySQL

## Getting started
### Prerequisites
* JDK 8 or more
* Maven
* MySQL 

### Run it! 
1. Clone this repository and get inside the spring-boot-jwt-auth folder.
```
$ git clone https://github.com/stefmedjo/spring-boot-jwt-auth.git
$ cd spring-boot-jwt-auth
```
2. Install all the dependencies.
```
$ mvn install
```
3. Create a database with the name "spring".
```
$ CREATE DATABASE spring;
```
4. Set username and password.
```
spring.datasource.username = username
spring.datasource.password = password
```
5. Run the project.
```
$ mvn spring-boot:run
```
6. Add Roles USER and ADMIN.
```
$ INSERT INTO roles VALUES(null,"USER");
$ INSERT INTO roles VALUES(null,"ADMIN");
```

## License
[MIT License](http://opensource.org/licenses/MIT).  
Copyright (c) 2020 [stefmedjo](https://twitter.com/stefmedjo).