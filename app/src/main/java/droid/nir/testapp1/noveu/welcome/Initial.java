package droid.nir.testapp1.noveu.welcome;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import droid.nir.databaseHelper.MainDatabase;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.dB.ParentDb;

/**
 * Created by droidcafe on 3/30/2016.
 */
public class Initial {

    public static void startInitialops(Context context) {


        SharedPreferences sharedPreferences =context.getSharedPreferences(SharedKeys.prefname, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //  editor.putInt("dbVersion")
    }
}
