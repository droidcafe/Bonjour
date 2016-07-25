package droid.nir.testapp1.noveu.Util;

/**
 * Created by droidcafe on 2/20/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;

import android.widget.TextView;
import android.widget.Toast;

import com.wnafee.vector.MorphButton;

import java.util.Calendar;

import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.dB.metaValues.dBmetaData;

public class Import {

    Context context;

    public Import(Context context) {
        this.context = context;
    }

    public void maketext(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void maketextshort(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void settypefaces(String typefaceName, TextView textview) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceName);
        textview.setTypeface(typeface);

    }

    public void changeColorState(TextView textView, MorphButton morphButton, int colorState) {

        Log.d("addreminder", "change color " + textView.getText().toString());
        textView.setTextColor(context.getResources().getColorStateList(colorState));
        (morphButton).setForegroundTintList(context.getResources().getColorStateList(colorState));

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

    public static String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        return date + "/" + month + "/" + year;
    }


    public static String[] inttoStringColumn(int reqcolumn[], int tableNo) {

        String[] projection = new String[reqcolumn.length];

        for (int i = 0; i < reqcolumn.length; i++) {
            projection[i] = dBmetaData.columnNames[tableNo][reqcolumn[i]];

        }

        return projection;
    }

    public static boolean checkTable(SQLiteDatabase db, String tableName) {
        String query = "Select name From sqlite_master WHERE type= 'table'  AND name= ?";
        Cursor cursor = db.rawQuery(query, new String[]{tableName});

        return isGoodCursor(cursor);
    }

    public static boolean isGoodCursor(Cursor cursor) {
        if (cursor.moveToNext())
            return true;
        else
            return false;

    }

    public static void settypefaces(Context context, String typefaceName, TextView textview) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), typefaceName);
        textview.setTypeface(typeface);

    }

    public static void changeColorState(Context context, TextView textView, MorphButton morphButton, int colorState) {

        Log.d("addreminder", "change color " + textView.getText().toString());
        textView.setTextColor(context.getResources().getColorStateList(colorState));
        if (morphButton != null)
            (morphButton).setForegroundTintList(context.getResources().getColorStateList(colorState));

    }

    public static String[] getStringArray(Context context, int array) {
        return context.getResources().getStringArray(array);
    }

    public static String getString(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static String getStringFromCursor(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getIntFromCursor(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static void setSharedPref(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(key, value);
        editor.commit();
    }

    public static void setSharedPref(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public static int getSharedPref(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname,0);
        return  sharedPreferences.getInt(key, -1);
    }
    public static String getSharedPref( String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname,0);
        return  sharedPreferences.getString(key, null);
    }
}


