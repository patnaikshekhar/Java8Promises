package com.shekharpatnaik.futures;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shpatnaik on 1/27/15.
 */
public class HTTPPromise {
    public static Promise<String> get(String urlString) throws Exception {
        return new Promise<>(() -> getURLResultAsString(urlString));
    }

    private static String getURLResultAsString(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String result = "";

        String l;
        while((l = br.readLine()) != null) {
            result += l;
        }

        br.close();

        return result;
    }

    public static Promise<JSONObject> getJSON(String urlString) throws Exception {
        return new Promise<>(() -> new JSONObject(getURLResultAsString(urlString)));
    }

    public static Promise<JSONObject> postJSON(String urlString, JSONObject data) throws Exception {
        return new Promise<>(() -> {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStream os = connection.getOutputStream();
            os.write(data.toString().getBytes());
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String result = "";

            String l;
            while((l = br.readLine()) != null) {
                result += l;
            }

            br.close();

            return new JSONObject(result);
        });
    }
}
