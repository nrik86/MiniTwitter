package com.enriquejimenez.minitwitter.data;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.enriquejimenez.minitwitter.retrofit.response.Tweet;

import java.util.List;

public class TweetViewModel extends AndroidViewModel {

    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> tweets;
    private LiveData<List<Tweet>> favTweets;

    public TweetViewModel(@NonNull Application application) {
        super(application);
        tweetRepository = new TweetRepository();
        tweets = tweetRepository.getAllTweets();
    }

    public LiveData<List<Tweet>> getTweets(){
        return tweets;
    }

    public LiveData<List<Tweet>> getFavTweets(){
        favTweets = tweetRepository.getAllFavTweets();
        return favTweets;
    }

    public LiveData<List<Tweet>> getNewTweets(){
        tweets = tweetRepository.getAllTweets();
        return tweets;
    }

    public LiveData<List<Tweet>> getNewFavsTweets(){
        getNewTweets();
        return getFavTweets();
    }

    public void insertTweet(String message){
        tweetRepository.createTweet(message);
    }

    public void deleteTweet(int id){
        tweetRepository.deleteTweet(id);
    }

    public void likeTweet(int idTweet){
        tweetRepository.likeTweet(idTweet);
    }

}
