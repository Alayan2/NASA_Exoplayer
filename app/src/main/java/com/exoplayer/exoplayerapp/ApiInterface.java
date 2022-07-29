package com.exoplayer.exoplayerapp;

import retrofit2.Call;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("search?q=")
    Call<Collection> getMovies(@Query("description") String page, @Query("media_type") String a);

}