package droid.nir.testapp1.noveu.Util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import droid.nir.testapp1.noveu.constants.constants;

/**
 * Created by droidcafe on 3/26/2016.
 * <p/>
 * handler class for time,alarm and calendar related boiler codes
 */
public class TimeUtil {

    /**
     * sets  alarms for notifications , daily syncs. On firing of alarm a service or broadcast
     * reciever is called
     *
     * @param context
     * @param pendingIntent Pending intent to be executed on firing the alarm
     * @param calendar      time at which to fire the alarm
     */
    public static void setAlarm(Context context, PendingIntent pendingIntent, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    /**
     * cancels set alarms .
     *
     * @param context
     * @param pendingIntent the pending alarm whrich should have fired if alarm went off
     */
    public static void cancelAlarm(Context context, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static Calendar setCalendar(int timehr, int timemin) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timehr);
        calendar.set(Calendar.MINUTE, timemin);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

    public static Calendar setCalendar(Calendar calendar,int timehr, int timemin) {
        calendar.set(Calendar.HOUR_OF_DAY, timehr);
        calendar.set(Calendar.MINUTE, timemin);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

    public static Calendar setCalendar(Calendar calendar,int year, int month,int date) {
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, date);

        return calendar;
    }

    public static int getNowTime(int timefield) {
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
        return getDate(calendar);
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
     *
     * @param repeatMode the repeat mode selectd {0- daily 1- weekly 2- monthly 3- yearly} in constants
     * @return the new date variable on which task is to be repeted
     */
    public static Calendar getNewRepeatDate(int repeatMode) {
        Calendar calendar = Calendar.getInstance();

        if (repeatMode == constants.repeatMode[0]) {
            calendar.add(Calendar.DATE, 1);
        } else if (repeatMode == constants.repeatMode[1]) {
            calendar.add(Calendar.DATE, 7);
        } else if (repeatMode == constants.repeatMode[2]) {
            calendar.add(Calendar.MONTH, 1);
        } else if (repeatMode == constants.repeatMode[3]) {
            calendar.add(Calendar.YEAR, 1);
        }
        return calendar;
    }

    /**
     * helper class for showing time picker fragment on any screen.
     * To show  the time picker fragment just create a new instance of
     * this class using either of two {@link #newInstance(int, int, OnTimeSelectedListener)} or
     * {@link #newInstance(OnTimeSelectedListener)} method and just call
     * {@link DialogFragment#show(FragmentManager, String)} method.
     *<br>
     * Have to implement {@link OnTimeSelectedListener} in the activity and when user selects
     * a particular time the {@link OnTimeSelectedListener#timeChanged(int, int)} method gets called
      */
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        public int hour, min;
        OnTimeSelectedListener mCallBack;

        /**
         * constructor for creating a newInstance with initial time
         * set to now time
         * @param mCallBack
         * @return a newInstance of {@link TimePickerFragment}
         */
        public static TimePickerFragment newInstance(OnTimeSelectedListener mCallBack) {
            int hour = getNowTime(Calendar.HOUR_OF_DAY);
            int min = getNowTime(Calendar.MINUTE);
            return newInstance(hour, min, mCallBack);
        }

        /**
         * constructor for creating a newInstance with initial time
         * set to custom time
         * @param hour - hour to be set as default in time fragment
         * @param min -  min to be set as default in time fragment
         * @param callBack
         * @return a newInstance of {@link TimePickerFragment}
         */
        public static TimePickerFragment newInstance(int hour, int min, OnTimeSelectedListener callBack) {
            TimePickerFragment t = new TimePickerFragment();
            t.setOnTimeSelectedListener(callBack);
            Bundle args = new Bundle();
            args.putInt("hour", hour);
            args.putInt("minute", min);

            t.setArguments(args);
            return t;
        }

        public void initialize(OnTimeSelectedListener mCallBack) {
            this.mCallBack = mCallBack;
        }

        public void setOnTimeSelectedListener(OnTimeSelectedListener callback) {
            mCallBack = callback;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            Log.d("tu", "on attach");
            try {
                mCallBack = (OnTimeSelectedListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString() + " must implement OnTimeSelectedListener");
            }

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            Bundle args = getArguments();
            hour = args.getInt("hour");
            min = args.getInt("minute");
            Log.d("tu", "on create " + hour + " " + min);
            super.onCreate(savedInstanceState);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Log.d("tu", "on create dialog" + hour + " " + min);
            return new TimePickerDialog(getActivity(), this, hour, min, true);
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            Log.d("tu", "time " + " " + i + " " + i1);
            hour = i;
            min = i1;
            if (mCallBack != null)
                mCallBack.timeChanged(hour, min);

        }

        /**
         * interface for giving the hour and minute selected by user to
         * the calling activity which will have to implement this interface.
         */
        public interface OnTimeSelectedListener {
            /**
             * the callback function when time has changed
             * @param hour - selected hour
             * @param min - selected minute
             */
            void timeChanged(int hour, int min);
        }


    }
    /**
     * helper class for showing date picker fragment on any screen.
     * To show  the time picker fragment just create a new instance of
     * this class using either of two {@link #newInstance(OnDateSelectedListener)} or
     * {@link #newInstance(int, int, int,OnDateSelectedListener)} method and just call
     * {@link DialogFragment#show(FragmentManager, String)} method.
     *<br>
     * Have to implement {@link OnDateSelectedListener} in the activity and when user selects
     * a particular time the {@link OnDateSelectedListener#dataChanged(int, int, int)} method gets called
     */
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        int year,month,day;
        OnDateSelectedListener mCallback;

        /**
         * constructor for creating a newInstance with initial date set to today
         * @param mCallback
         * @return a newInstance of {@link DatePickerFragment}
         */
        public static DatePickerFragment newInstance(OnDateSelectedListener mCallback) {
            return newInstance(getNowTime(Calendar.YEAR),
                    getNowTime(Calendar.MONTH),getNowTime(Calendar.DAY_OF_MONTH),mCallback);
        }

        /**
         * constructor for creating a newInstance with initial date
         * set to custom date
         * @param year - custom year to be displayed
         * @param month - custom month to be displayed
         * @param day - custom day to be displayed
         * @param mCallback
         * @return a newInstance of {@link DatePickerFragment}
         */
        public static DatePickerFragment newInstance(int year,int month,int day,OnDateSelectedListener mCallback) {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.setOnDateSelectedListener(mCallback);

            Bundle args = new Bundle();
            args.putInt("year",year);
            args.putInt("month",month);
            args.putInt("day",day);

            datePickerFragment.setArguments(args);
            return datePickerFragment;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            Log.d("tu", "on attach");
            try {
                mCallback = (OnDateSelectedListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString() + " must implement OnTimeSelectedListener");
            }

        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Bundle args = getArguments();
            year = args.getInt("year");
            month = args.getInt("month");
            day = args.getInt("day");
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getActivity(),this,year,month,day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year = i;
            month = i1;
            day = i2;
            if (mCallback != null) {
                mCallback.dataChanged(i,i1,i2);
            }
        }

        public void setOnDateSelectedListener(OnDateSelectedListener callback) {
            mCallback = callback;
        }

        /**
         * interface for giving the date,month and year selected by user to
         * the calling activity which will have to implement this interface.
         */
        public interface OnDateSelectedListener{
            /**
             *  the callback function when date has changed
             * @param year - selected year
             * @param month - selected month
             * @param day - selected day
             */
            void dataChanged(int year, int month,int day);
        }
    }

}
