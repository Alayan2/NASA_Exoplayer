package com.exoplayer.exoplayerapp;

import retrofit2.Call;

import retrofit2.Response;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("search?q=apollo%2011&description=moon%20landing&media_type=video")
    Call<Collection> getMovies();
}