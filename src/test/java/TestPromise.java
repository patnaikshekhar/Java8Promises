import com.shekharpatnaik.futures.Promise;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by shpatnaik on 1/25/15.
 */
public class TestPromise {
    
    @Test
    public void Future_CallFutureWithFunction_CallsCallbackwhenComplete() throws Exception {
        // Promise to wait
        Promise<Integer> p = new Promise<>(() -> 0);
        p.done((x) -> assertEquals((Integer) 0, x));
    }

    @Test
    public void Future_CallFutureWithErrorFunction_CallsErrorCallback() throws Exception {
        Promise<Integer> p = new Promise<>(() -> {
            throw new Exception("Exception");
            //return 0;
        });

        p.error((e) -> assertEquals("Exception", e.getMessage()));
    }
}
