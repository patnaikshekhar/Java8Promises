package com.shekharpatnaik.futures.interfaces;

/**
 * Created by shpatnaik on 1/27/15.
 */
@FunctionalInterface
public interface SupplierWithError<T> {
    public T get() throws Exception;
}
