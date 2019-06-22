package com.enriquejimenez.minitwitter.retrofit;

import com.enriquejimenez.minitwitter.retrofit.request.RequestNewTweet;
import com.enriquejimenez.minitwitter.retrofit.response.DeletedTweet;
import com.enriquejimenez.minitwitter.retrofit.response.Tweet;
import com.enriquejimenez.minitwitter.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthMiniTwitterService {

    @GET(Constants.ALL_TWEETS_URL)
    Call<List<Tweet>> getAllTweets();

    @GET(Constants.ALL_TWEETS_URL)
    Call<List<Tweet>> getAllFavsTweets();

    @POST(Constants.CREATE_TWEET_URL)
    Call<Tweet> createTweet(@Body RequestNewTweet requestNewTweet);

    @POST(Constants.LIKE_TWEET_URL)
    Call<Tweet> likeTweet(@Path("idTweet") int idTweet);

    @DELETE(Constants.DELETE_TWEET)
    Call<DeletedTweet> deleteTweet(@Path("id") int id);
}
