package droid.nir.testapp1.noveu.sync.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.util.List;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Tasks.Loaders.LoadTaskHelper;
import droid.nir.testapp1.noveu.Tasks.data.TaskVitalData;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.alarm.AlarmUtils;
import droid.nir.testapp1.noveu.constants.IntentActions;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.dB.Project;
import droid.nir.testapp1.noveu.notifications.TaskSilentNotification;
import droid.nir.testapp1.noveu.notifications.util.NotificationUtils;
import droid.nir.testapp1.noveu.notifications.util.TaskNotificationHelper;
import droid.nir.testapp1.noveu.today.TodayNotificationHelper;

/**
 * firts goes and checks out the today_notification for id and other details of notification
 * Then loads the task details using id from today_notification
 */

public class NotifyService extends IntentService {

    static Context context;
    static int id;

    public NotifyService() {
        super("NotifyService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action.equals(IntentActions.ACTION_NOTIFY_TASKS)) {
//                getApplicationContext();
                show(getApplicationContext(), getArguments(intent));

            }
        }
    }

    /**
     * load the entire data from today_notification table and handle the showing notification
     *
     * @param passData the data pass through intent from TaskChangeService 0- id 1-hr 2-min
     */
    public static void show(Context context, int passData[]) {
        NotifyService.context = context;
        id = passData[0];
        Log.d("ns", "notify " + id);
        if (id > 0) {

            String selection = "_id  = ?";
            String selectionArgs[] = {String.valueOf(id)};
            int[] reqcolumn = {2, 3, 4, 5, 6, 1};
            Cursor cursor = TodayNotificationHelper.loadNotificationData(context, selection, selectionArgs, reqcolumn);
            /**
             * 0- oid 1- timehr 2- timemin 3 = isalarm 4=isfired 5=mode
             */
            int notificationData[] = new int[reqcolumn.length];
            while (cursor.moveToNext()) {
                notificationData = TodayNotificationHelper.decodeNotificationData(cursor, reqcolumn);
                if (notificationData[1] == passData[1] && notificationData[2] == passData[2] &&
                        notificationData[4] == 0) {
                    if (notificationData[5] == constants.notificationMode[0])
                        if (showTaskNotification(notificationData)) /** here actually showing occurs - true if notification firing was successfull else false **/ {
                            if (notificationData[3] == 1)
                                PlayBackService.startPlayBack(context, AlarmUtils.getAlarmUri(), false);

                            /**
                             * here notification has been displayed . do things like change isfire = 1 and done =1
                             */
                            TodayNotificationHelper.updateIsFired(context, 1, id);
                            if (notificationData[5] == constants.notificationMode[0])
                                TaskNotificationHelper.handleNotificationFiring(context, notificationData[0]);
                        }
                }
            }

        }

    }

    /**
     * load the task data for particular notification
     *
     * @param notificationData - the data about the task stored in today_notification
     *                         0- oid 1- timehr 2- timemin 3 = isalarm 4- isfired
     * @return true - if notification successfully fired
     * else false
     */
    private static boolean showTaskNotification(int[] notificationData) {
        TaskVitalData taskData = LoadTaskHelper.loadTasksVitals(context, notificationData[0]);

        if (taskData == null)
            return false;
        String project = new Project(context).getProjectName(context, taskData.pid);
        Log.d("ns", "notify " + project + " " + taskData.done);

        NotificationUtils.NotificationMode notificationMode;
        if (notificationData[3] == 0)
            notificationMode = NotificationUtils.NotificationMode.silent;
        else if (notificationData[3] == 1)
            notificationMode = NotificationUtils.NotificationMode.alarm;
        else
            notificationMode = NotificationUtils.NotificationMode.permanent;

        boolean isNotes = (boolean) Import.getSettingSharedPref(context,
                SharedKeys.pref_task_notification_note, 3);
        boolean isSubtask = (boolean) Import.getSettingSharedPref(context,
                SharedKeys.pref_task_notification_subtask, 3);


        List<String> listsubtask = null;
        String notes = null;
        if (taskData.issubtask == 1 && isSubtask) {
            listsubtask = LoadTaskHelper.loadSubTasks(context, notificationData[0]);
            Log.d("ns", "notify " + 1);

        }
        if (taskData.isnotes == 1 && isNotes) {
            notes = LoadTaskHelper.loadNotes(context, notificationData[0]);
            Log.d("ns", "notify " + 2);

        }
        if (listsubtask != null) {

            TaskSilentNotification.notify(context,
                    taskData.name, project, listsubtask, id, notificationData[0], notificationMode,
                    notes, constants.notificationMode[0]);
        } else if (notes != null) {

            TaskSilentNotification.notify(context,
                    taskData.name, project, notes, id, notificationData[0], notificationMode,
                    constants.notificationMode[0]);
        } else {
            Log.d("ns", "notify " + 3);
            String summary = context.getResources().getString(R.string.task_silent_notification_text_default,
                    Import.formatTime(notificationData[1], notificationData[2]));
            TaskSilentNotification.notify(context,
                    taskData.name, project, summary, id, notificationData[0], notificationMode,
                    constants.notificationMode[0]);
        }


        return true;


    }


    public int[] getArguments(Intent intent) {
        int id = intent.getIntExtra("rid", -1);
        int hr = intent.getIntExtra("timehr", -1);
        int min = intent.getIntExtra("timemin", -1);

        return new int[]{id, hr, min};

    }


}
