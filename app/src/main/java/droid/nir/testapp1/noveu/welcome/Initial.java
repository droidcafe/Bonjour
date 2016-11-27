package droid.nir.testapp1.noveu.welcome;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import droid.nir.databaseHelper.MainDatabase;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.dB.ParentDb;
import droid.nir.testapp1.noveu.dB.initial.TaskMigrations;
import droid.nir.testapp1.noveu.dB.initial.inital_projects;
import droid.nir.testapp1.noveu.dB.initial.initial_tasks;

/**
 * Created by droidcafe on 3/30/2016.
 */
public class Initial {

    public static void startInitialops(Context context, int version) {

        Import.setSharedPref(context, SharedKeys.Version, constants.VERSION);
        Import.setSharedPref(context, SharedKeys.Version_old, (version > 0) ? version : constants.VERSION);
        Import.setSharedPref(context, SharedKeys.update, (version > 0) ? 1 : 0);
        Import.setSharedPref(context, SharedKeys.project_random_index, 0);
        Import.setSharedPref(context, SharedKeys.db_ops_status, -1);


        final SharedPreferences defaultValueSp = context.getSharedPreferences(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, Context.MODE_PRIVATE);

        if (!defaultValueSp.getBoolean(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, false)) {
            PreferenceManager.setDefaultValues(context, R.xml.pref_tasks, false);
            PreferenceManager.setDefaultValues(context, R.xml.pref_general, true);
            PreferenceManager.setDefaultValues(context, R.xml.pref_notification, true);
        }

    }

    public static void startDBops(Context context) {

        Log.d("initial","starting db ops");
        int bonjour_status = Import.getSharedPref(context, SharedKeys.db_bonjour);
        int mydb_status = Import.getSharedPref(context, SharedKeys.db_mydatabase);
        if (bonjour_status == constants.SUCCESS_CODE
                && mydb_status == constants.SUCCESS_CODE) {
            Log.d("initial","all db operations done");
            new inital_projects().execute(context);
            new initial_tasks().execute(context);
            TaskMigrations.migrate(context);

            Import.setSharedPref(context,SharedKeys.db_ops_status,constants.db_ops_status_modes[1]);
            return;
        }
        int db_ops_status_modes = Import.getSharedPref(context,SharedKeys.db_ops_status);
        Log.d("initial","not done "+db_ops_status_modes);
        if(bonjour_status != constants.SUCCESS_CODE &&
                ( db_ops_status_modes != constants.db_ops_status_modes[2] ||
                        db_ops_status_modes != constants.db_ops_status_modes[4] )){

            Log.d("initial","bonjour not yet ready "+bonjour_status);
            Import.updateSharedPref(context,SharedKeys.db_ops_status,constants.db_ops_status_modes[2]);
        }
        if(mydb_status != constants.SUCCESS_CODE &&
                ( db_ops_status_modes != constants.db_ops_status_modes[3] ||
                        db_ops_status_modes != constants.db_ops_status_modes[4] )){
            Log.d("initial","mydatabase not set "+mydb_status);
            Import.updateSharedPref(context,SharedKeys.db_ops_status,constants.db_ops_status_modes[3]);
        }
    }

    public static class AsyncInitial extends AsyncTask<Void, Void, Void> {

        Context context;
        Activity activity;
        int version;

        public AsyncInitial(Context context, Activity activity, int version) {
            this.context = context;
            this.activity = activity;
            this.version = version;
        }


        @Override
        protected Void doInBackground(Void... params) {

            startInitialops(context, version);
            ParentDb.getInstance(context).returnSQl();
            new MainDatabase(context, activity).settingDatabase(false);


            startDBops(context);

            return null;
        }
    }
}
