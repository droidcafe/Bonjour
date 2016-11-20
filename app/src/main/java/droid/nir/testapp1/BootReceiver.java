package droid.nir.testapp1;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



import droid.nir.alarmManager.CheckForToday;
import droid.nir.testapp1.toast;

/**
 * Created by user on 7/19/2015.
 */
public class BootReceiver  extends BroadcastReceiver{


    public void onReceive(Context context, Intent intent) {

        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {
            CheckForToday checkForToday = new CheckForToday();
            checkForToday.schedule(context);
        }
       // toast maketext  = new toast(context);
     //   maketext.makeText("Setting Alarms on booting up");
    }
}
