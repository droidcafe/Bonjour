package droid.nir.testapp1.noveu.sync.alarms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;

import java.util.Calendar;

import droid.nir.testapp1.noveu.Util.TimeUtil;
import droid.nir.testapp1.noveu.constants.IntentActions;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.sync.services.DailySyncService;

/**
 * Created by droidcafe on 6/28/2016.
 */
public class DailySyncAlarm {

    public static void setSyncAlarm(Context context) {
        Intent intent = new Intent(context, DailySyncService.class);
        intent.setAction(IntentActions.ACTION_DAILY_SYNC);
        PendingIntent pendingIntent = PendingIntent.getService(context,
                constants.daily_sync_pending_intent_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE,1);
        TimeUtil.setAlarm(context, pendingIntent, calendar);
        Log.d("dsa","Setting next day alarm");
        setAlarmPref(context);
    }

    public static void setSyncAlarmNow(Context context) {
        Intent intent = new Intent(context, DailySyncService.class);
        intent.setAction(IntentActions.ACTION_DAILY_SYNC);
        PendingIntent pendingIntent = PendingIntent.getService(context,
                constants.sync_now_pending_intent_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("dsa","setting alarm now ");

        TimeUtil.setAlarm(context, pendingIntent,Calendar.getInstance());
    }

    public static void setAlarmPref(Context context)
    {
        Import.setSharedPref(context,SharedKeys.dailyAlarmKey,TimeUtil.getTodayDate());
    }

    public static boolean isDailySyncSet(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname,0);
        String isalarmset = sharedPreferences.getString(SharedKeys.dailyAlarmKey, "notset");
        Log.d("dsa","checking sync "+isalarmset);
        if(isalarmset.equals(TimeUtil.getTodayDate())) {
            return true;
        }

        return false;
    }
}
