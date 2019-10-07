package com.ashiqur.weatherapp.rest_api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    public static String baseUrl = "http://api.openweathermap.org/data/2.5/";//"http://192.168.43.158:8000/"; //
    public static Retrofit retrofit = null;
    public static String apiKey = "31de1dc4507ce4031564ab9b4a1532f2";

    private static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("APPID", apiKey)
                    .build();
            return chain.proceed(newRequest);
        }
    }).build();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientWithAuth() {
        // Adds Authorization header
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static void setApiKey(String apiKey) {
        ApiClient.apiKey = apiKey;
    }
}
