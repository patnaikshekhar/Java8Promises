package com.shekharpatnaik.futures.examples;

import com.shekharpatnaik.futures.HTTPPromise;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by shpatnaik on 1/27/15.
 */
public class App {

    public static void HTTPPromise_URLGet() throws Exception {
        HTTPPromise.get("http://api.openweathermap.org/data/2.5/weather?q=London,uk")
                .done((String result) -> {
                    System.out.println(result);
                    assertNotNull(result);
                });
    }

    public static void main(String[] args) throws Exception {
        HTTPPromise_URLGet();
    }
}
