[Home](README.md)

## Chapter 1 Architecture

MultiBit Merchant is based on the Hexagonal Architecture using Domain Driven Design (DDD) principles. This is also
known as the Ports and Adapters architecture.

It might easiest to think of it in terms of package structure:

```
application - contains read and write services to allow interaction with the domain objects
+ read
+ write

domain - the domain objects
+ factories
+ model - the domain model(s)
+ repositories - interfaces to repositories for persistence

infrastructure - supporting code for the application (includes AOP frameworks)
+ persistence
  + hibernate
  + mem
+ spring

interfaces - client-facing code
+ rest - contains client-facing code for REST API
  + api - the client DTOs for request payloads
    + adapters - adapt from DTOs to domain objects
    + hal - support for Hypertext Application Language
      + common
      + representations - the client representations for response payloads
  + health - REST environment integrity checking
  + resources - contains all endpoints
  + utils
```

### Application
Provides the services to expose the domain objects.

### Domain
The domain objects providing the functionality of the application.

### Infrastructure
Supporting code for the overall system.

### Interfaces
All the client-facing code such as REST APIs and JMS endpoints is here. These are the "ports" into the application.
