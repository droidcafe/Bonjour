package droid.nir.testapp1.noveu.notifications.util;

import android.app.Notification;
import android.content.Context;

import java.util.Calendar;

import droid.nir.testapp1.noveu.Tasks.Loaders.LoadTaskHelper;
import droid.nir.testapp1.noveu.Tasks.TaskUtil;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.Util.TimeUtil;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.dB.Tasks;

/**
 * Created by droidcafe on 7/9/2016.
 */
public class TaskNotificationHelper {

    public static void handleNotificationFiring(Context context , int tid)
    {
        int remiderdata[] = LoadTaskHelper.loadReminder(context, tid);

        if (remiderdata[1] == 1) {
            String selection_remider = "rid = ?";
            int alarmdata[] = LoadTaskHelper.loadTaskPartial(context,2,
                    new int[]{0,5},selection_remider,
                    new String[]{Integer.toString(remiderdata[0])});
            Log.d("ns", "repeat " + alarmdata[0] + " " + alarmdata[1]);
            if (alarmdata[1] == 1) { /** repeat is present */
                int repeatmode = LoadTaskHelper.loadRepeat(context,alarmdata[0]);
                TaskNotificationHelper.startRepeatProcess(context, tid, repeatmode);
                TaskUtil.setDone(context, tid,0);
            }
            else
                TaskUtil.setDone(context, tid,1);
        } else{
            TaskUtil.setDone(context, tid, 1);
        }
    }
    public static void startRepeatProcess(Context context , int tid, int repeatMode){
        Calendar calendar = TimeUtil.getNewRepeatDate(repeatMode);
        String newDate = TimeUtil.getDate(calendar);
        int reqColInt[] = {7};
        int reqColString[] = {3};
        int newVal[] = {0};
        String[] newString = {newDate};

        String selection = "_id = ? ";
        String selectionArgs[] = { Integer.toString(tid)};
        Tasks.update(context,0,reqColInt,newVal,reqColString, newString,
                    selection,selectionArgs);
    }



    public static int getTaskNotificationDefault (Context context, boolean isSound){
        int taskDefault = 0;

        boolean isLight = (boolean) Import.getSettingSharedPref(context,
                SharedKeys.task_notification_light, 3);
        boolean isVibrate = (boolean) Import.getSettingSharedPref(context,
                SharedKeys.task_notification_vibrate, 3);

        if (isLight) {
            taskDefault = taskDefault | Notification.DEFAULT_LIGHTS;
        }
        if(isVibrate)
            taskDefault = taskDefault | Notification.DEFAULT_VIBRATE;
        if(isSound)
            taskDefault = taskDefault | Notification.DEFAULT_SOUND;

        return taskDefault;

    }


}
