package droid.nir.testapp1.noveu.sync.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import java.util.Calendar;

import droid.nir.testapp1.noveu.Tasks.Loaders.LoadTaskHelper;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.Util.TimeUtil;
import droid.nir.testapp1.noveu.constants.IntentActions;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.notifications.handlers.NotificationHandler;
import droid.nir.testapp1.noveu.notifications.util.TaskNotificationHelper;
import droid.nir.testapp1.noveu.today.TodayNotificationHelper;

public class TaskChangeService extends IntentService {

    static Context context;

    public TaskChangeService() {
        super("DbChangeService");
    }

    public static void startTaskChange(Context context, Intent broadcastIntent) {
        Intent intent = new Intent(context, TaskChangeService.class);
        intent.setAction(broadcastIntent.getAction());
        intent.putExtra("broadcast_bundle", broadcastIntent.getBundleExtra("broadcast_bundle"));

        context.startService(intent);
    }

    public static void startTaskChange(Context context, int tid) {
        Intent intent = new Intent(context, TaskChangeService.class);
        intent.setAction(IntentActions.ACTION_TASK_DAILY_SYNC);
        Bundle bundle = new Bundle();
        bundle.putInt("tid", tid);

        intent.putExtra("broadcast_bundle", bundle);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context = getApplicationContext();
        if (intent != null) {
            final String action = intent.getAction();
            Bundle bundle = intent.getBundleExtra("broadcast_bundle");
            int passInt[] = getIntArguments(bundle);
            int passIntExtra[] = (passInt[3] > 0)
                    ? Import.getIntExtraArguments(bundle, passInt[3])
                    : null;

            String date = bundle.getString("date");

            switch (action) {
                case IntentActions.ACTION_TASK_INSERT:
                    Log.d("tcs", "inserted " + passInt[0]);
                    if (TodayNotificationHelper.isGoodTask(passInt, date))
                        handleTaskInsert(passInt[0]);
                    break;
                case IntentActions.ACTION_TASK_UPDATE:
                    handleTaskUpdate(passInt, date, passIntExtra[0]);
                    break;
                case IntentActions.ACTION_TASK_DAILY_SYNC:
                    handleTaskInsert(passInt[0]);

            }
        }
    }

    /**
     * {@link #getIntArguments}
     *
     * @param passInt
     * @param date
     * @param notificationUpdateMode {@link constants#task_update_modes}
     */
    private void handleTaskUpdate(int[] passInt, String date, int notificationUpdateMode) {


        Log.d("tcs", "update mode " + notificationUpdateMode);
        String selection = "oid = " + passInt[0];
        int[] reqColumns = new int[]{0, 3, 4, 5, 6}; /** 0 id , 1 timehr, 2 timemin,3 isalarm,4  isfired */
        Cursor cursor = TodayNotificationHelper.loadNotificationData(context,
                selection, null, reqColumns
        );
        int notificationData[] = new int[cursor.getColumnCount()];
        if (notificationUpdateMode == constants.task_update_modes[0]) {
            /**
             * have to find a way to update notification if it is present
             */
            if (TodayNotificationHelper.isGoodTask(passInt, date)) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        notificationData = TodayNotificationHelper.decodeNotificationData(cursor,
                                reqColumns);
                    }
                    if (notificationData[4] == 1) {
                        TaskNotificationHelper.handleNotificationFiring(context, passInt[0]);
                    }
                }
            }

            return;
        }
        if (cursor.getCount() > 0) {
            Log.d("tcs","today notification present ");
            while (cursor.moveToNext()) {

                notificationData = TodayNotificationHelper.decodeNotificationData(cursor,
                        reqColumns);
                NotificationHandler.cancel(context, notificationData[0]);
                NotificationHandler.cancelAlarm(context,
                        new int[]{notificationData[0], notificationData[1], notificationData[2]});
                Log.d("tcs","today notification was present "+notificationData[0]);
            }

            if (TodayNotificationHelper.isGoodTask(passInt, date, notificationUpdateMode)) {
                Log.d("tcs","good task ");
                int reminderData[] = LoadTaskHelper.loadReminder(context, passInt[0]);
                if (reminderData[1] == 1) //if time is set or not
                {
                    Log.d("tcs","reminder present");
                    int timeData[] = LoadTaskHelper.loadTime(context, reminderData[0]);
                    TodayNotificationHelper.updateTodayNotification(context,
                            notificationData[0],
                            new int[]{1, passInt[0], timeData[0], timeData[1], timeData[2], 0});
                    if (TodayNotificationHelper.isValidTime(timeData[0], timeData[1]))
                        setTaskAlarm(new int[]{notificationData[0], timeData[0], timeData[1]});
                    else {
                        /**
                         * if time has passed do things like set fired =1 and check if repeat process is to begin
                         */
                        TodayNotificationHelper.updateIsFired(context, 1, notificationData[0]);
                        TaskNotificationHelper.handleNotificationFiring(context, passInt[0]);

                    }
                } else {
                    Log.d("tcs","no time set");
                    int timehr = TimeUtil.getNowTime(Calendar.HOUR_OF_DAY);
                    int timemin = TimeUtil.getNowTime(Calendar.MINUTE);
                    TodayNotificationHelper.updateTodayNotification(context,
                            notificationData[0],
                            new int[]{1, passInt[0], timehr, timemin, 2,0});

                    showNotificationNow(notificationData[0], timehr, timemin);

                }
            } else {
                Log.d("tcs","deleting today notification");
                TodayNotificationHelper.deleteTodayNotification(context, notificationData[0]);
            }
        } else {
            if (TodayNotificationHelper.isGoodTask(passInt, date))
                handleTaskInsert(passInt[0]);
        }
    }

    /**
     * helper function to handle the setting up of  notification on inserting a new task today
     *
     * @param tid id of task inserted
     */

    public static void handleTaskInsert(int tid) {

        int reminderData[] = LoadTaskHelper.loadReminder(context, tid);

        if (reminderData[1] == 1) //if time is set or not
        {
            int timeData[] = LoadTaskHelper.loadTime(context, reminderData[0]);
            int id = TodayNotificationHelper.insertTodayNotification(context, tid, timeData,
                    constants.notificationMode[0]);
            if (TodayNotificationHelper.isValidTime(timeData[0], timeData[1])) {
                setTaskAlarm(new int[]{id, timeData[0], timeData[1]});
            } else {
                /**
                 * if time has passed do things like set fired =1 and check if repeat process is to begin
                 */
                TodayNotificationHelper.updateIsFired(context, 1, id);
                TaskNotificationHelper.handleNotificationFiring(context, tid);

            }
        } else {
            int timehr = TimeUtil.getNowTime(Calendar.HOUR_OF_DAY);
            int timemin = TimeUtil.getNowTime(Calendar.MINUTE);
            int id = TodayNotificationHelper.insertTodayNotification(context, tid,
                    new int[]{timehr, timemin, 2}, constants.notificationMode[0]); /** 2 here sets mode for notification
             0- silent 1- with alarm 2- permanent **/

            showNotificationNow(id, timehr, timemin);
        }
    }

    /**
     * function for showing permanent notification right now for tasks with no time set
     *
     * @param id      id of notification row in today_notification
     * @param timehr  now hr
     * @param timemin now minute
     */
    public static void showNotificationNow(int id, int timehr, int timemin) {
        NotifyService.show(context, new int[]{id, timehr, timemin});
    }

    /**
     * get the service intent integer arguments
     *
     * @param bundle - the bundle passed along with the intent
     * @return an array of pass int 0-id of task ,1- reminder present or not  2-whether done or not
     * 3 - the no of extras present (extras include notification_update_mode in case of TASK_UPDATE)
     */
    public int[] getIntArguments(Bundle bundle) {

        int id = bundle.getInt("tid", -1);
        int done = bundle.getInt("done", -1);
        int isrem = bundle.getInt("isrem", -1);
        int extras = bundle.getInt("extras");
        Log.d("tcs", " " + id + " " + done + " " + isrem + " " + extras);
        return new int[]{id, isrem, done, extras};


    }


    /**
     * set task pending alarm for creting the notification
     *
     * @param passInt 0- id of the notification in today_notification table
     *                1- timehr of notification ,2-timemin of notification
     */
    public static void setTaskAlarm(int passInt[]) {
        NotificationHandler.setAlarm(context, passInt);
    }


}
