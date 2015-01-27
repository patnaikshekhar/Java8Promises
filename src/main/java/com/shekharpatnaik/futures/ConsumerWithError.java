package com.shekharpatnaik.futures;

/**
 * Created by shpatnaik on 1/27/15.
 */
@FunctionalInterface
public interface ConsumerWithError<T> {

    public T accept() throws Exception;
}
