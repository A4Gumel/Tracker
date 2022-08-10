package com.eamatracker.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // The Main API service client that support the app

    private static final String BASE_URL = "https://<your-domain>/";
    private static RetrofitClient mInstance;
    private Retrofit mRetrofit;


    private RetrofitClient(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if (mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public APIService getApiService(){
        return mRetrofit.create(APIService.class);
    }
}
