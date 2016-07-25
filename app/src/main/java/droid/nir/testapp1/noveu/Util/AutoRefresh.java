package droid.nir.testapp1.noveu.Util;

import android.content.Context;
import android.content.SharedPreferences;


import droid.nir.testapp1.noveu.constants.SharedKeys;

/**
 * Created by droidcafe on 3/19/2016.
 */
public class AutoRefresh {

    public static void setRefreshSharedPref(Context context)
    {
        Log.d("ar","setting refresh");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname,0);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putInt("ischanged",1);
        editor.apply();
    }

    public static boolean isRefreshNeeded(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname,0);
        int ischanged = sharedPreferences.getInt("ischanged", 0);
        Log.d("ar","refresh "+ischanged);
        if (ischanged == 1) {
           return  true;
        }

        return false;
    }

    public static void setRefreshDone(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ischanged",0);
        editor.apply();

    }
}
