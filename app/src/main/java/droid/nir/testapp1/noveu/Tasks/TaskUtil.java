package droid.nir.testapp1.noveu.Tasks;

import android.content.Context;

import droid.nir.testapp1.noveu.Tasks.Loaders.LoadTaskHelper;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.dB.Tasks;

/**
 * Created by droidcafe on 3/27/2016.
 */
public class TaskUtil {

    /**
     * util function to check if a task is saveable or not
     * @param task the task name
     * @param mode the current mode
     * @return true if either task has a name or if there is atleast one among reminder , notes, subtask has
     * been set . else false
     */
    public static boolean isSaveableTask(String task , int mode)
    {
        return !(task.equals("")&&mode==0);
    }

    /**
     * helper function to find out if sub task is present from a mode of tasks.
     * do so by comparing mode to predetermined value in @class - constants.taskexpandmode
     * @param mode current mode in expand view
     * @return 0 - not present 1-present
     */
    public static int isSubTask(int mode) {
        if (mode == constants.taskexpandmode[2] ||mode == constants.taskexpandmode[4] ||
                mode == constants.taskexpandmode[6] || mode == constants.taskexpandmode[7])
            return 1;

        return 0;
    }

    /**
     * helper function to find out if notes is present from a mode of tasks.
     * do so by comparing mode to predetermined value in @class - constants.taskexpandmode
     * @param mode current mode in expand view
     * @return 0 - not present 1-present
     */
    public static int isNotes(int mode) {
        if (mode == constants.taskexpandmode[3] ||mode == constants.taskexpandmode[5] ||
                mode == constants.taskexpandmode[6] || mode == constants.taskexpandmode[7])
            return 1;

        return 0;
    }
    /**
     * helper function to find out if reminder is present from a mode of tasks.
     * do so by comparing mode to predetermined value in @class - constants.taskexpandmode
     * @param mode current mode in expand view
     * @return 0 - not present 1-present
     */
    public static int isReminder(int mode) {
        if (mode == constants.taskexpandmode[1] ||mode == constants.taskexpandmode[4] ||
                mode == constants.taskexpandmode[5] || mode == constants.taskexpandmode[7])
            return 1;

        return 0;
    }
    /**
     * helper function to find out if time is set for a reminder
     * do so by comparing remmode to predetermined value in @class - constants.permitmode
     * @param remmode current remmode in expand view
     * @return 0 - not present 1-present
     */
    public static int isTime(int remmode){
        if (remmode > constants.permitMode[1])
            return 1;
        return 0;
    }
    /**
     * helper function to find out if alarm is set for a reminder
     * do so by comparing remmode to predetermined value in @class - constants.permitmode
     * @param remmode current remmode in expand view
     * @return 0 - not present 1-present
     */
    public static int isAlarm(int remmode){
        if (remmode == constants.permitMode[3] || remmode == constants.permitMode[5] )
            return 1;
        return 0;
    }
    /**
     * helper function to find out if repeat is set for a reminder
     * do so by comparing remmode to predetermined value in @class - constants.permitmode
     * @param remmode current remmode in expand view
     * @return 0 - not present 1-present
     */
    public static int isRepeat(int remmode){
        if (remmode == constants.permitMode[4] || remmode == constants.permitMode[5] )
            return 1;
        return 0;
    }


    /**
     * helper function to check if notification update is to be made
     * @param context
     * @param remData - the remider datas 0- isrem 1- istime, 2- timehr, 3- timemin, 4- isalarm, 5-isrepeat, 6-repeatmode
     * @param remdate - the new date of reminder
     * @param id - the original task id
     * @return one of the notification mode specified in {@link constants} task_update_mode
     */
    public static  int getNotificationUpdate(Context context, int[] remData, String remdate, int id){
        String selection = "_id = ?";
        String selectionArgs[] = {Integer.toString(id)};
        int[] isRem = LoadTaskHelper.loadTaskPartial(context,0,new int[]{4},selection,selectionArgs);

        if(isRem[0] == 0){
            if (remData[0] == 1)
                return constants.task_update_modes[0];
            else
                return constants.task_update_modes[1];
        }

        int remData_old[] = LoadTaskHelper.loadReminder(context, id);
        if (remData_old[1] == 0){ // time was not set
            if (remData[1] == 1) {
                return constants.task_update_modes[2];
            }
            else
                return constants.task_update_modes[0];
        }

        int[] timeData = LoadTaskHelper.loadTime(context, remData_old[0]);
        if (timeData[0] == remData[2] && timeData[1] == remData[3]) {
            if (timeData[2] == remData[4]) {
                if (timeData[3] == 1) {
                    if (remData[5] == 1)
                        if (timeData[4] == remData[6])
                            return constants.task_update_modes[0];
                        else
                            return constants.task_update_modes[3];
                } else if (remData[5] == 0)
                    return constants.task_update_modes[0];
                else
                    return constants.task_update_modes[3];
            }

            return constants.task_update_modes[3];

        } else {
            return constants.task_update_modes[3];
        }

    //    return constants.task_update_modes[0];

    }


    public static void setDone(Context context, int tid,int new_value){
        int reqCol[] = {7}; /** done */
        int newVal[] = {new_value};

        String selection = "_id = ? ";
        String selectionArgs[] = { Integer.toString(tid)};
        Tasks.update(context, 0, reqCol, newVal, selection, selectionArgs);
    }






}




