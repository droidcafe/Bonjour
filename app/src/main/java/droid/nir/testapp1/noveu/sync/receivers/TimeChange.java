package droid.nir.testapp1.noveu.sync.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import droid.nir.testapp1.noveu.sync.alarms.DailySyncAlarm;

public class TimeChange extends BroadcastReceiver {
    public TimeChange() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("tc","recieve "+intent.getAction());
        DailySyncAlarm.setSyncAlarmNow(context);
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
