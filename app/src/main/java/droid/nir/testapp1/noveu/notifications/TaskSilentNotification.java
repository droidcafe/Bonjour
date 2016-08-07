package droid.nir.testapp1.noveu.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;

import java.util.List;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Tasks.Add_Expand;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.notifications.handlers.NotificationHandler;
import droid.nir.testapp1.noveu.notifications.util.NotificationUtils;


/**
 * contains fuctions   related with creating notifications for tasks
 * for helper funcions - {@link droid.nir.testapp1.noveu.notifications.util.TaskNotificationHelper}
 */
public class TaskSilentNotification {



    /**
     * for creating notification for task items with todolist. Will remove the notification when click
     *
     * @param context
     * @param title            title of notification
     * @param project          project task belong to
     * @param taskList         list of todo list in the task
     * @param id               id of task in the today_notification table
     * @param tid              id of task
     * @param notificationMode mode of notification an enum value -silent,permanent,alarm
     * @param notificationType type of notification 1- from tasks
     */
    public static void notify(final Context context, String title, String project, List<String> taskList,
                              final int id, int tid, NotificationUtils.NotificationMode notificationMode,
                              int notificationType) {
        String listString = taskList.toString();


        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String summartText = context.getResources().getString(R.string.task_silent_notification_list_summary, taskList.size(), project);

        for (int i = 0; i < taskList.size() && i < 10; i++)
            inboxStyle.addLine(taskList.get(i));

        inboxStyle.setSummaryText(summartText);
        inboxStyle.setBigContentTitle(title);

        NotificationUtils.buildNotification(context, title, listString, tid, inboxStyle, id, notificationMode,
                notificationType);


    }

    /**
     * for creating notification for task items without todolist using either notes if present or
     * using a default string containg the task time . Will remove the notification when click
     *
     * @param context
     * @param title            title of notification
     * @param project          project task belong to
     * @param bigText          the big text to display in expanded mode (notes if present)
     * @param id               id of task in the today_notification table
     * @param tid              id of task
     * @param notificationMode mode of notification an enum value -silent,permanent,alarm
     * @param notificationType type of notification 1- from tasks
     */

    public static void notify(final Context context, String title, String project
            , String bigText, final int id, int tid, NotificationUtils.NotificationMode notificationMode,
                              int notificationType) {

        Log.d("tsn", "notify " + title);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        String summaryText = context.getResources().getString(R.string.task_silent_notification_text_summary, new Object[]{project});

        bigTextStyle.setBigContentTitle(title)
                .setSummaryText(summaryText)
                .bigText(bigText);

        NotificationUtils.buildNotification(context, title,
                bigText, tid, bigTextStyle, id, notificationMode,notificationType);
    }





}
