package droid.nir.testapp1.noveu.welcome;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import droid.nir.databaseHelper.MainDatabase;
import droid.nir.testapp1.noveu.Home.Home;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.dB.ParentDb;

/**
 * Created by droidcafe on 3/30/2016.
 */
public class Initial {

    public static void startInitialops(Context context, Activity activity, int version) {

        Import.setSharedPref(context, SharedKeys.Version, constants.VERSION);
        Import.setSharedPref(context, SharedKeys.Version_old, (version > 0)? version: constants.VERSION );
        Import.setSharedPref(context, SharedKeys.update, (version > 0)? 1: 0);

        new MainDatabase(context, activity).settingDatabase(false);

    }
}
