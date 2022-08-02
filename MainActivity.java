package com.exoplayer.exoplayerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {
    Collection movieList;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(),movieList);
        recyclerView.setAdapter(recyclerAdapter);


        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest.newBuilder();
                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        //
        ApiInterface apiService = ApiClient.getClient(okHttpClient).create(ApiInterface.class);
        searchVideos(apiService, "apollo");

        final EditText ET = (EditText) findViewById(R.id.searchbartextView);
        Button button = (Button) findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String searchTerm = ET.getText().toString();
                searchVideos(apiService, searchTerm);
            }
        });

    }

    public void searchVideos(ApiInterface a, String s) {
        Call<Collection> call = a.getMovies(s, "video");

        call.enqueue(new Callback<Collection>() {
            @Override
            public void onResponse(Call<Collection> call, Response<Collection> response) {
                if (response.isSuccessful()) {
                    movieList = response.body();

                    //if search results are empty, toast notification prompts
                    // user to try another search
                    if(movieList.getCollection().getItems().isEmpty()) {
                        Toast.makeText(MainActivity.this, "No results for '" + s + ",' try another keyword search.", Toast.LENGTH_LONG).show();
                    }

                    //recyclerAdapter is set with contents of movieList collection
                    recyclerAdapter.setMovieList(movieList);

                }
            }

            @Override
            public void onFailure(Call<Collection> call, Throwable t) {
                Log.d("TAG","Response = "+t.toString());
            }

        });
    }
}