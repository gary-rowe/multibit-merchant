[Home](README.md)

## Chapter 1 Architecture

MultiBit Merchant is based on the Hexagonal Architecture using Domain Driven Design (DDD) principles. This is also
known as the Ports and Adapters architecture.

In general the project is arranged into four main areas: application, domain, infrastructure and interfaces. 
Do not confuse the interfaces package with an actual Java `interface`. The interfaces package collects all the manners
in which the application layer can be accessed by external clients (e.g. REST, JMS and so on).

A Hexagonal Architecture follows a package structure that can be easily split into multiple modules, or even projects,
to allow for independent scalability as the project matures. 

### Production package structure

```
application - contains read and write services to allow interaction with the domain objects

application/domain_services
application/dto_services
application/dto_services/comparators

domain - the domain objects

domain/common
domain/factories
domain/model

infrastructure - supporting code for the application (includes AOP frameworks)

infrastructure/dto
infrastructure/dto/adapters
infrastructure/dto/adapters/domain
infrastructure/dto/adapters/dto
infrastructure/dto/factories
infrastructure/dto/repositories
infrastructure/jms
infrastructure/persistence
infrastructure/persistence/mem
infrastructure/persistence/mongo
infrastructure/spring
infrastructure/utils

interfaces - client-facing code

interfaces/rest
interfaces/rest/api
interfaces/rest/api/hal
interfaces/rest/api/hal/common
interfaces/rest/api/hal/representations
interfaces/rest/health
interfaces/rest/links
interfaces/rest/resources
interfaces/rest/utils
```

### Test package structure

```
application/domain_services
application/dto_services
application/dto_services/entity/mem
application/dto_services/entity/mongo

domain

infrastructure

infrastructure/dto
infrastructure/dto/adapters
infrastructure/dto/adapters/domain
infrastructure/dto/adapters/dto

interfaces

interfaces/rest
interfaces/rest/resources
interfaces/rest/stories

testing
utils
```

### Test resource structure

```
curl - A collection of curl scripts (derived from user stories)
curl/<entity>
curl/stories

fixtures - The test fixtures containing JSON/XML
fixtures/<entity>
  
```

### Application
Provides the services to expose the domain objects.

### Domain
The domain objects providing the functionality of the application.

### Infrastructure
Supporting code for the overall system.

### Interfaces
All the client-facing code such as REST APIs and JMS endpoints is here. These are the "ports" into the application.
