package droid.nir.testapp1.noveu.sync.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import droid.nir.testapp1.noveu.Util.Log;

import java.io.IOException;

import droid.nir.testapp1.noveu.constants.IntentActions;
import droid.nir.testapp1.noveu.notifications.handlers.NotificationHandler;

public class PlayBackService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer;
    private static Context context;
    private Uri soundUri;


    public static void startPlayBack(Context context, Uri uri, boolean vibrate) {
        PlayBackService.context = context.getApplicationContext();

        Intent i = new Intent(context.getApplicationContext(), PlayBackService.class);
        i.setAction(IntentActions.ACTION_PLAY_START);
        i.putExtra("soundUri", uri);
        i.putExtra("vibrate", vibrate);

        context.getApplicationContext().startService(i);
    }

    public static void stopPlayBack(Context context) {
        Log.d("pbs","cancel");

        Intent intent = new Intent(context, PlayBackService.class);
        intent.setAction(IntentActions.ACTION_PLAY_STOP);
        //intent.putExtra("nid",nid);
        context.getApplicationContext().startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            switch (intent.getAction()) {
                case IntentActions.ACTION_PLAY_START:
                    processPlay(intent);
                    break;
                case IntentActions.ACTION_PLAY_STOP:
                    processStop(intent);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void processPlay(Intent intent) {
        soundUri = null;
        if (intent.hasExtra("soundUri")) {
            soundUri = intent.getParcelableExtra("soundUri");
        }

        boolean vibrate = intent.getBooleanExtra("vibrate", false);
        if (soundUri != null) {
            playAlarm();
        }
        if (vibrate)
            startVibrate();

    }

    private void processStop(Intent intent) {
     //   int id = intent.getIntExtra("nid",-1);
        Log.d("pbs", "stop ");
     //   NotificationHandler.cancel(context,id);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        stopSelf();
    }

    private void startVibrate() {

    }

    private void playAlarm() {
        setMediaPlayer();
        try {
            mediaPlayer.setDataSource(this, soundUri);
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }

    private void setMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            //mediaPlayer.setOnErrorListener(this);
            return;
        }

        mediaPlayer.reset();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playAlarm();
    }
}
