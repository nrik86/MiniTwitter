package com.enriquejimenez.minitwitter.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.widget.Toast;

import com.enriquejimenez.minitwitter.retrofit.AuthMiniTwitterClient;
import com.enriquejimenez.minitwitter.retrofit.AuthMiniTwitterService;
import com.enriquejimenez.minitwitter.retrofit.response.Tweet;
import com.enriquejimenez.minitwitter.ui.adapter.MyTweetRecyclerViewAdapter;
import com.enriquejimenez.minitwitter.utils.MiniTwitterApp;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    private AuthMiniTwitterService authMiniTwitterService;
    private AuthMiniTwitterClient authMiniTwitterClient;
    private LiveData<List<Tweet>> allTweets;

    public TweetRepository(){
        authMiniTwitterClient = AuthMiniTwitterClient.getInstance();
        authMiniTwitterService = authMiniTwitterClient.getMiniTwitterService();
        allTweets = getAllTweets();
    }

    public LiveData<List<Tweet>> getAllTweets() {
        final MutableLiveData<List<Tweet>> data = new MutableLiveData<>();
        Call<List<Tweet>> call = authMiniTwitterService.getAllTweets();

        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if(response.isSuccessful()){
                    data.setValue(response.body());
                }else{
                    Toast.makeText(MiniTwitterApp.getContext(),"Algo ha ido mal...", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MiniTwitterApp.getContext(),"Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
        return data;

    }

}
