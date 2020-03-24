![](https://github.com/movaco/spring-boot-aws-multi-tenant-rest-api/workflows/Java%20CI%20with%20Maven/badge.svg)

# Spring Boot multi-tenant application

This repository contains the base implementation of our multi-tenant backend based on

 * Java application build with Maven
 * Spring Boot
 * multi-tenant scoped (schema per tenant)
 * RESTful Api with swagger definition
 * OAuth bearer authentication based on [AWS cognito](https://aws.amazon.com/de/cognito/)
 * Hibernate JPA
 * Flyway database migration
 * Google code formatting
 * Lombok


# Development

## Java Code Formatting

Check formatting based on [google java style guidelines](https://google.github.io/styleguide/javaguide.html):
```
mvn com.coveo:fmt-maven-plugin:check -Dverbose=true
```
To reformat the entire project run

```
mvn com.coveo:fmt-maven-plugin:format
```

## Vulnerabilities

To check for vulnerable dependencies using OSSIndex run
```
mvn org.sonatype.ossindex.maven:ossindex-maven-plugin:audit -f pom.xml
```

## Tests

Run all tests and create *jacoco* coverage report using
```
mvn test jacoco:report
```

## Build

To build a jar artefact containing all dependencies run
```
mvn package -DskipTests
```