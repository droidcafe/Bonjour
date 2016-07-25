package droid.nir.testapp1.noveu.notifications.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.constants.IntentActions;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.notifications.NotificationActivity;
import droid.nir.testapp1.noveu.notifications.handlers.NotificationHandler;
import droid.nir.testapp1.noveu.sync.services.NotificationResponseService;
import droid.nir.testapp1.noveu.sync.services.PlayBackService;
import droid.nir.testapp1.noveu.sync.services.ShareService;

/**
 * Created by droidcafe on 3/28/2016.
 */
public class NotificationUtils {

    public enum NotificationMode {silent, permanent, alarm}

    public static void buildNotification(final Context context,
                                         final String title, final String contentText,
                                         int tid, NotificationCompat.Style notifStyle, int id, NotificationMode mode,
                                         int notificationType) {
        final Resources res = context.getResources();

        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.cc);

        final String ticker = res.getString(R.string.task_silent_notification_ticker, new Object[]{title});

        Intent intent_expand = new Intent(context, NotificationActivity.class);

        if(notificationType == constants.notificationMode[0])
             intent_expand.setAction(IntentActions.ACTION_NOTIFICATION_PROCEED_TASK);
        else
            intent_expand.setAction(IntentActions.ACTION_NOTIFICATION_PROCEED);

        Bundle bundle_expand = new Bundle();
        bundle_expand.putInt("taskid", tid);
        bundle_expand.putInt("choice", 1);
        intent_expand.putExtras(bundle_expand);

        Intent shareIntent = new Intent(context , NotificationActivity.class );
        if (notificationType == constants.notificationMode[0]) {
            shareIntent.setAction(IntentActions.ACTION_SHARE_TASK);
            shareIntent.putExtra("extra", constants.share_task_intent_extra);
            shareIntent.putExtras(bundle_expand);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_task_silent)
                .setContentTitle(title)
                .setContentText(contentText)
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                id,
                                intent_expand,
                                PendingIntent.FLAG_UPDATE_CURRENT))
                .setTicker(ticker)
                .setLargeIcon(picture)
                .setStyle(notifStyle)
                .addAction(
                        R.drawable.ic_action_stat_share,
                        context.getResources().getString(R.string.action_share),
                        PendingIntent.getActivity(context, getActionId(id , 1), shareIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT))
                ;

        if (mode == NotificationMode.silent)
            builder = NotificationUtils.buildNotificatonSilent(context, builder,id,notificationType);
        else if (mode == NotificationMode.permanent)
            builder = NotificationUtils.buildNotificatonPermanent(context, builder,id,notificationType);
        else if (mode == NotificationMode.alarm)
            builder = NotificationUtils.buildNotificatonAlarm(context, builder, id,intent_expand,notificationType);

        NotificationHandler.notify(context, builder.build(), id);
    }


    public static NotificationCompat.Builder buildNotificatonSilent(final Context context,
                                                                    NotificationCompat.Builder builder, int id, int notificationType) {


        builder
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

                .addAction(
                        R.drawable.ic_history_white_24dp,
                        context.getResources().getString(R.string.action_later),
                        PendingIntent.getActivity(
                                context,
                                0,
                                Intent.createChooser(new Intent(Intent.ACTION_SEND)
                                        .setType("text/plain")
                                        .putExtra(Intent.EXTRA_TEXT, "Dummy text"), "Dummy title"),
                                PendingIntent.FLAG_UPDATE_CURRENT))

                .setAutoCancel(true);

        return builder;
    }

    public static NotificationCompat.Builder buildNotificatonPermanent(final Context context,
                                                                       NotificationCompat.Builder builder,
                                                                       int id, int notificationType) {
        Intent action_intent = new Intent(context,NotificationResponseService.class);
        action_intent.setAction(IntentActions.ACTION_NOTIFICATION_DISMISSPERMANENT);
        action_intent.putExtra("nid", id);


        builder
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)


                .addAction(
                        R.drawable.ic_cancel_white_24dp,
                        context.getResources().getString(R.string.action_clear),
                        PendingIntent.getService(
                                context,
                                getActionId(id, 2),
                                action_intent,
                                PendingIntent.FLAG_UPDATE_CURRENT))

                .setAutoCancel(false)
                .setOngoing(true);

        return builder;
    }

    public static NotificationCompat.Builder buildNotificatonAlarm(final Context context,
                                                                   NotificationCompat.Builder builder, int id, Intent intent, int notificationType) {

        Intent dismissIntent = new Intent(context, NotificationResponseService.class)
                .setAction(IntentActions.ACTION_NOTIFICATION_DISMISSALARM).putExtra("nid", id);
        if(notificationType == constants.notificationMode[0])
            intent.setAction(IntentActions.ACTION_NOTIFICATION_PROCEED_ALARM_TASK);

        intent.putExtra("nid",id);
        builder
                .setDefaults(0)
                .setPriority(NotificationCompat.PRIORITY_MAX)

                .addAction(
                        R.drawable.ic_alarm_off_white_24dp,
                        context.getResources().getString(R.string.action_dismiss),
                        PendingIntent.getService(
                                context,
                                getActionId(id, 2),
                                dismissIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT))

                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT));


        return builder;
    }

    /**
     * creates an id for pending intent for the actions in notification
     * @param id the id of notification
     * @param choice which action 1st or 2nd
     * @return for 1st action 111+id , for 2nd 222+id
     */
    private static int getActionId(int id, int choice)
    {
        return (choice==1)?Integer.parseInt(constants.notificationActionid1+id):Integer.parseInt(constants.notificationActionid2+id);
    }

}
