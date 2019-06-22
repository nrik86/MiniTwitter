package com.enriquejimenez.minitwitter.retrofit;

import com.enriquejimenez.minitwitter.retrofit.response.Tweet;
import com.enriquejimenez.minitwitter.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AuthMiniTwitterService {

    @GET(Constants.ALL_TWEETS_URL)
    Call<List<Tweet>> getAllTweets();
}
