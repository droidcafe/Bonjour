package droid.nir.testapp1.noveu.Tasks;

import android.content.Context;

import java.util.Random;

import droid.nir.testapp1.noveu.Tasks.Loaders.LoadTaskHelper;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.dB.Project;
import droid.nir.testapp1.noveu.dB.Tasks;

/**
 * Created by droidcafe on 3/27/2016.
 */
public class TaskUtil {

    /**
     * util function to check if a task is saveable or not
     *
     * @param task the task name
     * @param mode the current mode
     * @return true if either task has a name or if there is atleast one among reminder , notes, subtask has
     * been set . else false
     */
    public static boolean isSaveableTask(String task, int mode) {
        return !(task.equals("") && mode == 0);
    }

    /**
     * helper function to find out if sub task is present from a mode of tasks.
     * do so by comparing mode to predetermined value in @class - constants.taskexpandmode
     *
     * @param mode current mode in expand view
     * @return 0 - not present 1-present
     */
    public static int isSubTask(int mode) {
        if (mode == constants.taskexpandmode[2] || mode == constants.taskexpandmode[4] ||
                mode == constants.taskexpandmode[6] || mode == constants.taskexpandmode[7])
            return 1;

        return 0;
    }

    /**
     * helper function to find out if notes is present from a mode of tasks.
     * do so by comparing mode to predetermined value in @class - constants.taskexpandmode
     *
     * @param mode current mode in expand view
     * @return 0 - not present 1-present
     */
    public static int isNotes(int mode) {
        if (mode == constants.taskexpandmode[3] || mode == constants.taskexpandmode[5] ||
                mode == constants.taskexpandmode[6] || mode == constants.taskexpandmode[7])
            return 1;

        return 0;
    }

    /**
     * helper function to find out if reminder is present from a mode of tasks.
     * do so by comparing mode to predetermined value in @class - constants.taskexpandmode
     *
     * @param mode current mode in expand view
     * @return 0 - not present 1-present
     */
    public static int isReminder(int mode) {
        if (mode == constants.taskexpandmode[1] || mode == constants.taskexpandmode[4] ||
                mode == constants.taskexpandmode[5] || mode == constants.taskexpandmode[7])
            return 1;

        return 0;
    }

    /**
     * helper function to find out if time is set for a reminder
     * do so by comparing remmode to predetermined value in @class - constants.permitmode
     *
     * @param remmode current remmode in expand view
     * @return 0 - not present 1-present
     */
    public static int isTime(int remmode) {
        if (remmode > constants.permitMode[1])
            return 1;
        return 0;
    }

    /**
     * helper function to find out if alarm is set for a reminder
     * do so by comparing remmode to predetermined value in @class - constants.permitmode
     *
     * @param remmode current remmode in expand view
     * @return 0 - not present 1-present
     */
    public static int isAlarm(int remmode) {
        if (remmode == constants.permitMode[3] || remmode == constants.permitMode[5])
            return 1;
        return 0;
    }

    /**
     * helper function to find out if repeat is set for a reminder
     * do so by comparing remmode to predetermined value in @class - constants.permitmode
     *
     * @param remmode current remmode in expand view
     * @return 0 - not present 1-present
     */
    public static int isRepeat(int remmode) {
        if (remmode == constants.permitMode[4] || remmode == constants.permitMode[5])
            return 1;
        return 0;
    }


    /**
     * helper function to check if notification update is to be made
     *
     * @param context
     * @param remData - the remider datas 0- isrem 1- istime, 2- timehr, 3- timemin, 4- isalarm, 5-isrepeat, 6-repeatmode
     * @param remdate - the new date of reminder
     * @param id      - the original task id
     * @return one of the notification mode specified in {@link constants} task_update_mode
     */
    public static int getNotificationUpdate(Context context, int[] remData, String remdate, int id) {
        String selection = "_id = ?";
        String selectionArgs[] = {Integer.toString(id)};
        int[][] isRem = LoadTaskHelper.loadTaskPartial(context, 0, new int[]{4}, selection, selectionArgs);

        if (isRem[0][0] == 0) {
            Log.d("tu","previously no reminder  now "+remData[0]);
            if (remData[0] == 0)
                return constants.task_update_modes[0];
            else
                return constants.task_update_modes[1];
        }

        int remData_old[] = LoadTaskHelper.loadReminder(context, id);

        String date = LoadTaskHelper.loadTaskPartial(context, 1, 2, "tid = " + id, null);
        Log.d("tu","old date is "+date);
        if (remData_old[1] == 0) { // time was not set
            if (remData[1] == 1) {
                Log.d("tu","time was not set before now set");
                return constants.task_update_modes[2];
            } else{
                Log.d("tu","time was not set before now also not set");
                return constants.task_update_modes[0];
            }
        }

        int[] timeData = LoadTaskHelper.loadTime(context, remData_old[0]);
        Log.d("tu","hr "+timeData[0] +" "+remData[2] +"min "+timeData[1] +" "+remData[3]+"date "+date +" rem date "+remdate);
        if (timeData[0] == remData[2] && timeData[1] == remData[3] && date.equals(remdate)) {
            Log.d("tu","time data all same -first pass");
            if (timeData[2] == remData[4]) { //if alarm is same
                Log.d("tu","alarm data all same -second pass pass");
                if (timeData[3] == 1) { //if  repeat mode is set
                    Log.d("tu","repeat set -third pass "+remData[5]);
                    if (remData[5] == 1) {
                        int repeatMode = LoadTaskHelper.loadRepeat(context, timeData[4]);
                        Log.d("tu","repeat modes "+repeatMode+" "+remData[6]);
                        if (repeatMode == remData[6])
                            return constants.task_update_modes[0];
                        else
                            return constants.task_update_modes[3];
                    } else
                        return constants.task_update_modes[3];
                } else if (remData[5] == 0)
                    return constants.task_update_modes[0];
                else
                    return constants.task_update_modes[3];
            }

            return constants.task_update_modes[3];

        } else {
            Log.d("tu","time data different ");
            return constants.task_update_modes[3];
        }

        //    return constants.task_update_modes[0];

    }

    /**
     * updates done value to new value
     *
     * @param context
     * @param tid       id of task
     * @param new_value new value of done
     */
    public static void setDone(Context context, int tid, int new_value) {
        int reqCol[] = {7}; /** done */
        int newVal[] = {new_value};

        String selection = "_id = ? ";
        String selectionArgs[] = {Integer.toString(tid)};
        Tasks.update(context, 0, reqCol, newVal, selection, selectionArgs);
    }

    public static void changeProject(Context context, int pid, int old_pid) {
        Log.d("tu", "new " + pid + " " + old_pid);
        String selection = "" + Tasks.columnNames[0][2] + " = ?";
        String[] selectionArgs = {Integer.toString(old_pid)};
        int[][] tids = LoadTaskHelper.loadTaskPartial(context, 0, new int[]{0}, selection, selectionArgs);

        if (tids == null)
            return;

        Project.updateProject(context, pid, tids.length);

        for (int i = 0; i < tids.length; i++) {
            selection = "_id = ? ";
            selectionArgs[0] = Integer.toString(tids[i][0]);
            Tasks.update(context, 0, new int[]{2}, new int[]{pid}, selection, selectionArgs);
        }
    }

    public static void setUndo(Context context, int tid, int undo_value) {
        int reqCol[] = {8}; /** hideflag */
        int newVal[] = {undo_value};

        String selection = "_id = ? ";
        String selectionArgs[] = {Integer.toString(tid)};
        Tasks.update(context, 0, reqCol, newVal, selection, selectionArgs);
    }

    /**
     * helper function for  getting random hint for {@link Add_minimal} and {@link Add_Expand}
     *
     * @param context
     * @return
     */
    public static int getRandomHint(Context context) {
        Random random = new Random();
        int hint = random.nextInt(16);
        Log.d("tu", "hint " + hint);
        return getHint(context, hint);
    }

    public static int getHint(Context context, int hint) {
        String resource_id = "task" + hint;
        return Import.getResource(context, resource_id, "string");
    }


}




