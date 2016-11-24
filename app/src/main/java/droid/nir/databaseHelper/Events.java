package droid.nir.databaseHelper;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.Calendar;

import droid.nir.alarmManager.AlaramReceiver2;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.toast;

/**
 * Created by user on 7/14/2015.
 */
public class Events {


    DatabaseCreater edatabaseCreater;

    Context context;
    SQLiteDatabase db;
    String[][] columnNames, columnTypes;
    String[] tableNames;
    int[] columnNos;
    toast maketext;


    public Events(Context context) {
        this.context = context;
        //databaseCreater = Database.DatabaseCreater(context);

        edatabaseCreater = new DatabaseCreater(context);
        maketext = new toast(context);

        // maketext = new toast(context);

    }


    public SQLiteDatabase settingDatabase() {
        String[][] columnnames = {
                {"_id", "title", "location", "wholeday", "notify", "notes", "date", "done", "noofdays", "fromdate"},
                {"_id", "eid", "fromdate", "todate", "fromtimehr", "fromtimemin", "totimehr", "totimemin"},
                {"_id", "eid", "timehr", "timemin", "alarm"},
                {"_id", "eid", "note"}
        };

        String[][] columntypes = {
                {"INTEGER", "VARCHAR(100)", "VARCHAR(250)", "INTEGER", "INTEGER", "INTEGER", "VARCHAR(40)", "INTEGER", "INTEGER", "VARCHAR(40)"},
                {"INTEGER", "INTEGER", "VARCHAR(100)", "VARCHAR(100)", "INTEGER", "INTEGER", "INTEGER", "INTEGER"},
                {"INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER"},
                {"INTEGER", "INTEGER", "VARCHAR(1000)"}

        };

        String[] tablenames = {"event", "timeevent", "notify", "notes"};

        int[] columnnos = {10, 8, 5, 3};

        columnNames = columnnames;
        columnTypes = columntypes;
        columnNos = columnnos;
        tableNames = tablenames;

        edatabaseCreater.intialise(tableNames, columnNos, columnNames, columnTypes, 5);
        db = edatabaseCreater.getWritableDatabase();
        return db;
    }

    public int delete(SQLiteDatabase db, int tableno, String selection, String[] whereargs) {
        if (db != null)
            return edatabaseCreater.deleterow(db, tableNames[tableno], selection, whereargs);

        else
            return -1;
    }

    public Cursor select(SQLiteDatabase db, int tableno, int[] columnno, String selection, String[] selectionArgs, String groupby, String having, String orderby) {
        if (db != null) {
            String[] reqcolumns = new String[columnno.length];
            //  reqcolumns = null;
            for (int i = 0; i < columnno.length; i++) {
                reqcolumns[i] = columnNames[tableno][columnno[i]];
            }

            Cursor tempcursor = edatabaseCreater.select(db, tableNames[tableno], reqcolumns, selection, selectionArgs, groupby, having, orderby);
            return tempcursor;
        } else {
            return null;
        }


    }

    class Asyncinsert extends AsyncTask<Void, Void, Integer> {
        String[] values;
        int[] intvalues;
        SQLiteDatabase db;

        Asyncinsert(String[] values, int[] intvalues, SQLiteDatabase db) {
            this.values = values;
            this.intvalues = intvalues;
            this.db = db;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d("events", "done before" + intvalues[3]);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            int f[] = insertasync(values, intvalues, db);

            return f[0];
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            int x = (Integer) integer;
            if (x < 0) {
                maketext.makeText("Sorry!Event Creation Failed!Please Try Again ");

                Log.i("Events Insertion", "Insertion Failed");
            } else {
                maketext.makeText("Event Created Successfully");
                Log.i("Events Insertion", "Success at " + x);


            }

        }
    }

    public void setupalarm(int pendingintentid[], Calendar tempcalendar3) {
        try {
            Log.d("events", "alarm  " + pendingintentid[1] + " " + tempcalendar3.get(Calendar.HOUR_OF_DAY));
            if (pendingintentid[1] != 0) {
                Intent intent = new Intent(context, AlaramReceiver2.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context,
                        pendingintentid[0], intent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager am =
                        (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);

                //  .makeText(this, "" + tempcalendar3.get(Calendar.MINUTE) + " " + tempcalendar3.get(Calendar.SECOND), Toast.LENGTH_LONG).show();
                Log.d("events", " " + tempcalendar3.get(Calendar.MINUTE));
                am.set(AlarmManager.RTC_WAKEUP, tempcalendar3.getTimeInMillis(),
                        pendingIntent);
                Log.d("events", "Alarm Successfully Created");
            }


        } catch (Exception e) {
            Log.d("events alarm", "class not found  " + e);
        }

    }

    public void insert(String[] values, int[] intvalues, SQLiteDatabase db) {
        int f[] = insertasync(values, intvalues, db);
//        Asyncinsert asyncinsert = new Asyncinsert(values,intvalues,db);
//        asyncinsert.execute();
    }


    public int[] insertasync(String[] values, int[] intvalues, SQLiteDatabase db) {
        int todayinsert = 0;

        int setalarm = 0;
        Calendar tempcalendar2 = Calendar.getInstance();
        int month = tempcalendar2.get(Calendar.MONTH) + 1;

        String todaydate = tempcalendar2.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + tempcalendar2.get(Calendar.YEAR);

        boolean issamedate = todaydate.equals(values[2]);
        Log.d("events", "is same date " + issamedate);
        if (issamedate) {

            Log.d("events", "notify vlalue " + intvalues[1] + " alarm " + intvalues[11]);
            if (intvalues[1] != 0) {


                if (intvalues[11] != 0)
                    setalarm = 1;
                else
                    setalarm = 0;
            }

            todayinsert = 1;

        } else {
            intvalues[3] = 0;
        }

        Log.d("events", "done after" + intvalues[3]);
        //old code from here ..erase above
        ContentValues contentValues = new ContentValues();

        int j, k, l;
        j = 0;
        k = 0;
        for (int i = 1; i < 9; i++) {
            if (columnTypes[0][i].equals("INTEGER")) {
                Log.d("events", "name " + columnNames[0][i] + " j " + j + " i " + i);
                contentValues.put(columnNames[0][i], intvalues[j++]);

            } else {
                contentValues.put(columnNames[0][i], values[k++]);
                if (i == 6) {
                    Log.d("events", "date actually inserted" + values[k - 1] + " and j is " + k);
                }
            }


        }

        contentValues.put(columnNames[0][9], values[2]);


        Log.d("inserting events", "adding in events " + edatabaseCreater.insert(db, contentValues, tableNames[0]));

        String[] reqcolumnNames = {columnNames[0][0], columnNames[0][7]};
        String selection = "" + columnNames[0][2] + " = ? and " + columnNames[0][1] + " = ? ";
        String selectionArgs[] = {values[1], values[0]};
        Log.d("ev", " " + tableNames[0] + " " + reqcolumnNames[0] + " " + reqcolumnNames[1] + " " + " " + selection + " " + selectionArgs[0] + " " + selectionArgs[1]);
        Cursor tempcursor = edatabaseCreater.select(db, tableNames[0], reqcolumnNames, selection, selectionArgs, null, null, null);
        int pid = 0;
        while (tempcursor.moveToNext()) {
            pid = tempcursor.getInt(tempcursor.getColumnIndex(columnNames[0][0]));
            Log.d("Check for today", "done taken is " + tempcursor.getInt(tempcursor.getColumnIndex("done")) + " for id " + tempcursor.getInt(tempcursor.getColumnIndex("_id")));

        }


        if (todayinsert == 1) {

            String[] passstrings = {values[0], values[3], values[4], values[1]};
            int passvalues[] = {1, pid, intvalues[11], intvalues[1], intvalues[9], intvalues[10], intvalues[0], intvalues[5], intvalues[6], intvalues[7], intvalues[8], intvalues[2], intvalues[4]};

            Today today = new Today(context);

            today.settingDatabase();
            today.insert(passstrings, passvalues);
        }

        if (intvalues[0] == 0) {
            ContentValues tempcontentvalues = new ContentValues();
            tempcontentvalues.put(columnNames[1][1], pid);
            tempcontentvalues.put(columnNames[1][2], values[3]);
            tempcontentvalues.put(columnNames[1][3], values[4]);
            for (int f = 4; f < 8; f++) {
                tempcontentvalues.put(columnNames[1][f], intvalues[f + 1]);
            }
            Log.d("inserting events", "time table" + edatabaseCreater.insert(db, tempcontentvalues, tableNames[1]));


        }
        if (intvalues[1] > 0) {
            ContentValues tempcontentvalues = new ContentValues();
            tempcontentvalues.put(columnNames[2][1], pid);
            tempcontentvalues.put(columnNames[2][2], intvalues[9]);
            tempcontentvalues.put(columnNames[2][3], intvalues[10]);
            tempcontentvalues.put(columnNames[2][4], intvalues[11]);

            Log.d("events", "actually notify insert " + intvalues[9] + " " + intvalues[10]);

            Log.d("inserting events", "notify table " + edatabaseCreater.insert(db, tempcontentvalues, tableNames[2]));
        }
        if (intvalues[2] == 1) {
            ContentValues tempcontentvalues = new ContentValues();
            tempcontentvalues.put(columnNames[3][1], pid);
            tempcontentvalues.put(columnNames[3][2], values[5]);

            Log.d("inserting events", "notes table " + edatabaseCreater.insert(db, tempcontentvalues, tableNames[3]));
        }

        Log.d("events", " " + setalarm);
        int returnarr[] = {pid, setalarm};
        return returnarr;
    }


}
