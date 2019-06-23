package com.enriquejimenez.minitwitter.retrofit;

import com.enriquejimenez.minitwitter.retrofit.request.RequestNewTweet;
import com.enriquejimenez.minitwitter.retrofit.request.RequestUserProfile;
import com.enriquejimenez.minitwitter.retrofit.response.DeletedTweet;
import com.enriquejimenez.minitwitter.retrofit.response.ResponseUploadPhoto;
import com.enriquejimenez.minitwitter.retrofit.response.ResponseUserProfile;
import com.enriquejimenez.minitwitter.retrofit.response.Tweet;
import com.enriquejimenez.minitwitter.retrofit.response.User;
import com.enriquejimenez.minitwitter.utils.Constants;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AuthMiniTwitterService {

    //TWEETS

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


    //USERS
    @GET(Constants.USER_PROFILE)
    Call<ResponseUserProfile> getUserProfile();

    @PUT(Constants.USER_PROFILE)
    Call<ResponseUserProfile> updateProfile(@Body RequestUserProfile requestUserProfile);

    @Multipart
    @POST(Constants.UPLOAD_PROFILE_PHOTO)
    Call<ResponseUploadPhoto> uploadProfilePhoto(@Part("file\"; filename\"photo.jpeg\" ")RequestBody file);
}
