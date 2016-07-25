package droid.nir.testapp1.noveu.sync.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.DateUtils;
import droid.nir.testapp1.noveu.Util.Log;

import java.util.Calendar;

import droid.nir.testapp1.noveu.Tasks.Loaders.LoadTaskHelper;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.TimeUtil;
import droid.nir.testapp1.noveu.constants.IntentActions;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.notifications.TaskSilentNotification;
import droid.nir.testapp1.noveu.notifications.handlers.NotificationHandler;
import droid.nir.testapp1.noveu.today.TodayNotificationHelper;

public class TaskChangeService extends IntentService {

    static Context context;

    public TaskChangeService() {
        super("DbChangeService");
    }

    public static void startTaskChange(Context context, Intent broadcastIntent) {
        Intent intent = new Intent(context, TaskChangeService.class);
        intent.setAction(broadcastIntent.getAction());
        intent.putExtra("tid", broadcastIntent.getIntExtra("tid", -1));
        intent.putExtra("isrem", broadcastIntent.getIntExtra("isrem", -1));
        intent.putExtra("date", broadcastIntent.getStringExtra("date"));
        intent.putExtra("done", broadcastIntent.getIntExtra("done", -1));
        context.startService(intent);
    }

    public static void startTaskChange(Context context,int tid)
    {
        Intent intent = new Intent(context, TaskChangeService.class);
        intent.setAction(IntentActions.ACTION_TASK_DAILY_SYNC);
        intent.putExtra("tid", tid);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context = getApplicationContext();
        if (intent != null) {
            final String action = intent.getAction();
            int passInt[] = getIntArguments(intent);
            String date = intent.getStringExtra("date");

            switch (action) {
                case IntentActions.ACTION_TASK_INSERT:
                    Log.d("tcs", "inserted " + passInt[0]);
                   if(TodayNotificationHelper.isGoodTask(passInt,date))
                        handleTaskInsert(passInt[0]);
                    break;
                case IntentActions.ACTION_TASK_UPDATE:
                    handleTaskUpdate(passInt, date);
                    break;
                case  IntentActions.ACTION_TASK_DAILY_SYNC:
                    handleTaskInsert(passInt[0]);

            }
        }
    }

    private void handleTaskUpdate(int[] passInt, String date) {

        String selection = "oid = " + passInt[0];
        Cursor cursor = TodayNotificationHelper.loadNotificationData(context,
                                                                     selection, null,
                                                                     new int[]{0, 3, 4, 5});
        int notificationData[] = new int[cursor.getColumnCount()];
        if (cursor.getCount() > 0) {
            while(cursor.moveToNext())
            {
                notificationData = TodayNotificationHelper.decodeNotificationData(cursor,
                        new int[]{0, 3, 4, 5});
                NotificationHandler.cancel(context, notificationData[0]);
                NotificationHandler.cancelAlarm(context,
                        new int[]{notificationData[0],notificationData[1],notificationData[2]});
            }

            if (TodayNotificationHelper.isGoodTask(passInt,date)) {

                int reminderData[] = LoadTaskHelper.loadReminder(context, passInt[0]);
                if (reminderData[1] == 1) //if time is set or not
                {
                    int timeData[] = LoadTaskHelper.loadTime(context, reminderData[0]);
                    TodayNotificationHelper.updateTodayNotification(context,
                            notificationData[0],
                            new int[]{1,passInt[0],timeData[0],timeData[1],timeData[2]});

                    setTaskAlarm(new int[]{notificationData[0], timeData[0], timeData[1]});
                }
                else {
                    int timehr = TimeUtil.getNowTime(Calendar.HOUR_OF_DAY);
                    int timemin = TimeUtil.getNowTime(Calendar.MINUTE);
                    TodayNotificationHelper.updateTodayNotification(context,
                            notificationData[0],
                            new int[]{1, passInt[0], timehr, timemin, 2});

                    showNotificationNow(notificationData[0],timehr,timemin);

                }
            }
        } else {
            if (TodayNotificationHelper.isGoodTask(passInt,date))
                handleTaskInsert(passInt[0]);
        }
    }

    /**
     * helper function to handle the setting up of  notification on inserting a new task today
     * @param tid id of task inserted
     */

    public static void handleTaskInsert(int tid) {

        int reminderData[] = LoadTaskHelper.loadReminder(context, tid);

        if (reminderData[1] == 1) //if time is set or not
        {
            int timeData[] = LoadTaskHelper.loadTime(context, reminderData[0]);
            int id = TodayNotificationHelper.insertTodayNotification(context, tid, timeData,
                    constants.notificationMode[0]);
            setTaskAlarm(new int[]{id, timeData[0], timeData[1]});
        } else {
            int timehr = TimeUtil.getNowTime(Calendar.HOUR_OF_DAY);
            int timemin = TimeUtil.getNowTime(Calendar.MINUTE);
            int id = TodayNotificationHelper.insertTodayNotification(context, tid,
                    new int[]{timehr,timemin, 2}, constants.notificationMode[0]); /** 2 here sets mode for notification
                                                    0- silent 1- with alarm 2- permanent **/

            showNotificationNow(id,timehr,timemin);
        }
    }

    /**
     * function for showing permanent notification right now for tasks with no time set
     * @param id id of notification row in today_notification
     * @param timehr now hr
     * @param timemin now minute
     */
    public static void showNotificationNow(int id,int timehr,int timemin)
    {
        NotifyService.show(context, new int[]{id, timehr, timemin});
    }

    /**
     * get the service intent integer arguments
     * @param intent
     * @return an array of pass int 0-id of task ,1- reminder present or not  2-whether done or not
     */
    public int[] getIntArguments(Intent intent) {

        int id = intent.getIntExtra("tid", -1);
        int done = intent.getIntExtra("done", -1);
        int isrem = intent.getIntExtra("isrem", -1);
        return new int[]{id, isrem, done};

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
