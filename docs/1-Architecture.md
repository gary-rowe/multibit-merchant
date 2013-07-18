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

The architecture does not depend on the existence of some library of feature-laden software. This allows you to use
such frameworks as tools, rather than having to cram your system into their limited constraints. A framework should
support, not enclose.

### Easy to test

The business rules can be tested without the UI, database, web server, or any other external element.

### No fixed UI

The UI can change easily, without changing the rest of the system. A Web UI could be
replaced  with a console UI, for example, without changing the business rules.

### No fixed persistence

By removing a fixed persistence model from your domain you free yourself to be able to swap out your persistence
store into something more suitable for the scale at which you're working. For unit testing a simple cache-based
in-memory database is sufficient. In production the design choice for persistence may be a relational database but as
 horizontal scaling becomes a significant cost this may change to become a NoSQL database instead.

All the above is made much easier when your domain is not dependent on a particular persistence implementation.

### Independent of external agencies

Your business rules simply don’t know anything at all about the outside world. The more isolated and self-contained
the domain model is, the easier it becomes to discuss and reason about.

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
The domain objects providing the functionality of the application.

### Infrastructure
Supporting code for the overall system.

### Interfaces
All the client-facing code such as REST APIs and JMS endpoints is here. These are the "ports" into the application.

[Next](2-Design.md)
