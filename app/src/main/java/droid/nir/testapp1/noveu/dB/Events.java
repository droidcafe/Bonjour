package droid.nir.testapp1.noveu.dB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import droid.nir.databaseHelper.DatabaseCreater;

/**
 * Created by droidcafe on 11/16/2016.
 */

public class Events {

    public final static String[][] columnNames = {
            {"_id","title","location","wholeday","notify","notes","date","done","noofdays","fromdate"},
            {"_id","eid","fromdate","todate","fromtimehr","fromtimemin","totimehr","totimemin"},
            {"_id","eid","timehr","timemin","alarm"},
            {"_id","eid","note"}
    };

    public final static String[][] columnTypes ={
            {"INTEGER","VARCHAR(100)","VARCHAR(250)","INTEGER","INTEGER","INTEGER","VARCHAR(40)","INTEGER","INTEGER","VARCHAR(40)"},
            {"INTEGER","INTEGER","VARCHAR(100)","VARCHAR(100)","INTEGER","INTEGER","INTEGER","INTEGER"},
            {"INTEGER","INTEGER","INTEGER","INTEGER","INTEGER"},
            {"INTEGER","INTEGER","VARCHAR(1000)"}

    };

    public final static String[] tableNames ={"event","timeevent","notify","notes"};

    public final static int[] columnNos ={10,8,5,3};

    public static Cursor select(Context context, int tableno, int[] columnno,
                                String selection, String[] selectionArgs,
                                String groupby, String having, String orderby) {
        DatabaseCreater databaseCreater = DatabaseCreater.getInstance(context);
        SQLiteDatabase db = databaseCreater.returnSQl();


        if (db != null) {
            String[] reqcolumns = new String[columnno.length];
            for (int i = 0; i < columnno.length; i++) {
                reqcolumns[i] = columnNames[tableno][columnno[i]];
            }

            Cursor tempcursor = databaseCreater.select(db, tableNames[tableno],
                    reqcolumns, selection,
                    selectionArgs, groupby,
                    having, orderby);

            return tempcursor;
        } else {
            return null;
        }


    }



    public static Cursor select(Context context, boolean distinct, int tableno,
                                int[] columnno, String selection, String[] selectionArgs,
                                String groupby, String having, String orderby, String limit) {
        DatabaseCreater databaseCreater = DatabaseCreater.getInstance(context);
        SQLiteDatabase db = databaseCreater.returnSQl();


        if (db != null) {
            String[] reqcolumns = new String[columnno.length];
            for (int i = 0; i < columnno.length; i++) {
                reqcolumns[i] = columnNames[tableno][columnno[i]];
            }

            Cursor tempcursor = databaseCreater.select(db, distinct, tableNames[tableno],
                    reqcolumns, selection, selectionArgs, groupby, having, orderby, limit);
            return tempcursor;
        } else {
            return null;
        }


    }
}
