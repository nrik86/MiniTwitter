package com.enriquejimenez.minitwitter.retrofit;

import com.enriquejimenez.minitwitter.utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthMiniTwitterClient {

    private static AuthMiniTwitterClient instance = null;
    private AuthMiniTwitterService authMiniTwitterService;

    private Retrofit retrofit;

    public AuthMiniTwitterClient(){
        //INCLUIR EN LA CABEZERA DE LA PETICION EL TOKEN QUE AUTORIZA AL USER
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new AuthInterceptor());
        OkHttpClient client = okHttpClientBuilder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        authMiniTwitterService = retrofit.create(AuthMiniTwitterService.class);
    }


    public static AuthMiniTwitterClient getInstance(){
        if (instance == null){
            instance = new AuthMiniTwitterClient();
        }
        return instance;
    }

    public AuthMiniTwitterService getMiniTwitterService(){
        return authMiniTwitterService;
    }



}
