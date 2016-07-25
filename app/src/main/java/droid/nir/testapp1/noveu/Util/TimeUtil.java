package droid.nir.testapp1.noveu.Util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import java.util.Calendar;

import droid.nir.testapp1.noveu.constants.constants;

/**
 * Created by droidcafe on 3/26/2016.
 *
 * handler class for time,alarm and calendar related boiler codes
 */
public class TimeUtil {

    /**
     * sets  alarms for notifications , daily syncs. On firing of alarm a service or broadcast
     * reciever is called
     * @param context
     * @param pendingIntent Pending intent to be executed on firing the alarm
     * @param calendar time at which to fire the alarm
     */
    public  static void setAlarm(Context context,PendingIntent pendingIntent , Calendar calendar)
    {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        if(Build.VERSION.SDK_INT>= 19)
        {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }
        else{
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    /**
     * cancels set alarms .
     * @param context
     * @param pendingIntent the pending alarm whrich should have fired if alarm went off
     */
    public static void cancelAlarm(Context context, PendingIntent pendingIntent)
    {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static Calendar setCalendar(int timehr,int timemin)
    {
        Calendar calendar  = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,timehr);
        calendar.set(Calendar.MINUTE,timemin);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

    public static int getNowTime(int timefield)
    {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(timefield);

    }

    public static String getDate(Calendar calendar) {
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        return getDate(date, month, year);
    }

    public static String getDate(int date, int month, int year) {
        return date + "/" + month + "/" + year;
    }
    public static String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        return  getDate(calendar);
    }

    public static String formatTime(int hr, int min) {

        String time = preCheckSingle(hr) + ":" + preCheckSingle(min) + "";
        return time;
    }

    private static String preCheckSingle(int x) {
        if (x / 10 < 1)
            return "0" + x;
        else
            return "" + x;
    }

    private static String preCheckHourClock(int hr) {
        if (hr >= 12)
            return "PM";
        else return "AM";
    }

    /**
     * helper function to calculate the new date on which the task is to be repeated based on
     * mode of repeat selected
     * @param repeatMode the repeat mode selectd {0- daily 1- weekly 2- monthly 3- yearly} in constants
     * @return the new date variable on which task is to be repeted
     */
    public static Calendar getNewRepeatDate(int repeatMode)
    {
        Calendar calendar = Calendar.getInstance();

        if (repeatMode == constants.repeatMode[0]) {
            calendar.add(Calendar.DATE,1);
        }
        else if (repeatMode == constants.repeatMode[1]) {
            calendar.add(Calendar.DATE,7);
        }
        else if (repeatMode == constants.repeatMode[2]) {
            calendar.add(Calendar.MONTH,1);
        }
        else if (repeatMode == constants.repeatMode[3]) {
            calendar.add(Calendar.YEAR,1);
        }
        return calendar;
    }
}
