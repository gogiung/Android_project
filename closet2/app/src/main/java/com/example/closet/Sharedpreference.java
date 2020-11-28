package com.example.closet;

import android.content.Context;
import android.content.SharedPreferences;


public class Sharedpreference {

    final private static String PREFNAME_WEATHER = "weather";

    //외장 SD 사용 여부
    final private static String PREFKEY_WEATHER = "weathering";



    public static void setSharedPrefWeather(Context context, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(PREFNAME_WEATHER, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putBoolean(PREFKEY_WEATHER, value);
        prefEditor.commit();
    }

    public static boolean getSharedPrefWeather(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFNAME_WEATHER, Context.MODE_PRIVATE);
        return pref.getBoolean(PREFKEY_WEATHER, false);
    }


}
