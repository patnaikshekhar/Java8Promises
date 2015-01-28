package com.shekharpatnaik.futures;

import com.shekharpatnaik.futures.interfaces.FunctionWithError;
import com.shekharpatnaik.futures.interfaces.SupplierWithError;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by shpatnaik on 1/25/15.
 * This is a promises library with allows a user to execute functions concurrently
 */
public class Promise<T> {

    private Consumer<T> callback;
    private Consumer<Exception> errorCallback;
    private T result;
    private Exception exceptionResult;
    private SupplierWithError<T> executableFunction;
    private FunctionWithError<T> executableFunctionWithArguments;
    private Promise<T> chainedPromise;
    private static List<Thread> allThreads = new Vector<>();

    public void done(Consumer<T> callback) {
        this.callback = callback;

        if (result != null) {
            this.callback.accept(result);
        }
    }

    public Promise<T> then(FunctionWithError<T> f) throws Exception {
        this.chainedPromise = new Promise<>(f);

        if (result != null) {
            this.chainedPromise.execute(result);
        }

        return this.chainedPromise;
    }

    public void error(Consumer<Exception> callback) {
        this.errorCallback = callback;

        if (this.exceptionResult != null) {
            this.errorCallback.accept(exceptionResult);
        }
    }

    public void execute() {
        execute(null);
    }

    public void execute(T data) {
        Thread thread = new Thread(() -> {
            try {

                if (this.executableFunction != null) {
                    result = this.executableFunction.get();
                } else if (this.executableFunctionWithArguments != null) {
                    result = this.executableFunctionWithArguments.apply(data);
                }

                if (this.chainedPromise != null) {
                    this.chainedPromise.execute(result);
                } else if (this.callback != null) {
                    this.callback.accept(result);
                }

            } catch(Exception e) {

                if (this.errorCallback != null) {
                    this.errorCallback.accept(e);
                } else {
                    exceptionResult = e;
                }
            }
        });

        allThreads.add(thread);
        thread.start();
    }

    public Promise(SupplierWithError<T> f) throws Exception {
        this.executableFunction = f;
        this.execute();
    }

    public Promise(FunctionWithError<T> f) throws Exception {
        this.executableFunctionWithArguments = f;
    }

    public static void waitForAllThreadsToComplete() {
        allThreads.forEach((thread) -> {
            try {
                thread.join();
            } catch(Exception e) {
                // Silently fail
            }
        });
    }

    private static <T> Promise<List<T>> All(List<Promise<T>> promises) throws Exception {

        List results = new Vector<>();

        return new Promise<>(() -> {
            promises.forEach((promise) -> promise.done((x) -> {
                results.add(x);
            }));

            // Wait infinitely until the results are obtained
            while(results.size() < promises.size()) {
                Thread.sleep(100);
            }

            return results;
        });

    }

    public static <T> Promise<List<T>> All(Promise<T>... promises) {
        return All(promises);
    }

    public static <T> Promise<List<T>> All(SupplierWithError<T>... lambdas) throws Exception {
        List<Promise<T>> promises = Arrays.asList(lambdas).stream().map((x) -> {
            try {
                return new Promise<T>(x);
            } catch (Exception e) {
                return null;
            }
        }).collect(Collectors.toList());

        return All(promises);
    }

    private static <T> Promise<T> Any(List<Promise<T>> promises) throws Exception {

        List<T> results = new Vector<>();

        return new Promise<>(() -> {
            promises.forEach((promise) -> promise.done((x) -> {
                results.add(x);
            }));

            // Wait infinitely until the results are obtained
            while(results.size() <= 0) {
                Thread.sleep(100);
            }

            return results.get(0);
        });

    }

    public static <T> Promise<T> Any(Promise<T>... promises) {
        return Any(promises);
    }

    public static <T> Promise<T> Any(SupplierWithError<T>... lambdas) throws Exception {
        List<Promise<T>> promises = Arrays.asList(lambdas).stream().map((x) -> {
            try {
                return new Promise<T>(x);
            } catch (Exception e) {
                return null;
            }
        }).collect(Collectors.toList());

        return Any(promises);
    }
}
