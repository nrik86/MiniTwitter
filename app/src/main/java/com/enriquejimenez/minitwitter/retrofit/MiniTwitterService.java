package com.enriquejimenez.minitwitter.retrofit;

import com.enriquejimenez.minitwitter.retrofit.request.RequestLogin;
import com.enriquejimenez.minitwitter.retrofit.request.RequestSignUp;
import com.enriquejimenez.minitwitter.retrofit.response.ResponseAuth;
import com.enriquejimenez.minitwitter.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MiniTwitterService {

    @POST(Constants.AUTH_LOGIN_URL)
    Call<ResponseAuth> doLogin (@Body RequestLogin requestLogin);

    @POST(Constants.AUTH_SIGN_UP_URL)
    Call<ResponseAuth> doSignUp (@Body RequestSignUp requestSignUp);


}
