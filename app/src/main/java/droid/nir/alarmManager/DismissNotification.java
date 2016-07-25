package droid.nir.alarmManager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import droid.nir.testapp1.noveu.Util.Log;

import java.io.IOException;
import java.util.Calendar;

import droid.nir.testapp1.R;

/**
 * Created by user on 8/24/2015.
 */
public class DismissNotification extends Service {

    MediaPlayer mMediaPlayer1,mMediaPlayer2,mMediaPlayer3;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int id = Integer.parseInt(intent.getStringExtra("oid"));
        int type = Integer.parseInt(intent.getStringExtra("type"));

        switch (type)
        {
            case 0:
               String status = intent.getStringExtra("status");
                Log.d("dismiss","of type 0 "+status);
                if(status.equals("start"))
                {
                    playSound(this, getAlarmUri(),0);
                }
                else  if(status.equals("stop"))
                {
                    String x = "0";
                    x = x.concat(Integer.toString(id));
                    int pendingintentid = Integer.parseInt(x);

                    NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(pendingintentid);
                    if(mMediaPlayer1!=null)
                    mMediaPlayer1.stop();
                }
                else if(status.equals("snooze"))
                {
                    String x = "0";
                    x = x.concat(Integer.toString(id));
                    int pendingintentid = Integer.parseInt(x);

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
                    mBuilder.setContentText("Alarm snoozed for 10 minutes");
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MINUTE, 10);
                    if(mMediaPlayer1!=null)
                    mMediaPlayer1.stop();

                    Intent dismissIntent = new Intent(this, DismissNotification.class);
                    dismissIntent.putExtra("oid", Integer.toString(id));
                    dismissIntent.putExtra("type", type);
                    dismissIntent.putExtra("status", "stop");
                    dismissIntent.setAction("ACTION DISMISS");

                    String xx = "55".concat(Integer.toString(id));
                    int kk = Integer.parseInt(xx);
                    PendingIntent cancelPendingIntent = PendingIntent.getService(this, kk, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    mBuilder.addAction(R.drawable.ic_alarm_off_black_24dp, "DISMISS ALARM", cancelPendingIntent);
                    NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(pendingintentid,mBuilder.build());
                }
                break;

            case 1:

                String statuss = intent.getStringExtra("status");
                Log.d("dismiss","of type 1 "+statuss);;
                if(statuss.equals("start"))
                {
                    playSound(this, getAlarmUri(),1);
                }
                else  if(statuss.equals("stop"))
                {
                    String x = "1";
                    x = x.concat(Integer.toString(id));
                    int pendingintentid = Integer.parseInt(x);

                    NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(pendingintentid);
                    if(mMediaPlayer2!=null)
                    mMediaPlayer2.stop();
                }

                else if(statuss.equals("snooze"))
                {
                    String x = "1";
                    x = x.concat(Integer.toString(id));
                    int pendingintentid = Integer.parseInt(x);

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
                    mBuilder.setContentText("Alarm snoozed for 10 minutes");
                    Calendar calendar = Calendar.getInstance();
                    if(mMediaPlayer2!=null)
                    mMediaPlayer2.stop();

                    NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(pendingintentid,mBuilder.build());
                }

                break;
            case 2:
                String x = "2";
                x = x.concat(Integer.toString(id));
                int pendingintentid = Integer.parseInt(x);

                NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(pendingintentid);
                break;

            case  3:
                String statusss = intent.getStringExtra("status");
                Log.d("dismiss","of type 3 "+statusss);;
                if(statusss.equals("start"))
                {
                    playSound(this, getAlarmUri(),3);
                }
                else  if(statusss.equals("stop"))
                {
                    String xx = "3";
                    xx = xx.concat(Integer.toString(id));
                    int ppendingintentid = Integer.parseInt(xx);

                    NotificationManager nnotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    nnotificationManager.cancel(ppendingintentid);
                    if(mMediaPlayer3!=null)
                    mMediaPlayer3.stop();
                }
        }


        return  START_REDELIVER_INTENT;
    }

    private void playSound(Context context, Uri alert,int type) {

        switch (type)
        {
            case 0:
                mMediaPlayer1 = new MediaPlayer();
                try {
                    mMediaPlayer1.setDataSource(context, alert);
                    final AudioManager audioManager = (AudioManager) context
                            .getSystemService(Context.AUDIO_SERVICE);
                    if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                        mMediaPlayer1.setAudioStreamType(AudioManager.STREAM_ALARM);
                        mMediaPlayer1.prepare();
                        mMediaPlayer1.start();
                    }
                } catch (IOException e) {
                    System.out.println("OOPS");
                }
                break;
            case 1:

                mMediaPlayer2 = new MediaPlayer();
                try {
                    mMediaPlayer2.setDataSource(context, alert);
                    final AudioManager audioManager = (AudioManager) context
                            .getSystemService(Context.AUDIO_SERVICE);
                    if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                        mMediaPlayer2.setAudioStreamType(AudioManager.STREAM_ALARM);
                        mMediaPlayer2.prepare();
                        mMediaPlayer2.start();
                    }
                } catch (IOException e) {
                    System.out.println("OOPS");
                }
                break;

            case 3:

                mMediaPlayer3 = new MediaPlayer();
                try {
                    mMediaPlayer3.setDataSource(context, alert);
                    final AudioManager audioManager = (AudioManager) context
                            .getSystemService(Context.AUDIO_SERVICE);
                    if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                        mMediaPlayer3.setAudioStreamType(AudioManager.STREAM_ALARM);
                        mMediaPlayer3.prepare();
                        mMediaPlayer3.start();
                    }
                } catch (IOException e) {
                    System.out.println("OOPS");
                }
                break;
        }

    }

    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
