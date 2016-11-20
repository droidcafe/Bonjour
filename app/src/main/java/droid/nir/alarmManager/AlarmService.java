package droid.nir.alarmManager;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import droid.nir.testapp1.noveu.Util.Log;
import android.widget.Toast;


import droid.nir.testapp1.MainActivity;
import droid.nir.testapp1.R;


/**
 * Created by user on 7/18/2015.
 */
public class AlarmService extends Service  {

    @Override
    public int onStartCommand(Intent intentt, int flags, int startId) {
        super.onStartCommand(intentt, flags, startId);

        Log.d("trial in alarmservice", "startin service");

        NotificationManager alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Alarm"))
                .setContentText("haie from check here");


        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");


        return  START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("alarmservice oncreate", "startin service");

        Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();




    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}