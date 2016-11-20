package droid.nir.testapp1.noveu.alarm;

import android.media.RingtoneManager;
import android.net.Uri;

/**
 * Created by droidcafe on 3/28/2016.
 */
public class AlarmUtils {

    public static Uri getAlarmUri() {
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
}
