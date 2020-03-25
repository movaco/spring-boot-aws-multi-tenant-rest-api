![](https://github.com/movaco/spring-boot-aws-multi-tenant-rest-api/workflows/Java%20CI%20with%20Maven/badge.svg)

# Spring Boot multi-tenant example application

This repository contains the base implementation of our multi-tenant backend based on

 * Java // [Spring Boot](https://spring.io/projects/spring-boot) // [Maven](https://maven.apache.org/) application
 * Multi-tenant scoped (one database schema per tenant)
 * RESTful Api with [swagger](https://swagger.io/) definition
 * OAuth bearer authentication based on [AWS cognito](https://aws.amazon.com/de/cognito/)
 * [Hibernate](https://hibernate.org/) JPA with [Flyway](https://flywaydb.org/) database migration
 * [Google java code style](https://google.github.io/styleguide/javaguide.html) and [Lombok](https://projectlombok.org/)

## Usage

The application only requires a cognito user pool to be available.

### AWS Cognito Setup

This application requires a cognito user pool and the following variables need to be set within the *application.yml*:

 1. cognito.region: region of the user pool
 2. cognito.poolId: ID of the user pool (starts with region code)
 3. cognito.clientId: ID of an app-client created for the user pool

## Build and run the application
To run the application with an in-memory hsqldb you need to build the jar-file
```
mvn package -DskipTests
```
and execute it
```
java -jar target/server.jar
```
which will start the application on **port 5000**
The swagger-ui will be available at [http://localhost:5000/swagger-ui.html]()



## Development

### Java Code Style

Check formatting based on [google java style guidelines](https://google.github.io/styleguide/javaguide.html):
```
mvn com.coveo:fmt-maven-plugin:check -Dverbose=true
```
To reformat the entire project run

```
mvn com.coveo:fmt-maven-plugin:format
```

### Vulnerabilities

To check for vulnerable dependencies using OSSIndex run
```
mvn org.sonatype.ossindex.maven:ossindex-maven-plugin:audit -f pom.xml
```

### Tests

Run all tests and create *jacoco* coverage report using
```
mvn test jacoco:report
```

### Build

To build a jar artefact containing all dependencies run
```
mvn package -DskipTests
```