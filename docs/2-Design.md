[Home](README.md)

## Chapter 2 Design Choices

### Why Dropwizard?

Dropwizard was chosen to provide the REST API supporting infrastructure since it met the following criteria:

* Open source
* Simple and lightweight
* Process-oriented
* Uncompromising on REST
* Easy testing of resources
* Easy access to runtime metrics

For more information [you should read "Deployment Driven Design"](http://gary-rowe
.com/agilestack/2012/06/06/multibit-merchant-deployment-driven-design/).

### Why DTOs and entities?

Many performant web applications can be written using a combination of DTOs and services. This often leads to an
anaemic domain model where all the power of object-oriented expression is reduced to a series of manipulations of
getters and setters. The use of dedicated entities seeks to redress that imbalance thus DTOs are used where
appropriate: in external APIs such as persistence and REST Interfaces. The inflated role of the service is distilled
into supporting use cases by means of manipulating domain entities.

The outcome is that DTOs are used for what they were originally intended: as simple data carriers. The application
services present use cases to the Interfaces and in turn rely on the domain entities to decompose those use cases
into more manageable units of work.

### Why MongoDB?

This is a straight up scalability choice. At the time of writing the application relies on a relational database model
that has no hope of horizontal scalability. MongoDB provides an extremely simple data persistence model that adapts
to a wide range of requirements without resorting to complex 6th normal form constructs. Thus choosing MongoDB
provides both simplicity and scalability without loss of functionality.

### Why HAL?

The choice of Hypertext Application Language (HAL) came about as a natural progression from choosing to provide a
fully REST compliant API. In summary, HAL promotes:

* a lightweight and extensible approach
* working with JSON and XML (if required)
* a good linking framework
* easy interfacing with JavaScript client

For more information [you should read "Open the pod bay doors, HAL"](http://gary-rowe.com/agilestack/2012/06/08/multibit-merchant-open-the-pod-bay-doors-hal/).

[Next](3-Implementation.md)
