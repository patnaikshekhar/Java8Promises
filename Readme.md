# Simple Promises Library for Java

## Getting Started
- Include the JAR file in your project

## Simple Done Scenario
The done callback is called after the promise is complete

```java
Promise<Integer> p = new Promise<>(() -> 0);
p.done((x) -> assertEquals((Integer) 0, x));
```

## Simple Error Scenario
The error callback is when an error occurs in the chain.

```java
Promise<Integer> p = new Promise<>(() -> {
    throw new Exception("Exception");
});

p.error((e) -> assertEquals("Exception", e.getMessage()));
```