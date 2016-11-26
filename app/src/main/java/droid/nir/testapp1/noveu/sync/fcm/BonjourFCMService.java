package droid.nir.testapp1.noveu.sync.fcm;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Home.Home;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.notifications.handlers.NotificationHandler;

public class BonjourFCMService extends FirebaseMessagingService {

    public BonjourFCMService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("bfs", "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d("bfs", "message received " + remoteMessage.getData());
        }
        Map<String, String> data = remoteMessage.getData();
        PendingIntent pendingIntent = null;
        if (data != null) {
            String type = data.get("type");
            if (type == null) {
                pendingIntent = getDefaultPendingIntent();
                Log.d("bfs", "null type");
            } else if (type.equals(constants.fcm_type_modes[0])) { /** regular */
                Log.d("bfs", "regular type");
                pendingIntent = getDefaultPendingIntent();
            } else if (type.equals(constants.fcm_type_modes[1])) { /** update available */
                Log.d("bfs", "update type");
                String update_version = data.get("version");
                String version_present = Import.getSharedPref(SharedKeys.fcm_update_availabe, this);
                if (update_version.equals(version_present))
                    return;

                Import.setSharedPref(this, SharedKeys.fcm_update_availabe, update_version);
                Intent intent = Import.getPlayStoreIntent(this);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);
            }
        } else {
            Log.d("bfs", "data null");
            pendingIntent = getDefaultPendingIntent();
        }
        if (remoteMessage.getNotification() != null) {
            Log.d("bfs", "Message Notification Body: " +
                    remoteMessage.getNotification().getBody());
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            sendNotification(notification.getTitle(), notification.getBody(), pendingIntent);
        }
    }

    private void sendNotification(String title, String messageBody, PendingIntent pendingIntent) {
        title = (title == null) ? Import.getString(this, R.string.app_name) : title;
        pendingIntent = (pendingIntent == null) ? getDefaultPendingIntent() : pendingIntent;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.cc)
                .setColor(getResources().getColor(R.color.accent))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationHandler.notify(this, notificationBuilder.build(), (int) (456 * Math.random()) + 2);
    }

    private PendingIntent getDefaultPendingIntent() {
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
    }
}

