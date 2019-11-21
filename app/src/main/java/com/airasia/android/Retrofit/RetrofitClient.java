package com.airasia.android.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


// singleton class
public class RetrofitClient {
    private static final String BASE_URL = "http://private-dd0f0-engineers3.apiary-mock.com/";
    private static RetrofitClient mInstance;
    // Retrofit object
    private Retrofit mRetrofit;


    public RetrofitClient() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // get single intance
    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    // get api
    public ApiInterface getApi() {
        return mRetrofit.create(ApiInterface.class);
    }
}
