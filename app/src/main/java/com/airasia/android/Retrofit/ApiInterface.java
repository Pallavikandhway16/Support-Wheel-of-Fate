package com.airasia.android.Retrofit;

import com.airasia.android.Data.EngineersList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    // get saved stations
    @GET("engineers")
    Call<EngineersList> getEngineers();
}
