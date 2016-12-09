package droid.nir.testapp1.noveu.Util;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by droidcafe on 11/27/2016.
 */

public class FirebaseUtil {


    public static final String task_insert = "TASK_INSERT";
    public static final String task_insert_minimal = "TASK_INSERT_MINIMAL";
    public static final String task_update = "TASK_UPDATE";
    public static final String decision_insert = "DECISION_INSERT";
    public static final String decision_update = "DECISION_UPDATE";
    public static final String event_insert = "EVENT_INSERT";
    public static final String event_update = "EVENT_UPDATE";
    public static final String task_notification = "TASK_NOTIFICATION";
    public static final String decision_notification = "DECISION_NOTIFICATION";
    public static final String event_notification = "EVENT_NOTIFICATION";
    public static final String dailySync = "DAILYSYNC";
    public static final String project_insert = "PROJECT_INSERT";
    public static final String project_update = "PROJECT_UPDATE";
    public static final String project_delete = "PROJECT_DELETE";
    public static final String task_delete = "TASK_DELETE";
    public static final String event_delete = "EVENT_DELETE";
    public static final String decision_delete = "DECISION_DELETE";
    public static final String sign_out = "SIGN_OUT";
    public static final String menu_help = "MENU_HELP";
    public static final String menu_feedback = "FEEDBACK";
    public static final String menu_rate = "RATE";
    public static final String help_view = "HELP_VIEW";
    public static final String help_pending = "HELP_PENDING";
    public static final String help_event = "HELP_EVENT";
    public static final String help_task = "HELP_TASK";
    public static final String help_label = "HELP_LABEL";
    public static final String compose_mail = "COMPOSE_MAIL";
//    public static final String ="";
//    public static final String ="";
//    public static final String ="";


    public static final String path_help="help";

    public static void logEvent(FirebaseAnalytics mFirebaseAnalytics,
                                String event, String[] keys, String[] type,
                                String[] passString, int[] passInt, double[] passDouble) {
        Bundle firebaseBundle = new Bundle();
        int i = 0, iS = 0, iI = 0, iD = 0;
        for (String s : type) {
            switch (s) {
                case "string":
                    firebaseBundle.putString(keys[i++], passString[iS++]);
                    break;
                case "integer":
                    firebaseBundle.putInt(keys[i++], passInt[iI++]);
                    break;
                case "double":
                    firebaseBundle.putDouble(keys[i++], passDouble[iD++]);
                    break;

            }
        }
        Log.d("fu", "log event " + event);
        mFirebaseAnalytics.logEvent(event, firebaseBundle);
    }

    public static void recordScreenView(Activity activity, String screenName, FirebaseAnalytics mFirebaseAnalytics) {
        mFirebaseAnalytics.setCurrentScreen(activity, screenName, null /* class override */);
    }
}


