package com.shekharpatnaik.futures;

import com.shekharpatnaik.futures.interfaces.FunctionWithError;
import com.shekharpatnaik.futures.interfaces.SupplierWithError;

import java.util.function.Consumer;

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

        thread.start();
    }

    public Promise(SupplierWithError<T> f) throws Exception {
        this.executableFunction = f;
        this.execute();
    }

    public Promise(FunctionWithError<T> f) throws Exception {
        this.executableFunctionWithArguments = f;
    }
}
