package droid.nir.testapp1.noveu.dB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import droid.nir.testapp1.noveu.dB.metaValues.dBmetaData;

/**
 * Created by droidcafe on 8/15/2016.
 */
public class dBUtil {

    /**
     * helper function for returning the largest value in a particular column from a table
     *
     * @param context
     * @param tableNo  - the table no containing the column - based on {@link dBmetaData#tableNames}
     * @param columnNo - the column no of the required column in {@link dBmetaData#columnNames}
     * @return - the largest value in the column
     */
    public static int getLargestColumn(Context context, int tableNo, int columnNo) {
        String selection = "SELECT MAX(" +
                dBmetaData.columnNames[tableNo][columnNo] + ") as maximum FROM " + dBmetaData.tableNames[tableNo];

        ParentDb parentDb = ParentDb.getInstance(context);
        SQLiteDatabase db = parentDb.returnSQl();

        Cursor cursor = parentDb.rawQuery(db, selection, null);
        if (cursor == null || cursor.getCount() == 0) {
            return -1;
        }
        while (cursor.moveToNext()) {
            return cursor.getInt(cursor.getColumnIndex(dBmetaData.columnNames[tableNo][columnNo]));
        }

        return -1;
    }

    /**
     * helper function for returning the whole row containing the largest value of a particular
     * column
     *
     * @param context
     * @param tableNo  - the table no containing the column - based on {@link dBmetaData#tableNames}
     * @param columnNo - the column no of the required column in {@link dBmetaData#columnNames}
     * @param limit    - the no of rows required. If only one row is required - limit 1
     * @return - the largest cursor pointing the whole row
     */
    public static Cursor getLargestColumn(Context context, int tableNo, int columnNo, int limit) {

        String selection = "SELECT * FROM " + dBmetaData.tableNames[tableNo] +
                " ORDER BY " + dBmetaData.columnNames[tableNo][columnNo] + " desc LIMIT " + limit;

        ParentDb parentDb = ParentDb.getInstance(context);
        SQLiteDatabase db = parentDb.returnSQl();

        Cursor cursor = parentDb.rawQuery(db, selection, null);
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        return cursor;
    }
}
