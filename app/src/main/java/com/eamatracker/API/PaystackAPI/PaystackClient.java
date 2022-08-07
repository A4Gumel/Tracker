package com.eamatracker.API.PaystackAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaystackClient {


    // Payment Gatewaye API Client
    private static final String BASE_URL = "https://api.paystack.co/";
    private static PaystackClient mInstance;
    private Retrofit mRetrofit;


    private PaystackClient() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized PaystackClient getInstance() {
        if (mInstance == null) {
            mInstance = new PaystackClient();
        }
        return mInstance;
    }

    public PaystackService getApiService() {
        return mRetrofit.create(PaystackService.class);
    }
}
