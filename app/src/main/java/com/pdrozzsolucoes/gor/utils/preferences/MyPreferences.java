package com.pdrozzsolucoes.gor.utils.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {

    private SharedPreferences sharedPreferences;
    private final String loginPreferences="LoginPrecerences";
    private final String mainPreferences="MainPreferences";
    private Activity activity;

    public static String emailUser="USERKEY";
    public static String passUser="PASSKEY";;

    public MyPreferences(Activity activity) {
        this.activity = activity;
        sharedPreferences=activity.getSharedPreferences(mainPreferences, Context.MODE_PRIVATE);
    }

    public void savePref(String key, String value){
        sharedPreferences.edit().putString(key,value).commit();
    }

    public String readPref(String key){
        return sharedPreferences.getString(key,null);
    }


}
