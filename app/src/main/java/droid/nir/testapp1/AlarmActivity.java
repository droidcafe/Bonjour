package droid.nir.testapp1;




public class AlarmActivity {



} /*extends WakefulBroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)


            Add_Event2 inst = Add_Event2.instance();
            inst.setAlarmText("Alarm! Wake up! Wake up!");

        Log.i("cjll","sdfksd");
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
*/