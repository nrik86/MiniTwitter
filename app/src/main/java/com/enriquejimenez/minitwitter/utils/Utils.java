package com.enriquejimenez.minitwitter.utils;

public class Utils {

    public static boolean isNullOrEmpty(String string){
        if(string != null){
            if (!string.isEmpty()){
                return true;
            }
        }else {
            return false;
        }
        return false;
    }

}
