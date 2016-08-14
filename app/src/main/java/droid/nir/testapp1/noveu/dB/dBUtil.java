package droid.nir.testapp1.noveu.dB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import droid.nir.testapp1.noveu.dB.metaValues.dBmetaData;

/**
 * Created by droidcafe on 8/15/2016.
 */
public class dBUtil {

    public static int getLargestColumn(Context context, int tableNo, int columnNo) {
        String selection = "SELECT MAX(" +
                dBmetaData.columnNames[tableNo][columnNo] +") as maximum FROM "+dBmetaData.tableNames[tableNo];

        ParentDb parentDb = ParentDb.getInstance(context);
        SQLiteDatabase db = parentDb.returnSQl();

        Cursor cursor = parentDb.rawQuery(db, selection,null);
        if (cursor== null || cursor.getCount() == 0) {
            return -1;
        }
        while (cursor.moveToNext()) {

        }
    }

}
