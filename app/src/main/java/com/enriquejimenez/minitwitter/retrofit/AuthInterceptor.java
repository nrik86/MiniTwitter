package com.enriquejimenez.minitwitter.retrofit;

import com.enriquejimenez.minitwitter.utils.Constants;
import com.enriquejimenez.minitwitter.utils.SharedPreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SharedPreferencesManager.getString(Constants.PREF_TOKEN);
        Request request = chain.request().newBuilder().addHeader(Constants.AUTH, "Bearer "+token).build();
        return chain.proceed(request);
    }
}
