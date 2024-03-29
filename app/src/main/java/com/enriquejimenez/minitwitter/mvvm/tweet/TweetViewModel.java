package com.enriquejimenez.minitwitter.mvvm.tweet;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.enriquejimenez.minitwitter.retrofit.response.Tweet;
import com.enriquejimenez.minitwitter.ui.tweets.fragment.BottomModalTweetFragment;

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

    public void openDialogTweetMenu(Context context, int idTweet){
        BottomModalTweetFragment dialogTweet = BottomModalTweetFragment.newInstance(idTweet);
        dialogTweet.show(((AppCompatActivity)context).getSupportFragmentManager(), "BottomModalTweetFragment");
    }

}
