package droid.nir.testapp1.noveu.sync.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.sync.services.TaskChangeService;

public class DbChangeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("dcr", "" + intent.getAction() + " " + intent.getIntExtra("tid", -1));
        TaskChangeService.startTaskChange(context, intent);
    }


    /**
     * function to broadcast a db change for task
     *
     * @param context
     * @param action  {@link droid.nir.testapp1.noveu.constants.IntentActions}
     * @param ints    the int required as extras for intent 0-tid, 1-isrem 2- done
     * @param date    date of task
     * @param extras  to add any extra flags to the broadcaset intent
     *                like notification_update_mode
     *                ({@link droid.nir.testapp1.noveu.constants.constants })
     *                value in case of task update
     */
    public static void broadCastDbChange(Context context, String action, int[] ints, String date, int[] extras) {
        Intent intent_broadcast = new Intent(action);
        Bundle broadcast_bundle = new Bundle();
        broadcast_bundle.putInt("tid", ints[0]);
        broadcast_bundle.putInt("isrem", ints[1]);
        broadcast_bundle.putInt("done", ints[2]);
        broadcast_bundle.putString("date", date);

        int extra_length = (extras != null) ?  extras.length : 0;

        broadcast_bundle.putInt("extras", extra_length);

        for (int i = 0; i < extra_length; i++) {
            broadcast_bundle.putInt("extra" + i, extras[i]);
        }
        intent_broadcast.putExtra("broadcast_bundle", broadcast_bundle);
        context.sendBroadcast(intent_broadcast);
    }


}
