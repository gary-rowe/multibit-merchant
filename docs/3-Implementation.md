[Home](README.md)

## Chapter 3 Implementation

The next step in exploring the application is to look at implementation details. We start with the package structure
and then go on to explore specific solutions to problems within each layer.

### Package structure

Since this is a single module application, all the layers can be expressed as packages. Should the application grow
into one requiring a Maven reactor then each of these packages can be moved out into their own modules and combined
into a collection of JARs and WARs as required.

A quick overview provides the following:

```
domain/common
domain/factories
domain/model

application/domain_services
application/dto_services
application/dto_services/comparators

interfaces/rest
interfaces/rest/api
interfaces/rest/api/hal
interfaces/rest/api/hal/common
interfaces/rest/api/hal/representations
interfaces/rest/health
interfaces/rest/links
interfaces/rest/resources
interfaces/rest/utils

infrastructure/dto
infrastructure/dto/adapters
infrastructure/dto/adapters/domain
infrastructure/dto/adapters/dto
infrastructure/dto/factories
infrastructure/jms
infrastructure/persistence
infrastructure/persistence/mem
infrastructure/persistence/mongo
infrastructure/persistence/repositories
infrastructure/spring
infrastructure/utils
```

Each layer is described in more detail below.

### Domain

```
domain/common
domain/factories
domain/model
```

The `domain/model` contains the entities and any sub-packages allowing for natural subdivision of those entities.

#### Common domain utilities

The most common domain utilities tend to be expressed in terms of interfaces and include

* `Entity` - ID management
* `PaginatedList` - an interface wrapper for a List with `Pagination` to allow paginated batches

#### Domain factories

Frequently it is necessary to create complex instances of domain entities. Having entity-specific factories available
at runtime (i.e. not just in test code) allows for easier coding of complex use cases that may not require
persistence.

### Application

```
application/domain_services
application/dto_services
application/dto_services/comparators
```

DTO comparators are often required when implementing page ordering and suchlike from presentation logic.

#### Domain services

These are services that operate on domain entities directly. Usually these are for initiating use cases or
occasionally providing access to a repository for read and write services although the actual implementation normally
has a domain entity adapted to/from a DTO to reduce code duplication.

#### DTO services

These are services that operate on DTOs directly. Typically these are used for near direct access to a repository for
both read and write operations.

### Interfaces

```
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

#### API

The `api` package contains code to support REST representations used in responses. They rely on having a DTO which is
then adapted into a HAL representation containing additional semantic markup such as `_links` and `_embedded`
entries to facilitate side-loading of related entities in a single request.

The DTOs are re-used as HTTP request entities (payloads) as part of the REST interface supporting persistence
operations through PUT, POST, DELETE and so on.

In order to create bindings between entities (such as Foo 1 has Bar 2) the REST API typically relies on both
identities being provided in a single path (no request entity). For example:

```
PUT /foos/1/bars/2
```

is sufficient to cause Foo 1 to be bound to Bar 2.

For more complex bindings, such as a many-to-may-with-attributes all that is required is an HTTP request entity
providing the necessary detail, such as an additional entity for Bas 3 with a particular index position:

```
PUT /foos/1/bars/2
{
  "basId" : 3,
  "index" : 4
}
```

#### Health

Dropwizard provides a useful set of internal self-tests called health checks. Each targets a particular area of
functionality that is usually an external dependency, such as a database connection. In the event of a failure a
network administrator can quickly interrogate an endpoint served on an admin port to determine the cause of a problem.

#### Links

In order to keep development of REST resources following the Don't Repeat Yourself (DRY) principle,
it is good practice to avoid hard coding paths into the `@Path` annotations. In the event that you decide to change a
 naming convention (perhaps switching away from `/CamelCase` to the more net-friendly `hyphenated-case`) then it is
 simply a matter of updating the relevant common strings.

when working with HAL it is also necessary

* `StandardLinkRelations` - contain the [IETF standard link relation names](http://www.iana.org/assignments/link-relations/link-relations.xml)
* `ApplicationLinkRelations` - contain application-specific link relations (e.g. `FOOS_REL="foos"`)
* `Paths` - contain the various fragments used when building up `@Path` entries (e.g. `FOO_ID="/{fooId}"`)

#### Resources

All the REST API endpoints are defined here. Typically classes will have a lot of JAX-RS annotations and implemented
in the following order:

* `POST` - supporting create/insert (first because it's the first operation you need to support)
* `GET` - supporting `findAll()` providing a paginated list of DTOs (master view) based on a regex
* `GET` - supporting `findOne()` providing a single DTO (detail view) based on an ID
* `PUT` - supporting `update()` providing an update operation taking a DTO as a request entity
* `DELETE` - supporting `delete()` providing a mechanism to either "soft" or "hard" delete an entity (flag or remove)

Usually resources perform some simple validation and then hand over to an Application DTO service to fulfil the
operation.

#### Utilities

Commonly a `RuntimeExceptionMapper` is required to simplify and unify the exception handling caused by validation
errors.

### Infrastructure

```
infrastructure/dto
infrastructure/dto/adapters
infrastructure/dto/adapters/domain
infrastructure/dto/adapters/dto
infrastructure/dto/factories
infrastructure/jms
infrastructure/persistence
infrastructure/persistence/mem
infrastructure/persistence/mongo
infrastructure/persistence/repositories
infrastructure/spring
infrastructure/utils
```

#### Data Transfer Objects (DTOs)

Since DTOs are used in many layers they belong in Infrastructure. Adapters exist to allow DTOs to be created from
domain entities and for the reverse. The factories are provided for the same reasons as for the domain.

#### Persistence

The combination of the [MongoJack](http://mongojack.org/) and [Embedded Mongo](https://github.com/flapdoodle-oss/embedmongo.flapdoodle.de)
libraries provides an easy way to create and test repository code against a real MongoDB. The Embedded Mongo library
can be configured to pull down a particular version of MongoDB as part of the build process and then execute against
it. The first time this occurs it can be quite lengthy (so it may not be suitable for clean virtual machine builds
like Travis) but is then stored locally for fast extraction and use within test cases.

Since working against a real database can be a little slow developers may choose to create in-memory implementations
of repositories based on [Guava caches](https://code.google.com/p/guava-libraries/wiki/CachesExplained). This allows
test code to be partitioned into unit and integration tests which can be configured through a Maven profile. Thus the
 critical tests are conducted every build, but the more heavyweight ones are run less often.

#### Spring or Guice

In general Spring is the preferred choice for dependency injection. This is because it also provides many other
features through its `XYZTemplate` approach which are damn handy. Of course, if your application does not require all
 the extra features that Spring brings to the table it can easily be swapped out for Guice instead.

[Home](README.md)