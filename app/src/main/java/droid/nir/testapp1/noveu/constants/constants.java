package droid.nir.testapp1.noveu.constants;

/**
 * Created by droidcafe on 2/28/2016.
 */
public class constants {

    public final static int VERSION = 10;
    public final static String VERSION_CODE  = "2.0.0";
    public final static String dev_mail  = "wiizzapps@gmail.com";
    public final static int REMINDER_REQUESTCODE = 101;
    public final static int SUBTASK_REQUESTCODE = 102;
    public final static int NOTES_REQUESTCODE = 103;
    public final static int WELCOME_REQUESTCODE = 434;
    public final static int NOTIFY_TASKS_PENDINGINTENT_REQUESTODE = 20;
    public final static int DEFAULT_PROJECT_ID = 4;


    public final static int SUCCESS_CODE = 400;
    public final static int ERROR_CODE = 404;

    public static String sharedprefs = "sharedprefs";

    public static String projectTaskTAG = "pt";

    public static int MIN_VALID_SECONDS = 300; /** in seconds */


    /**
     * the status values for each funtion in reminder screen
     * 1 date
     * 2 reminder
     * 4 alarm
     * 8 repeat
     */
    public static final int[] modeAdd = {0, 1, 2, 4, 8};
    /**
     * permited mode for reminder values in add reminder screen
     * 1 date  1
     * 3 date+reminder  2
     * 7 date +alarm+reminder  3
     * 11 date + reminder + repeat  4
     * 15 all  5
     */
    public static final int[] permitMode = {0, 1, 3, 7, 11, 15};


    /**
     * mode values
     * 0 = nothing
     * 1= r 1
     * 3 = s 2
     * 5 = n 3
     * 4 r+s 4
     * 6 n+r 5
     * 8 s+n 6
     * 9 r+S+n 7
     * r = reminder(1,4,6,9),s =sub(3,4,8,9), n = notes(5,6,8,9)
     */
    public static final int[] taskexpandmode = {0, 1, 3, 5, 4, 6, 8, 9};

    /**
     * permitted mode for repeat
     * 0 -daily
     * 1 - weekly
     * 2 - monthly
     * 3 - yearly
     */
    public static final int[] repeatMode = {0, 1, 2, 3};

    public static final int daily_sync_pending_intent_id = 1111;
    public static final int daily_sync_shared_pref = 2329;
    public static final int sync_now_pending_intent_id = 2222;

    /**
     * permitted mode for notification (in today_notification table)
     * 1- from tasks
     */
    public static final int[] notificationMode = {1, 2};

    public static final String notificationActionid1 = "111";
    public static final String notificationActionid2 = "222";

    public static final String share_task_intent_extra = "share";

    /**
     * different types of task updates. return by {@link droid.nir.testapp1.noveu.Tasks.TaskUtil}
     * isNotificationUpdate function
     * <p/>
     * 0 - no update is to be made
     * 1 - added reminder now only so notification is to be inserted
     * 2 - no time was set. Now time has been set (notification type changed from permanent to normal/alarm)
     * 3 - time details got changed (redo entire notification)
     */
    public static final int[] task_update_modes = {0, 1, 2, 3};


}
