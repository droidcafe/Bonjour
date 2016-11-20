package droid.nir.testapp1.noveu.today;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.Util.TimeUtil;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.dB.DBProvider;
import droid.nir.testapp1.noveu.dB.Today;

/**
 * Created by droidcafe on 3/26/2016.
 */
public class TodayNotificationHelper {

    /**
     * loads notification data from today notification
     *
     * @param context
     * @param selection
     * @param selectionArgs
     * @param columnreq
     * @return
     */
    public static Cursor loadNotificationData(Context context, String selection, String[] selectionArgs, int[] columnreq) {
        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, Today.tableNames[0]);
        String[] projection = Import.inttoStringColumn(columnreq, 7);
        Cursor cursor = Today.select(context, 0, selection, selectionArgs, projection);
        return cursor;

    }

    /**
     * decodes the cursor containing data from today notification table
     *
     * @param cursor    cursor to be decoded
     * @param reqcolumn array of column required from cursor .. order in reqcolumn will the order in return int[]
     * @return
     */
    public static int[] decodeNotificationData(Cursor cursor, int[] reqcolumn) {
        if(cursor.getColumnCount() == 0)
            return null;
        int retint[] = new int[cursor.getColumnCount()];
        for (int i = 0; i < cursor.getColumnCount(); i++)
            retint[i] = cursor.getInt(cursor.getColumnIndex(Today.columnNames[0][reqcolumn[i]]));

        return retint;
    }

    /**
     * function for inserting to todaynotification table
     *
     * @param context
     * @param tid              id of task attached to notification
     * @param timeData         array related to time data for notification 0-timehr
     *                         1-timemin 2- mode of notification (0- silent type 1- alarm type 2-permanent type)
     * @param notificationMode the mode of notification (1 - for task 2- )
     * @return id of the row inserted
     */
    public static int insertTodayNotification(Context context, int tid,
                                              int timeData[], int notificationMode) {
        int[] passInt = new int[6];
        passInt[0] = notificationMode;
        passInt[1] = tid;
        passInt[2] = timeData[0];
        passInt[3] = timeData[1];

        passInt[4] = timeData[2];
        passInt[5] = 0; /** isfired **/
        Uri insertedUri = Today.insert(context, 0, passInt);
        int id = Integer.parseInt(insertedUri.getLastPathSegment());
        return id;

    }

    /**
     * function to update todaynotification table
     *
     * @param context
     * @param id      id of row to be updated
     * @param passInt 0-mode 1-oid 2-timehr 3-timemin 4-mode of notification(0- silent 1-alarm 2-permanent)
     *                5 - isfired
     * @return no of rows affected
     */

    public static int updateTodayNotification(Context context, int id, int passInt[]) {
        String selection = "_id = ?";
        String selectionArgs[] = {String.valueOf(id)};
        return Today.update(context, 0, passInt, selection, selectionArgs);
    }

    /**
     * function to delete rows in notification table
     * @param context
     * @param id      id of row to be deleted
     * @return no of rows affected
     */
    public static int deleteTodayNotification(Context context, int id){
        String selection = "_id = ?";
        String selectionArgs[] = {String.valueOf(id)};
        return Today.delete(context,0,selection,selectionArgs);
    }

    /**
     * to update isfired column in today_notification to 1 or o
     *
     * @param context
     * @param newValue the value to be updated to - {0 or 1}
     * @param id       the id of entry in today_notication table
     * @return no of rows affected
     */
    public static int updateIsFired(Context context, int newValue, int id) {

        String selection = "_id = ?";
        String selectionArgs[] = {String.valueOf(id)};
        return Today.update(context, 0, new int[]{6},
                new int[]{newValue}, selection,
                selectionArgs);
    }

    /**
     * checks if the task should be inserted to today_notification list
     * compares if done == 0 , date is of today and isreminder == 1
     *
     * @param passInt 0-id 1-isrem 2-done
     * @param date    date of task
     * @return
     */
    public static boolean isGoodTask(int passInt[], String date) {
        Log.d("tcs", "done is " + passInt[2] + "isrem is  " + passInt[1] + " " + date);
        if (passInt[2] == 0 && date.equals(TimeUtil.getTodayDate()) && passInt[1] == 1)
            return true;
        else
            return false;
    }

    /**
     * checks if the task should be updated in today_notification list
     * compares if done == 0 , date is of today and isreminder == 1
     *
     * @param passInt            0-id 1-isrem 2-done
     * @param date               date of task
     * @param notificationUpdate {@link constants#task_update_modes}
     * @return
     */
    public static boolean isGoodTask(int passInt[], String date, int notificationUpdate) {
        Log.d("tcs", "" + passInt[2] + " " + passInt[1] + " " + date);
        if (passInt[2] == 0 && date.equals(TimeUtil.getTodayDate()) && passInt[1] == 1 && notificationUpdate != 0)
            return true;
        else
            return false;
    }

    /**
     * checks if the task should be inserted in today_notification list
     *
     * @param cursor the cursor containing details from task table
     * @return
     */
    public static boolean isGoodTask(Cursor cursor) {
        int id = Import.getIntFromCursor(cursor, "_id");
        int isrem = Import.getIntFromCursor(cursor, "isrem");
        int done = Import.getIntFromCursor(cursor, "done");

        String date = Import.getStringFromCursor(cursor, "date");
        return isGoodTask(new int[]{id, isrem, done}, date);
    }

    /**
     * checks if time is valid or not . Valid time is which is greater than or minimum {@value constants#MIN_VALID_SECONDS}
     * less than present time
     * @param hour set hour for notification
     * @param min set minute for notification
     * @return whether time is valid or not
     * @throws ParseException
     */
    public static boolean isValidTime(int hour, int min) {
        int nowhr = TimeUtil.getNowTime(Calendar.HOUR_OF_DAY);
        int nowmin = TimeUtil.getNowTime(Calendar.MINUTE);


        String time_now = "" + nowhr + ":" + nowmin + ":00";
        String time_set = "" + hour + ":" + min + ":00";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date_now = null;
        try {
            date_now = simpleDateFormat.parse(time_now);
            Date date_set = simpleDateFormat.parse(time_set);
            long timeDiff = (date_now.getTime() - date_set.getTime());
            if (timeDiff < 0)
                return true;
            timeDiff /= 1000;
            if (timeDiff < constants.MIN_VALID_SECONDS)
                return true;
        } catch (ParseException e) {
           Log.e("tnh",""+e.toString());
            return  false;
        }


        return false;
    }
}
