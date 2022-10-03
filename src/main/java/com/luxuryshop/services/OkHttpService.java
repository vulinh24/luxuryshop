package com.luxuryshop.services;

import okhttp3.*;
import org.springframework.stereotype.Service;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class OkHttpService {

    private static ConnectionPool connectionPool = new ConnectionPool(1000, 15, SECONDS);

    public OkHttpClient getClient() {
        return new OkHttpClient.Builder().connectionPool(connectionPool).build();
    }

    public String get(String url, String accessToken) throws Exception {

        Request request;
        if (accessToken == null)
            request = new Request.Builder().url(url)
                    .get().build();
        else request = new Request.Builder().url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .get().build();
        try (Response response = getClient().newCall(request).execute()) {
            String result = response.body().string();
            response.body().close();
            return result;
        }
    }

    public String get(String url) throws Exception {
        Request request;
        request = new Request.Builder().url(url)
                .get().build();
        try (Response response = getClient().newCall(request).execute()) {
            String result = response.body().string();
            response.body().close();
            return result;
        }
    }

    public String post(String url, String jsonBody) throws Exception {
        MediaType json = MediaType.parse("application/json;charset=utf-8");
        Request request;
        RequestBody body = jsonBody == null ? RequestBody.create(json, "") : RequestBody.create(json, jsonBody);
        request = new Request.Builder().url(url).post(body).build();
        try (Response response = getClient().newCall(request).execute()) {
            String result = response.body().string();
            response.body().close();
            return result;
        } catch (Exception exception) {
            return null;
        }
    }


}
