# Simple Promises Library for Java

## Usage Examples

- Simple Done Scenario
The done callback is called after the promise is complete

```java
Promise<Integer> p = new Promise<>(() -> 0);
p.done((x) -> assertEquals((Integer) 0, x));
```

- Simple Error Scenario
The error callback is when an error occurs in the chain.

```java
Promise<Integer> p = new Promise<>(() -> {
    throw new Exception("Exception");
});

p.error((e) -> assertEquals("Exception", e.getMessage()));
```

- Promises can be chained with then
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

- Promises can be executed in parallel and results can be returned in a list

```java
Promise.All(() -> 1, () -> 2, () -> 3).done((lst) -> {
    assertEquals(3, lst.size());
    assertTrue(lst.contains(1));
    assertTrue(lst.contains(2));
    assertTrue(lst.contains(3));
});
```

- Promises can be executed in parallel and the result of the fastest one can be used

```java
Promise.Any(() -> 1, () -> 2, () -> 3).done((element) -> {
    assertTrue(element == 1 || element == 2 || element == 3);
});
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