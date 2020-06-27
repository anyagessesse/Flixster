package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";  //TODO sign up for my own api key
    public static final String TAG = "MainActivity";
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //creating binding to replace findViewById()
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        RecyclerView rvMovies = binding.rvMovies;

        //adding a divider to the recyclerview
        DividerItemDecoration divider = new DividerItemDecoration(rvMovies.getContext(),DividerItemDecoration.VERTICAL);
        rvMovies.addItemDecoration(divider);
        movies = new ArrayList<>();

        //create the adaptor
        final MovieAdapter movieAdapter = new MovieAdapter(this,movies);
        //set the adaptor on the recycler view
        rvMovies.setAdapter(movieAdapter);
        //set a layout manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG,"onSuccess");
                JSONObject jsonObject = json.jsonObject;

                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG,"Results:" + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG,"Movies:" + movies.toString());
                } catch (JSONException e) {
                    Log.e(TAG,"Hit json exception",e);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG,"onFailure");

            }
        });

    }
}