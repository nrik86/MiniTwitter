package com.enriquejimenez.minitwitter.utils;

public class Constants {

    public static final String API_MINITWITTER_BASE_URL = "https://www.minitwitter.com:3001/apiv1/";

    public static final String AUTH_LOGIN_URL = "auth/login";
    public static final String AUTH_SIGN_UP_URL = "auth/signup";

    public static final String ALL_TWEETS_URL = "tweets/all";
    public static final String ALL_FAV_TWEETS_URL = "tweets/favs";
    public static final String CREATE_TWEET_URL = "tweets/create";
    public static final String LIKE_TWEET_URL = "tweets/like/{idTweet}";
    public static final String DELETE_TWEET = "tweets/{id}";
    public static final String PHOTO_URL = "https://www.minitwitter.com/apiv1/uploads/photos/";


    public static final String AUTH = "Authorization";

    //PREFERENCES
    public static final String PREF_TOKEN = "PREF_TOKEN";
    public static final String PREF_USER_NAME = "PREF_USER_NAME";
    public static final String PREF_EMAIL = "PREF_EMAIL";
    public static final String PREF_PASS = "PREF_PASS";
    public static final String PREF_URL_PHOTO = "PREF_URL_PHOTO";
    public static final String PREF_CREATED = "PREF_CREATED";
    public static final String PREF_ACTIVE = "PREF_ACTIVE";

    //ARGUMENTS
    public static final String TWEET_LIST_TYPE = "TWEET_LIST_TYPE";
    public static final int TWEET_LIST_ALL = 1;
    public static final int TWEET_LIST_FAVS = 2;


    public static final String ARG_TWEET_ID = "TWEET_ID";
}
