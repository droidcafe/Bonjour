package droid.nir.testapp1.noveu.Tasks;

import droid.nir.testapp1.noveu.constants.constants;

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


}




