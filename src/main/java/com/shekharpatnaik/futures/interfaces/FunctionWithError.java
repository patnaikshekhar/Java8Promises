package com.shekharpatnaik.futures.interfaces;

/**
 * Created by shpatnaik on 1/27/15.
 */
@FunctionalInterface
public interface FunctionWithError<T> {
    public T apply(T data) throws Exception;
}
