package droid.nir.testapp1.noveu.Util;

/**
 * Created by droidcafe on 2/20/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wnafee.vector.MorphButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import droid.nir.testapp1.R;
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
    public static void maketext(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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
        editor.apply();
    }

    public static void setSharedPref(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);
        editor.apply();
    }

    public static int getSharedPref(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname, 0);
        return sharedPreferences.getInt(key, -1);
    }

    public static void updateSharedPref(Context context, String key, int updateValue) {
        int old_value = getSharedPref(context,key);
        int new_value = old_value + updateValue;
        setSharedPref(context,key,new_value);
    }

    public static void updateSharedPref(Context context, String key, String updateValue) {
        String  old_value = getSharedPref(key,context);
        String new_value = old_value.concat(updateValue);
        setSharedPref(context,key,new_value);
    }

    public static String getSharedPref(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname, 0);
        return sharedPreferences.getString(key, null);
    }

    /**
     * helper function for getting settings shared prefs
     *
     * @param context
     * @param key     key of shared preference
     * @param type    type of preference 1 - string 2- integer 3 - boolean
     * @return
     */
    public static Object getSettingSharedPref(Context context, String key, int type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname_setting, 0);

        if (sharedPreferences == null)
            return null;

        if (type == 1)
            return sharedPreferences.getString(key, null);

        else if (type == 2)
            return sharedPreferences.getInt(key, -1);

        else if (type == 3)
            return sharedPreferences.getBoolean(key, true);

        return null;
    }

    /**
     * helper function for setting settings shared prefs
     *
     * @param context
     * @param key     key of shared preference
     * @param value   the value of key to be set
     * @param type    type of preference 1 - string 2- integer 3 - boolean
     * @return
     */
    public static void setSettingSharedPref(Context context, String key, Object value, int type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname_setting, 0);
        if (sharedPreferences == null)
            return;

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (type == 1)
            editor.putString(key, (String) value);
        else if (type == 2)
            editor.putInt(key, (Integer) value);

        else if (type == 3)
            editor.putBoolean(key, (Boolean) value);

        editor.apply();
    }

    /**
     * helper function for getting integer extras present in bundle - extras are to be named like extra0, extra1..extraN (N = length -1)
     *
     * @param bundle
     * @param length - the no of extras present
     * @return the array of extras present
     */
    public static int[] getIntExtraArguments(Bundle bundle, int length) {
        int extras[] = new int[length];
        for (int i = 0; i < length; i++) {
            extras[i] = bundle.getInt("extra" + i, -1);
        }

        return extras;
    }

    /**
     * function for returning default notification sound
     *
     * @return uri of system default notificatin sound
     */
    public static Uri getDefaultNotificationSound() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

    public static void setStatusBarColor(Context context, Activity activity, int color) {
        Window window = activity.getWindow();

        if (isVersionOK(Build.VERSION_CODES.LOLLIPOP)) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(context.getResources().getColor(color));

        }
    }

    public static void setBackGroundColor(Context context, Activity activity, int viewId, int color) {
        activity.findViewById(viewId).setBackgroundColor(context.getResources().
                getColor(color));
    }

    public static void setToolBarColor(Context context, Activity activity, int toolbarid,int color){
       activity.findViewById(toolbarid).setBackgroundColor(context.getResources().getColor(color));
    }

    public  static void allDone(Context context, ImageView alldone_pic, TextView alldone_title,
                               TextView alldone_promo) {
        alldone_pic.setVisibility(View.VISIBLE);
        alldone_promo.setVisibility(View.VISIBLE);
        alldone_title.setVisibility(View.VISIBLE);

        settypefaces(context, "Raleway-Medium.ttf", alldone_title);
        settypefaces(context, "SourceSansPro-Regular.otf", alldone_promo);

        Random random = new Random();
        int rand1 = random.nextInt(8);
        int rand2 = random.nextInt(4);

        String imageId = "alldone"+rand1;
        String titleId = "alldone"+rand2;
      //  Glide.with(context).load(getResource(context,imageId,"drawable")).into(alldone_pic);

        alldone_pic.setImageResource(getResource(context,imageId,"drawable"));
        alldone_title.setText(getResource(context,titleId,"string"));

    }

    /**
     * helper function for undoing all done . when list is non empty
     * @param context
     * @param alldone_pic
     * @param alldone_title
     * @param alldone_promo
     */
    public static void allDoneUndo(Context context, ImageView alldone_pic, TextView alldone_title,
                                   TextView alldone_promo) {
        alldone_pic.setVisibility(View.GONE);
        alldone_promo.setVisibility(View.GONE);
        alldone_title.setVisibility(View.GONE);

    }

    /**
     * helper function to get resource id of a resource
     * @param context
     * @param resourceId the id of resource
     * @param prefix - prefix like drawable, string,array - where is resource is
     * @return the id
     */
    public static int getResource(Context context, String resourceId,String prefix) {

        String uri = prefix +"/" + resourceId;
        return context.getResources().getIdentifier(uri, null, context.getPackageName());
    }


    /**
     * helper function for composing mail
     * @param activity
     * @param addresses
     * @param subject
     * @param attachment
     */
    public static void composeEmail(Activity activity, String[] addresses, String subject, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (attachment != null)
           intent.putExtra(Intent.EXTRA_STREAM, attachment);

        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    public static void getLogs(){
        File filename = new File(Environment.getExternalStorageDirectory()+"/bonjour.log");
        try {
            boolean file = filename.createNewFile();
            Log.d("import", "created file " + file + " " + filename.getAbsolutePath());

            String cmd = "logcat -d -f"+filename.getAbsolutePath();
            Runtime.getRuntime().exec(cmd);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * to check if a list is null or not . If null
     * a new empty list is returned
     * @param list - the specified list
     * @return - the given list if not null else a new
     * empty list
     */
    private static List<?> checkList(List<?> list)
    {
        if(list==null)
            return new ArrayList<>(0);
        else
            return list;
    }

    public static  boolean isVersionOK(int base_version) {
        return ( Build.VERSION.SDK_INT >=  base_version) ;
    }

    public static Intent getPlayStoreIntent(Context context) {
        return new Intent(Intent.ACTION_VIEW,
                Uri.parse(context.getResources().getString(R.string.app_uri)));
    }

}


