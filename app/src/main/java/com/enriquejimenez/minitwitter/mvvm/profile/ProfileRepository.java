package com.enriquejimenez.minitwitter.mvvm.profile;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.enriquejimenez.minitwitter.retrofit.AuthMiniTwitterClient;
import com.enriquejimenez.minitwitter.retrofit.AuthMiniTwitterService;
import com.enriquejimenez.minitwitter.retrofit.request.RequestNewTweet;
import com.enriquejimenez.minitwitter.retrofit.request.RequestUserProfile;
import com.enriquejimenez.minitwitter.retrofit.response.DeletedTweet;
import com.enriquejimenez.minitwitter.retrofit.response.Like;
import com.enriquejimenez.minitwitter.retrofit.response.ResponseUploadPhoto;
import com.enriquejimenez.minitwitter.retrofit.response.ResponseUserProfile;
import com.enriquejimenez.minitwitter.retrofit.response.Tweet;
import com.enriquejimenez.minitwitter.retrofit.response.User;
import com.enriquejimenez.minitwitter.utils.Constants;
import com.enriquejimenez.minitwitter.utils.MiniTwitterApp;
import com.enriquejimenez.minitwitter.utils.SharedPreferencesManager;

import java.io.CharArrayReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    private AuthMiniTwitterService authMiniTwitterService;
    private AuthMiniTwitterClient authMiniTwitterClient;
    private MutableLiveData<ResponseUserProfile> profile;
    private MutableLiveData<String> photoProfile;


    public ProfileRepository(){
        authMiniTwitterClient = AuthMiniTwitterClient.getInstance();
        authMiniTwitterService = authMiniTwitterClient.getMiniTwitterService();
        profile = getProfile();
        if(photoProfile == null){
            photoProfile = new MutableLiveData<>();
        }
    }

    public MutableLiveData<ResponseUserProfile> getProfile() {
        if(profile == null) {
            profile = new MutableLiveData<>();
        }

        Call<ResponseUserProfile> call = authMiniTwitterService.getUserProfile();
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if(response.isSuccessful()) {
                    profile.setValue(response.body());
                }else{
                    Toast.makeText(MiniTwitterApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MiniTwitterApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

        return profile;
    }

    public MutableLiveData<String> getPhotoProfile(){
        return photoProfile;
    }


    public void updateProfile(RequestUserProfile requestUserProfile) {
        Call<ResponseUserProfile> call = authMiniTwitterService.updateProfile(requestUserProfile);
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if(response.isSuccessful()) {
                    profile.setValue(response.body());
                }else{
                    Toast.makeText(MiniTwitterApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MiniTwitterApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void uploadPhoto(String photoPath){
        File file = new File(photoPath);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("image.jpg"),file);
        Call<ResponseUploadPhoto> call = authMiniTwitterService.uploadProfilePhoto(requestBody);
        call.enqueue(new Callback<ResponseUploadPhoto>() {
            @Override
            public void onResponse(Call<ResponseUploadPhoto> call, Response<ResponseUploadPhoto> response) {
                if (response.isSuccessful()){
                    SharedPreferencesManager.setString(Constants.PREF_URL_PHOTO, response.body().getFilename());
                    photoProfile.setValue(response.body().getFilename());
                }else{
                    Toast.makeText(MiniTwitterApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseUploadPhoto> call, Throwable t) {
                Toast.makeText(MiniTwitterApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
