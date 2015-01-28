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

## Promises can be chained with then
```java
(new Promise<>(() -> 1))
        .then((x) -> {
            assertEquals((Integer) 1, x);
            return 2;
        })
        .then((x) -> {
            assertEquals((Integer) 2, x);
            return 3;
        })
        .done((x) -> assertEquals((Integer) 3, x));
```

## The library also includes HTTP helper methods such as

- HTTP Get with String response

```java
HTTPPromise.get("http://api.openweathermap.org/data/2.5/weather?q=London,uk")
    .done((result) -> assertNotNull(result));
```

- HTTP Get with JSON response

```java
HTTPPromise.getJSON("http://api.openweathermap.org/data/2.5/weather?q=London,uk")
    .done((result) -> assertNotNull(result.get("name")));
```

- HTTP POST JSON with JSON response

```java
JSONObject data = new JSONObject();
data.put("title", "foo");
data.put("body", "bar");
data.put("userId", "1");

HTTPPromise.postJSON("http://jsonplaceholder.typicode.com/posts", data)
    .done((result) -> assertEquals("foo", result.get("title")));
```