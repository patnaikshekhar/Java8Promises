import com.shekharpatnaik.futures.HTTPPromise;
import com.shekharpatnaik.futures.Promise;
import jdk.nashorn.api.scripting.JSObject;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by shpatnaik on 1/27/15.
 * End to End Integration tests for HTTPPromise API
 */
public class TestHTTPPromise {

    @After
    public void tearDown() {
        Promise.waitForAllThreadsToComplete();
    }

    @Test
    public void HTTPPromise_getURLAsString_ReturnsString() throws Exception {
        HTTPPromise.get("http://api.openweathermap.org/data/2.5/weather?q=London,uk")
                .done((result) -> assertNotNull(result));
    }

    @Test
    public void HTTPPromise_getURLAsJSONObject_ReturnsJSONObject() throws Exception {
        HTTPPromise.getJSON("http://api.openweathermap.org/data/2.5/weather?q=London,uk")
                .done((result) -> assertNotNull(result.get("name")));
    }

    @Test
    public void HTTPPromise_postJSONToURL_ReturnsJSONObject() throws Exception {
        JSONObject data = new JSONObject();
        data.put("title", "foo");
        data.put("body", "bar");
        data.put("userId", "1");

        HTTPPromise.postJSON("http://jsonplaceholder.typicode.com/posts", data)
                .done((result) -> assertEquals("foo", result.get("title")));
    }
}
