package com.shekharpatnaik.futures;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by shpatnaik on 1/25/15.
 */
public class Promise<T> {

    private Consumer<T> callback;
    private Consumer<Exception> errorCallback;
    private T result;
    private Exception exceptionResult;

    public void done(Consumer<T> callback) {
        this.callback = callback;

        if (result != null) {
            this.callback.accept(result);
        }
    }

    public void error(Consumer<Exception> callback) {
        this.errorCallback = callback;

        if (this.exceptionResult != null) {
            this.errorCallback.accept(exceptionResult);
        }
    }

    public Promise(SupplierWithError<T> f) throws Exception {
        Thread thread = new Thread(() -> {
            try {
                if (this.callback != null) {
                    this.callback.accept(f.get());
                } else {
                    result = f.get();
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
}
