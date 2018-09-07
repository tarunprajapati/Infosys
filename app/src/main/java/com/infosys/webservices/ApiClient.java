package com.infosys.webservices;


import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infosys.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String SERVER = BuildConfig.HOST;
    private Retrofit retrofit = null;
    private Retrofit retrofitToken;

    public static ApiClient newApiClient() {
        ApiClient apiClient = new ApiClient();
        return apiClient;
    }

    public Retrofit getClient() {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getHttpClient().build())
                    .build();
        return retrofit;
    }

    public void resetClient() {
        //apiClient = null;
        retrofit = null;
        retrofitToken = null;
    }

    public Retrofit getClient(String url) {
       // if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient().build())
                    .build();
        //}
        return retrofit;
    }

    public Retrofit getClientStoken(Context context) {
       // if (retrofitToken == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofitToken = new Retrofit.Builder()
                    .baseUrl(SERVER)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getHttpClient(context).build())
                    .build();
      //  }
        return retrofitToken;
    }

    @NonNull
    private OkHttpClient.Builder getHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        httpClient.connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES);
        return httpClient;
    }

    public ApiInterface getInterface() {
        return getClient().create(ApiInterface.class);
    }

    /*public static ApiInterface getInterface(String url) {
        return getClient(url).create(ApiInterface.class);
    }*/

    public ApiInterface getInterfaceSToken(Context context) {
        return getClientStoken(context).create(ApiInterface.class);
    }

    @NonNull
    private OkHttpClient.Builder getHttpClient(final Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        // add logging as last interceptor
        //httpClient.addInterceptor(logging);  // <-- this is the important line!
        httpClient.connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES);
        return httpClient;
    }
}
