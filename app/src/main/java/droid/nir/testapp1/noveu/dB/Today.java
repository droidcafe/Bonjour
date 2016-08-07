package droid.nir.testapp1.noveu.dB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import droid.nir.testapp1.toast;

/**
 * Created by droidcafe on 2/23/2016.
 */
public class Today {

    Context context;
    public static String[][] columnNames = {
            {"_id", "mode", "oid", "timehr", "timemin", "isalarm", "isfired"}
    };
    public static String[][] columnTypes = {
            {"INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER"}
    };
    public static String[] tableNames = {"today_notification"};
    public static int[] columnNos = {7};
    toast maketext;

    public Today(Context context) {
        this.context = context;
        maketext = new toast(context);

    }


    public static Uri insert(Context context, int tableNo, int[] passInt) {

        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[tableNo]);
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < passInt.length; i++)
            contentValues.put(columnNames[0][i + 1], passInt[i]);

        return context.getContentResolver().insert(uri, contentValues);
    }

    public static Cursor select(Context context, int tableNo, String selection,
                                String[] selectionArgs, String[] projection) {
        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, Today.tableNames[tableNo]);
        return context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
    }

    /**
     * for updating all column of req table
     *
     * @param context
     * @param tableNo       the req table no
     * @param passInt       the new values to be entered in db
     * @param selection     the selection string
     * @param selectionArgs the selection arguments for selection string
     * @return
     */
    public static int update(Context context, int tableNo, int[] passInt,
                             String selection, String[] selectionArgs) {

        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[tableNo]);
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < passInt.length; i++)
            contentValues.put(columnNames[0][i + 1], passInt[i]);

        return context.getContentResolver().update(uri, contentValues, selection, selectionArgs);
    }

    /**
     * for updating only certain columns from required table
     *
     * @param context
     * @param tableNo       the req table
     * @param reqCol        the req column no to be updated
     * @param passInt       the new values to be entered to db
     * @param selection     the selection of rows which is to be updated
     * @param selectionArgs the selection args
     * @return
     */
    public static int update(Context context, int tableNo, int[] reqCol,
                             int[] passInt, String selection, String[] selectionArgs) {

        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[tableNo]);
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < passInt.length; i++)
            contentValues.put(columnNames[0][reqCol[i]], passInt[i]);

        return context.getContentResolver().update(uri, contentValues, selection, selectionArgs);
    }

    /**
     * to delete all rows in req table - used in dailysync service before inserting today notifications
     *
     * @param context
     * @param tableNo required table
     */
    public static void deleteAll(Context context, int tableNo) {
        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[tableNo]);
        context.getContentResolver().delete(uri, null, null);
    }


}


