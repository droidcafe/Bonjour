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
import droid.nir.testapp1.noveu.Util.Log;

import java.util.List;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Tasks.Add_Expand;
import droid.nir.testapp1.noveu.notifications.handlers.NotificationHandler;
import droid.nir.testapp1.noveu.notifications.util.NotificationUtils;

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


    private static void buildNotificaton(final Context context,
                                         final String title, final String contentText, int tid, NotificationCompat.Style notifStyle, int id) {
        final Resources res = context.getResources();

        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.cc);


        final String ticker = res.getString(R.string.task_silent_notification_ticker, new Object[]{title});

        Intent intent_expand = new Intent(context, Add_Expand.class);

        Bundle bundle_expand = new Bundle();
        bundle_expand.putInt("taskid", tid);
        bundle_expand.putInt("choice", 1);

        intent_expand.putExtras(bundle_expand);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_stat_task_silent)
                .setContentTitle(title)
                .setContentText(contentText)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(picture)
                .setTicker(ticker)
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                id,
                                intent_expand,
                                PendingIntent.FLAG_UPDATE_CURRENT))
                .setStyle(notifStyle)

                .addAction(
                        R.drawable.ic_action_stat_share,
                        res.getString(R.string.action_share),
                        PendingIntent.getActivity(
                                context,
                                0,
                                Intent.createChooser(new Intent(Intent.ACTION_SEND)
                                        .setType("text/plain")
                                        .putExtra(Intent.EXTRA_TEXT, "Dummy text"), "Dummy title"),
                                PendingIntent.FLAG_UPDATE_CURRENT))
                .addAction(
                        R.drawable.ic_action_stat_reply,
                        res.getString(R.string.action_reply),
                        null)

                        // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);

        NotificationHandler.notify(context, builder.build(), id);

    }


}
