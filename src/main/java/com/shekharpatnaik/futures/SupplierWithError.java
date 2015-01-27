package com.shekharpatnaik.futures;

/**
 * Created by shpatnaik on 1/27/15.
 */
@FunctionalInterface
public interface SupplierWithError<T> {
    public T get() throws Exception;
}
