package com.surveyor.app.surveyorapp.retrofit;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    public Context context;
    private static ApiService REST_CLIENT;
    SharedObjects sharedObjects ;

    public RestClient(Context context) {
        this.context = context ;
        sharedObjects = new SharedObjects(context);
        setupRestClient();
    }

    public static ApiService get() {
        return REST_CLIENT;
    }

    public static ApiService getMain(Context context) {

        return REST_CLIENT;
    }

    public static ApiService post() {
        return REST_CLIENT;
    }

    private void setupRestClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .addInterceptor(new BasicAuthInterceptor(sharedObjects.getPreference(AppConstants.USERNAME), sharedObjects.getPreference(AppConstants.PASSWORD)))
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder() .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASEURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        REST_CLIENT = retrofit.create(ApiService.class);
    }

}

