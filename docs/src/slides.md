
![img](images/brand/holisticon-logo-grey.svg) <!-- .element: style="width: 40%" -->

# Axon Academy
## Axon Testing Workshop

<!-- .slide: class="title" data-background="images/brand/title_white_footer.png" data-background-repeat="repeat-x" data-background-position="bottom center" data-background-size="inherit" -->

---

# Structure

* Testing concepts
* Unit Testing with Fixtures
  * Aggregates
  * Sagas
* JGiven Axon Extension
* Integration Testing with Spring Boot
* Black Box Testing with Spring Boot & Test Containers

---

# Testing Concepts

* E2E         -> Full system (black box)
* Integration -> Components (white box, mocks)
* Unit        -> Individual functions (white box, mocks)

---

# Unit Testing

--

## Theory

* Use JUnit as runner
* Test one class
* Use Fixtures provided by Axon Framework
* Limitations: multiple classes in combination

--

## Aggregate Testing

* Testing of the aggregate in isolation
* Given/When/Then style
* Command dispatching tests
```java
import org.axonframework.test.aggregate.AggregateTestFixture;
```

--

## Saga Testing

* Testing of the saga in isolation
* Given/When/Then style
* Event command testing

```java
import org.axonframework.test.saga.SagaTestFixture;
```

---

# JGiven Axon Extension

* Testing of Aggregates, Sagas, Projections
* Usage of JGiven with special Axon Stages
* Business-friendly reporting
* Limitations: same as by unit

--

// MORE

---

# Spring Integration Testing

--

## Theory

* Spring Boot Test
* Own Stages sending commands
* Usage of JGiven
* Usage of Awaitility if required
* Usage of own configuration
* Limitations: no serialization, in-memory facilities

---

# Spring Boot with Test Containers

--

## Theory

* Spring Boot Test
* Axon Server in Container
* Usage of JGiven
* Usage of Awaitility
* Usage of real adapters (e.g. REST)

---





