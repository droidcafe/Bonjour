package droid.nir.testapp1.noveu.welcome;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import droid.nir.databaseHelper.MainDatabase;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Home.Home;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.dB.ParentDb;
import droid.nir.testapp1.noveu.dB.initial.TaskMigrations;
import droid.nir.testapp1.noveu.dB.initial.inital_projects;

/**
 * Created by droidcafe on 3/30/2016.
 */
public class Initial {

    public static void startInitialops(Context context, Activity activity, int version) {

        Import.setSharedPref(context, SharedKeys.Version, constants.VERSION);
        Import.setSharedPref(context, SharedKeys.Version_old, (version > 0) ? version : constants.VERSION);
        Import.setSharedPref(context, SharedKeys.update, (version > 0) ? 1 : 0);
        Import.setSharedPref(context, SharedKeys.project_random_index, 0);

        new MainDatabase(context, activity).settingDatabase(false);
        final SharedPreferences defaultValueSp = context.getSharedPreferences(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, Context.MODE_PRIVATE);

        if (!defaultValueSp.getBoolean(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, false)) {
            PreferenceManager.setDefaultValues(context, R.xml.pref_tasks, false);
            PreferenceManager.setDefaultValues(context, R.xml.pref_general, true);
            PreferenceManager.setDefaultValues(context, R.xml.pref_notification, true);
        }

    }

    public static void startDBops(Context context) {
        new inital_projects().execute(context);
        TaskMigrations.migrate();
    }
}
