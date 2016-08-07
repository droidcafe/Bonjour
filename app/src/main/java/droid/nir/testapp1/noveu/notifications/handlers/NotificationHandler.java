package droid.nir.testapp1.noveu.notifications.handlers;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;

import droid.nir.testapp1.noveu.Util.TimeUtil;
import droid.nir.testapp1.noveu.constants.IntentActions;
import droid.nir.testapp1.noveu.sync.services.NotifyService;
import droid.nir.testapp1.noveu.sync.services.PlayBackService;
import droid.nir.testapp1.noveu.today.TodayNotificationHelper;

/**
 * Created by droidcafe on 3/26/2016.
 * <p/>
 * Class to handle various boiler plates related to setting a notification
 */
public class NotificationHandler {


    private static final String NOTIFICATION_TAG = "TaskSilent";

    /**
     * set pending alarm for particular notification using id from today_notification
     *
     * @param context
     * @param passInt 0 - id of the notification row in today_notification 2- hr to fire notification 3 - min to fire notification
     */
    public static void setAlarm(Context context, int[] passInt) {
        Intent intent = new Intent(context, NotifyService.class);
        intent.setAction(IntentActions.ACTION_NOTIFY_TASKS);
        intent.putExtra("rid", passInt[0]);
        intent.putExtra("timehr", passInt[1]);
        intent.putExtra("timemin", passInt[2]);

        PendingIntent pendingIntent = PendingIntent.getService(context,
                passInt[0], intent, PendingIntent.FLAG_UPDATE_CURRENT);
        TimeUtil.setAlarm(context, pendingIntent, TimeUtil.setCalendar(passInt[1], passInt[2]));
    }

    /**
     * function to cancel an alarm set for notification firing
     *
     * @param context
     * @param passInt 0-rid 1-timehr 2- timemin
     */
    public static void cancelAlarm(Context context, int[] passInt) {
        Intent intent = new Intent(context, NotifyService.class);
        intent.setAction(IntentActions.ACTION_NOTIFY_TASKS);
        intent.putExtra("rid", passInt[0]);
        intent.putExtra("timehr", passInt[1]);
        intent.putExtra("timemin", passInt[2]);

        PendingIntent pendingIntent = PendingIntent.getService(context, passInt[0],
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        TimeUtil.cancelAlarm(context, pendingIntent);
    }

    /**
     * cancels all pending alarms set in today_notification table
     * it checks isfired is 0 or not
     * @param context
     */
    public static void cancelAllPendingAlarm(Context context)
    {
        String selection = "isfired = ?";
        String[] selectionArgs = {Integer.toString(0)};
        int[] columnreq = {0,3,4};
        Cursor today_cursor = TodayNotificationHelper.loadNotificationData(context,selection, selectionArgs,columnreq);
        while (today_cursor.moveToNext()){
            int[] passInt = TodayNotificationHelper.decodeNotificationData(today_cursor,new int[]{0,3,4});
            cancelAlarm(context,passInt);
        }
    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void notify(final Context context, final Notification notification, int id) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, id, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }


    /**
     * cancels a active notification from the drawer using the notification id
     * @param context
     * @param id the id of notification (the row id from today table {@link droid.nir.testapp1.noveu.dB.Today})
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context, int id) {
      PlayBackService.stopPlayBack(context);
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, id);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }



}
