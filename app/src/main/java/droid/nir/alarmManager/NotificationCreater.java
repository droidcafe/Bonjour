package droid.nir.alarmManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import droid.nir.testapp1.noveu.Util.Log;


import droid.nir.testapp1.MainActivity;
import droid.nir.testapp1.R;
import droid.nir.testapp1.ShowDecision;
import droid.nir.testapp1.ShowEvent;
import droid.nir.testapp1.ShowList;
import droid.nir.testapp1.ShowRemainder;

/**
 * Created by user on 8/23/2015.
 */
public class NotificationCreater  extends BroadcastReceiver{

    int choice = 0;
    Context context;


   /* NotificationCreater(String[] passinfo,int m,int[] id,Context context)
    {
        this.context = context;
        switch (m)
        {
            case 2:
                setuplistnotification(passinfo,id);
                break;
        }
    }*/


    private void setuplistnotification(String title,String text, int id) {

        Log.d("notificationcreater", "starting broadcast " + title + " " + id);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_format_list_bulleted_white_24dp)
                        .setContentTitle(""+title)
                        .setContentText(text)
                        .setPriority(2)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setOngoing(true);

        Intent resultIntent = new Intent(context, ShowList.class);
        resultIntent.putExtra("oid", id);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(ShowList.class);

        String x = "2";
        x = x.concat(Integer.toString(id));
        int pendingintentid = Integer.parseInt(x);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        pendingintentid,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        Intent dismissIntent = new Intent(context, DismissNotification.class);
        dismissIntent.putExtra("oid", Integer.toString(id));
        String type = Integer.toString(2);
        dismissIntent.putExtra("type", type);
        dismissIntent.setAction("ACTION DISMISS");
        String xx = "55".concat(Integer.toString(id));
        int kk = Integer.parseInt(xx);
        PendingIntent cancelPendingIntent = PendingIntent.getService(context,kk , dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);


        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setSummaryText("Clear Notification");
        bigTextStyle.bigText(text);
        mBuilder.addAction(R.drawable.ic_cancel_white_24dp,"CLEAR NOTIFICATION",cancelPendingIntent);
        //mBuilder.setStyle(bigTextStyle);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        Log.d("notificaationcreater","notification id is "+pendingintentid);
        mNotificationManager.notify(pendingintentid,mBuilder.build());
    }


    @Override
    public void onReceive(Context context, Intent intent) {


        Log.d("notificationcreater","on recieve");
        String title = intent.getStringExtra("title");
        String statement = intent.getStringExtra("text");
        int id = Integer.parseInt(intent.getStringExtra("oid"));
        int type =  Integer.parseInt(intent.getStringExtra("type"));


        this.context = context;
        switch (type)
        {
            case  0:
                String alarm = intent.getStringExtra("isalarm");
                int isalarm =Integer.parseInt(alarm);
                setuppendingnotification(title,statement,id,isalarm);
                break;

            case 1:
                String alarm1 = intent.getStringExtra("isalarm");
                int isalarm1 =Integer.parseInt(alarm1);
                setupeventnotification(title, statement, id, isalarm1);
                break;

            case 2:
                setuplistnotification(title,statement,id);
                break;

            case  3:
                String alarm2 = intent.getStringExtra("isalarm");
                int isalarm2 =Integer.parseInt(alarm2);
                setupremaindernotification(title, statement, id, isalarm2);
                break;
        }

    }

    private void setupeventnotification(String title, String text, int id, int isalarm) {
        Log.d("notificationcreater", "starting broadcast " + title + " " + id);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_event_white_24dp)
                        .setContentTitle("" + title)
                        .setContentText(text)

                ;
        Intent resultIntent = new Intent(context, ShowEvent.class);
        resultIntent.putExtra("oid", id);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(ShowEvent.class);

        String x = "1";
        x = x.concat(Integer.toString(id));
        int pendingintentid = Integer.parseInt(x);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        pendingintentid,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        if (isalarm == 1) {

            mBuilder.setPriority(2).setOngoing(true);
            mBuilder.setDefaults(0);


            Intent startIntent = new Intent(context, DismissNotification.class);
            startIntent.putExtra("oid", Integer.toString(id));
            String type = Integer.toString(1);
            startIntent.putExtra("type", type);
            startIntent.putExtra("status", "start");
            startIntent.setAction("ACTION DISMISS");
            context.startService(startIntent);

            Intent dismissIntent = new Intent(context, DismissNotification.class);
            dismissIntent.putExtra("oid", Integer.toString(id));
            dismissIntent.putExtra("type", type);
            dismissIntent.putExtra("status", "stop");
            dismissIntent.setAction("ACTION DISMISS");



            String xx = "4444".concat(Integer.toString(id));
            int kk = Integer.parseInt(xx);
          //  xx = "66".concat(Integer.toString(id));
            //int kkk = Integer.parseInt(xx);
            PendingIntent cancelPendingIntent = PendingIntent.getService(context, kk, dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            //PendingIntent snoozePendingIntent = PendingIntent.getService(context, kkk, snoozeIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            // mBuilder.addAction(R.drawable.ic_snooze_black_24dp, "SNOOZE", snoozePendingIntent);
            mBuilder.addAction(R.drawable.ic_alarm_off_black_24dp, "DISMISS ALARM", cancelPendingIntent);


        } else {
            mBuilder.setAutoCancel(true);
            mBuilder.setDefaults(Notification.DEFAULT_ALL);
            mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }


        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        Log.d("notificaationcreater","notification id is "+pendingintentid);
        mNotificationManager.notify(pendingintentid, mBuilder.build());
    }

    private void setuppendingnotification(String title, String text, int id,int isalarm) {

        Log.d("notificationcreater", "starting broadcast " + title + " " + id);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_hourglass_empty_white_24dp)
                        .setContentTitle("" + title)
                        .setContentText(text);


        Intent resultIntent = new Intent(context, ShowDecision.class);
        resultIntent.putExtra("oid", id);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(ShowDecision.class);

        String x = "0";
        x = x.concat(Integer.toString(id));
        int pendingintentid = Integer.parseInt(x);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        pendingintentid,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        if (isalarm == 1) {

            mBuilder.setPriority(2).setOngoing(true);
            mBuilder.setDefaults(0);


            Intent startIntent = new Intent(context, DismissNotification.class);
            startIntent.putExtra("oid", Integer.toString(id));
            String type = Integer.toString(0);
            startIntent.putExtra("type", type);
            startIntent.putExtra("status", "start");
            startIntent.setAction("ACTION DISMISS");
            context.startService(startIntent);

            Intent dismissIntent = new Intent(context, DismissNotification.class);
            dismissIntent.putExtra("oid", Integer.toString(id));
            dismissIntent.putExtra("type", type);
            dismissIntent.putExtra("status", "stop");
            dismissIntent.setAction("ACTION DISMISS");

            Intent snoozeIntent = new Intent(context, DismissNotification.class);
            snoozeIntent.putExtra("oid", Integer.toString(id));
            snoozeIntent.putExtra("type", type);
            snoozeIntent.putExtra("status","snooze");
            snoozeIntent.setAction("ACTION DISMISS");

            String xx = "55".concat(Integer.toString(id));
            int kk = Integer.parseInt(xx);
             xx = "66".concat(Integer.toString(id));
            int kkk = Integer.parseInt(xx);
            PendingIntent cancelPendingIntent = PendingIntent.getService(context, kk, dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            PendingIntent snoozePendingIntent = PendingIntent.getService(context, kkk, snoozeIntent, PendingIntent.FLAG_CANCEL_CURRENT);

           // mBuilder.addAction(R.drawable.ic_snooze_black_24dp, "SNOOZE", snoozePendingIntent);
            mBuilder.addAction(R.drawable.ic_alarm_off_black_24dp, "DISMISS ALARM", cancelPendingIntent);


        } else {
            mBuilder.setAutoCancel(true);
            mBuilder.setDefaults(Notification.DEFAULT_ALL);
            mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        }


        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        Log.d("notificaationcreater","notification id is "+pendingintentid);
        mNotificationManager.notify(pendingintentid, mBuilder.build());
    }

    private void setupremaindernotification(String title, String text, int id,int isalarm) {

        Log.d("notificationcreater", "starting broadcast " + title + " " + id);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_alarm_add_white_24dp)
                        .setContentTitle("" + title)
                        .setContentText(text);


        Intent resultIntent = new Intent(context, ShowRemainder.class);
        resultIntent.putExtra("oid", id);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(MainActivity.class);

        String x = "3";
        x = x.concat(Integer.toString(id));
        int pendingintentid = Integer.parseInt(x);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        pendingintentid,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        if (isalarm == 1) {

            mBuilder.setPriority(2).setOngoing(true);
            mBuilder.setDefaults(0);


            Intent startIntent = new Intent(context, DismissNotification.class);
            startIntent.putExtra("oid", Integer.toString(id));
            String type = Integer.toString(3);
            startIntent.putExtra("type", type);
            startIntent.putExtra("status", "start");
            startIntent.setAction("ACTION DISMISS");
            context.startService(startIntent);

            Intent dismissIntent = new Intent(context, DismissNotification.class);
            dismissIntent.putExtra("oid", Integer.toString(id));
            dismissIntent.putExtra("type", type);
            dismissIntent.putExtra("status", "stop");
            dismissIntent.setAction("ACTION DISMISS");

            Intent snoozeIntent = new Intent(context, DismissNotification.class);
            snoozeIntent.putExtra("oid", Integer.toString(id));
            snoozeIntent.putExtra("type", type);
            snoozeIntent.putExtra("status","snooze");
            snoozeIntent.setAction("ACTION DISMISS");

            String xx = "78".concat(Integer.toString(id));
            int kk = Integer.parseInt(xx);

            PendingIntent cancelPendingIntent = PendingIntent.getService(context, kk, dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT);
         //   PendingIntent snoozePendingIntent = PendingIntent.getService(context, kkk, snoozeIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            // mBuilder.addAction(R.drawable.ic_snooze_black_24dp, "SNOOZE", snoozePendingIntent);
            mBuilder.addAction(R.drawable.ic_alarm_off_black_24dp, "DISMISS ALARM", cancelPendingIntent);


        } else {
            mBuilder.setAutoCancel(true);
            mBuilder.setDefaults(Notification.DEFAULT_ALL);
            mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        }


        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        Log.d("notificaationcreater","notification id is "+pendingintentid);
        mNotificationManager.notify(pendingintentid, mBuilder.build());
    }
}
