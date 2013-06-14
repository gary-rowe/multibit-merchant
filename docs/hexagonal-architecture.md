# Hexagonal Architecture

## Package structure for a typical application

```
application
+ read
+ write

domain
+ factories
+ model
+ repositories

infrastructure
+ persistence
  + mem
+ spring

interfaces
+ rest
  + api
    + adapters
    + hal
      + common
      + representations
  + health
  + resources
  + utils
```
