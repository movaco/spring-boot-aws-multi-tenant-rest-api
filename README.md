![](https://github.com/movaco/spring-boot-aws-multi-tenant-rest-api/workflows/Java%20CI%20with%20Maven/badge.svg)

# Spring Boot multi-tenant example application

This repository contains the base implementation of our multi-tenant backend based on

 * Java // [Spring Boot](https://spring.io/projects/spring-boot) // [Maven](https://maven.apache.org/) application
 * Multi-tenant scoped (one database schema per tenant)
 * RESTful Api with [swagger](https://swagger.io/) definition
 * OAuth bearer authentication based on [AWS cognito](https://aws.amazon.com/de/cognito/)
 * [Hibernate](https://hibernate.org/) JPA with [Flyway](https://flywaydb.org/) database migration
 * [Google java code style](https://google.github.io/styleguide/javaguide.html) and [Lombok](https://projectlombok.org/)

## Basic Concept

The application is based on the following multi-tenancy concept:

 * One database schema per tenant
 * There is one global schema `default` containing a `tenant` table with all basic tenant information (see [V1__init_tenant_table.sql](src/main/resources/db/migration/default/V1__init_tenant_table.sql)):
   * Tenant-Name: Display-name of the tenant
   * Schema-Name: Name of the database schema in which all tenant data is stored
 * All tenant-specific data is stored in one separate schema
 * For each http-request 
   1. the [tenant interceptor](src/main/java/de/movaco/server/multi_tenancy/TenantInterceptorAdapter.java) will validate the jwt bearer token,
   2. [request the tenant-identifier](src/main/java/de/movaco/server/security/cognito/CognitoTenantResolver.java) from cognito,
   3. [add user roles](src/main/java/de/movaco/server/security/cognito/CognitoAuthenticationManager.java) to the authentication object and
   3. set the [tenant context](src/main/java/de/movaco/server/multi_tenancy/TenantContext.java) of the request so that all database connections will use the tenant schema during the request. 

This way all tenant-specific data is completely isolated within it's own schema and for each http-request it is assured that only the assigned tenant is used.

## Setup

To run the application a cognito user pool must be in place.

### AWS Cognito Setup

The authentication of this application is based on AWS cognito but could also be replaced by some other OAuth provider.

#### User Pool and application.yml

This application requires a cognito user pool set-up with the following minimum requirements:

 * One user pool for all tenants
 * A custom attribute called **tenant** (read-only) needs to be present and required for each user. The value needs to match the schema name of the tenant to which the user belongs to.
 * An app-client need to be created.

The following variables need to be set within the *application.yml*:

 1. cognito.region: region of the user pool
 2. cognito.poolId: ID of the user pool (starts with region code)
 3. cognito.clientId: ID of an app-client created for the user pool


#### Groups

To assign users to specific roles such as administrators, groups are being used.
Within the user-pool there need to be two groups with the following names:

 * admins: administrators of a specific tenant
 * superAdmins: super-administrator which is allowed to create, update and delete tenants

To create a tenant, a user needs to be added to the *superAdmins* group.


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


## Create an administrator

As listed in `de.movaco.server.security.Roles` there are two special user types:

  * ADMIN: administrator of a specific tenant. This user 



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