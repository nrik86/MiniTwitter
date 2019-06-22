package com.enriquejimenez.minitwitter.mvvm.tweet;

import androidx.lifecycle.MutableLiveData;
import android.widget.Toast;

import com.enriquejimenez.minitwitter.retrofit.AuthMiniTwitterClient;
import com.enriquejimenez.minitwitter.retrofit.AuthMiniTwitterService;
import com.enriquejimenez.minitwitter.retrofit.request.RequestNewTweet;
import com.enriquejimenez.minitwitter.retrofit.response.DeletedTweet;
import com.enriquejimenez.minitwitter.retrofit.response.Like;
import com.enriquejimenez.minitwitter.retrofit.response.Tweet;
import com.enriquejimenez.minitwitter.utils.Constants;
import com.enriquejimenez.minitwitter.utils.MiniTwitterApp;
import com.enriquejimenez.minitwitter.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    private AuthMiniTwitterService authMiniTwitterService;
    private AuthMiniTwitterClient authMiniTwitterClient;
    private MutableLiveData<List<Tweet>> allTweets;
    private MutableLiveData<List<Tweet>> allFavTweets;

    private String ownUserName;

    public TweetRepository(){
        authMiniTwitterClient = AuthMiniTwitterClient.getInstance();
        authMiniTwitterService = authMiniTwitterClient.getMiniTwitterService();
        allTweets = getAllTweets();
        ownUserName = SharedPreferencesManager.getString(Constants.PREF_USER_NAME);
    }

    public MutableLiveData<List<Tweet>> getAllTweets() {
        if(allTweets == null) {
            allTweets = new MutableLiveData<>();
        }

        Call<List<Tweet>> call = authMiniTwitterService.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if(response.isSuccessful()) {
                    allTweets.setValue(response.body());
                } else {
                    Toast.makeText(MiniTwitterApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MiniTwitterApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

        return allTweets;
    }

    public MutableLiveData<List<Tweet>> getAllFavTweets() {
        if(allFavTweets == null) {
            allFavTweets = new MutableLiveData<>();
        }

        List<Tweet> newFavList = new ArrayList<>();
        Iterator itTweets = allTweets.getValue().iterator();

        while(itTweets.hasNext()) {
            Tweet current = (Tweet) itTweets.next();
            Iterator itLikes = current.getLikes().iterator();
            boolean enc = false;
            while (itLikes.hasNext() && !enc) {
                Like like = (Like)itLikes.next();
                if(like.getUsername().equals(ownUserName)) {
                    enc = true;
                    newFavList.add(current);
                }
            }
        }

        allFavTweets.setValue(newFavList);

        return allFavTweets;
    }


//    public MutableLiveData<List<Tweet>> getAllFavTweets() {
//
//        if(allFavTweets == null){
//            allFavTweets = new MutableLiveData<>();
//        }
//
//        Call<List<Tweet>> call = authMiniTwitterService.getAllFavsTweets();
//
//        call.enqueue(new Callback<List<Tweet>>() {
//            @Override
//            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
//                if(response.isSuccessful()){
//                    allFavTweets.setValue(response.body());
//                }else{
//                    Toast.makeText(MiniTwitterApp.getContext(),"Algo ha ido mal...", Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Tweet>> call, Throwable t) {
//                Toast.makeText(MiniTwitterApp.getContext(),"Error de conexión", Toast.LENGTH_SHORT).show();
//            }
//        });
//        return allFavTweets;
//
//    }

    public void createTweet(String message){
        RequestNewTweet requestNewTweet = new RequestNewTweet(message);
        Call<Tweet> call = authMiniTwitterService.createTweet(requestNewTweet);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if (response.isSuccessful()){
                    List<Tweet> cloneTweetList = new ArrayList<>();
                    //Añadimos en primer lugar el nuevo tweet que nos llega del servidor
                    cloneTweetList.add(response.body());
                    for(int i=0;i< allTweets.getValue().size();i++){
                        cloneTweetList.add(new Tweet(allTweets.getValue().get(i)));
                    }
                    allTweets.setValue(cloneTweetList);
                }else{
                    Toast.makeText(MiniTwitterApp.getContext(),"Algo ha ido mal...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MiniTwitterApp.getContext(),"Error de conexión", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void deleteTweet(final int idTweet){
        Call<DeletedTweet> call = authMiniTwitterService.deleteTweet(idTweet);
        call.enqueue(new Callback<DeletedTweet>() {
            @Override
            public void onResponse(Call<DeletedTweet> call, Response<DeletedTweet> response) {
                if (response.isSuccessful()){
                    List<Tweet> clonedTweets = new ArrayList<>();
                    for(int i = 0 ; i < allTweets.getValue().size(); i++){
                        if(allTweets.getValue().get(i).getId() != idTweet){
                            clonedTweets.add(new Tweet(allTweets.getValue().get(i)));
                        }
                    }
                    allTweets.setValue(clonedTweets);
                    getAllFavTweets();

                }else{
                    Toast.makeText(MiniTwitterApp.getContext(),"Algo ha ido mal...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeletedTweet> call, Throwable t) {
                Toast.makeText(MiniTwitterApp.getContext(),"Error de conexión", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void likeTweet(final int idTweet){
        Call<Tweet> call = authMiniTwitterService.likeTweet(idTweet);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if (response.isSuccessful()){
                    List<Tweet> cloneTweetList = new ArrayList<>();
                    //Añadimos en primer lugar el nuevo tweet que nos llega del servidor
                    for(int i=0;i< allTweets.getValue().size();i++){

                        if(allTweets.getValue().get(i).getId() == idTweet ){
                            //Si hemos encontrado en la lista original el elemento sobre el que hemos
                            //hecho like, introducimos el elemento que nos ha llegado del servidor
                            cloneTweetList.add(response.body());
                        }else{
                            cloneTweetList.add(new Tweet(allTweets.getValue().get(i)));
                        }
                    }
                    allTweets.setValue(cloneTweetList);
                    //Para introducir el nuevo favorito a la lista
                    getAllFavTweets();
                }else{
                    Toast.makeText(MiniTwitterApp.getContext(),"Algo ha ido mal...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MiniTwitterApp.getContext(),"Error de conexión", Toast.LENGTH_SHORT).show();

            }
        });
    }



}
