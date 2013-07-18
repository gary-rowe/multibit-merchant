[Home](README.md)

## Chapter 1 Architecture

MultiBit Merchant is based on the Hexagonal Architecture using Domain Driven Design (DDD) principles. This is also
known as the Ports and Adapters architecture. The design goals of this architecture are roughly that it must be:

* independent of frameworks
* easy to test
* no fixed user interface (UI)
* persistence is neither fixed nor mandatory
* independent of any external agency

### Independent of frameworks

The architecture does not depend on the existence of some library of feature laden
software. This allows you to use such frameworks as tools, rather than having to cram your system into their limited constraints.

### Easy to test

The business rules can be tested without the UI, Database, Web Server, or any other external element.

### No fixed UI

The UI can change easily, without changing the rest of the system. A Web UI could be
replaced  with a console UI, for example, without changing the business rules.

### No fixed persistence

. You can swap out Oracle or SQL Server, for Mongo, BigTable, CouchDB,
or something else. Your business rules are not bound to the database.

### Independent of external agencies

. In fact your business rules simply don’t know anything at all about the
outside world.

## The Hexagonal Architecture

The application is split into 4 discrete areas that encapsulate specific requirements listed here in order of how
restricted the layer is:

* Domain
* Application
* Interfaces
* Infrastructure

These are described in more detail below. However, the Dependency Rule states that an "inner" area cannot reference
an "outer" one except in the case of Infrastructure which provides common utilities and annotations.

### Domain

The Domain objects providing the functionality of the Application. Typically this code represents the Ubiqitous
Language used by developers and Domain experts. Method names are expressive and target tasks within use cases. The
domain can only reference the Application through an injected interface.

References to Infrastructure are only made as a result of including annotations or access to common utility methods
that act upon value objects that may be found in multiple layers.

Examples include use of `@Inject` (for depenency injection) or `DateUtils`.

### Application

Provides the services to expose the Domain objects. Typically this code is defined by services targeting use cases.

#### A note on CQRS

These may follow the [Command Query Responsibility Segregation (CQRS) design pattern](http://martinfowler.com/bliki/CQRS.html)
for maximum scalability. In practice this means implementing services that provides "read" and "write" operations
separately. This then allows the developer to reason about implementing queries in isolation from any updates that
may be taking place. Thus the supporting architecture for reads may be completely different to that of writes without
either side leaking into the other. Perhaps the reads use a Reporting Database, while the writes make use of a JMS
queue.

### Interfaces

All the client-facing code such as REST APIs and JMS endpoints can be found here. These are the "ports" into the
application in the Hexagonal Architecture nomenclature. Any required bootstrapping code to launch the application would be placed here,
although it may call upon the Infrastructure for support. Interface code relies on the Application to accomplish its goals.

### Infrastructure

Supporting code for the overall system. Common utilities and frameworks are configured and implemented in this layer.
This includes persistence and dependency injection. Persistence often makes use of a DTO model rather than persisting
entities directly so that REST APIs can directly access persistence implementations without involving the domain leading
to reduced complexity and improved overall efficiency. By necessity Infrastructure may reference Application and/or Domain.

[Next](2-Design.md)
