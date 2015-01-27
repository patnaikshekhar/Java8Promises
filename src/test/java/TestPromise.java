import com.shekharpatnaik.futures.Promise;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by shpatnaik on 1/25/15.
 */
public class TestPromise {

    @After
    public void tearDown() {
        Promise.waitForAllThreadsToComplete();
    }

    @Test
    public void Promise_CallFutureWithFunction_CallsCallbackwhenComplete() throws Exception {
        // Promise to wait
        Promise<Integer> p = new Promise<>(() -> 0);
        p.done((x) -> assertEquals((Integer) 0, x));
    }

    @Test
    public void Promise_CallFutureWithErrorFunction_CallsErrorCallback() throws Exception {
        Promise<Integer> p = new Promise<>(() -> {
            throw new Exception("Exception");
            //return 0;
        });

        p.error((e) -> assertEquals("Exception", e.getMessage()));
    }

    @Test
    public void Promise_CallPromiseWithThen_ExecutesTheThenFunction() throws Exception {
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
    }
}
