package droid.nir.testapp1.noveu.sync.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
     * @param context
     * @param action IntentActions.ACTION_TASK_INSERT
     * @param ints the int required as extras for intent 0-tid, 1-isrem 2- done
     * @param date date of task
     */
    public static void broadCastDbChange(Context context,String action , int[] ints, String date)
    {
        Intent intent_broadcast = new Intent(action);
        intent_broadcast.putExtra("tid",ints[0]);
        intent_broadcast.putExtra("isrem",ints[1]);
        intent_broadcast.putExtra("done",ints[2]);
        intent_broadcast.putExtra("date", date);
        context.sendBroadcast(intent_broadcast);
    }

}
