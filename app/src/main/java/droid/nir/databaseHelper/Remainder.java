package droid.nir.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import droid.nir.testapp1.noveu.Util.Log;

import java.util.Calendar;
import java.util.List;

import droid.nir.testapp1.toast;

/**
 * Created by user on 8/27/2015.
 */
public class Remainder {

    DatabaseCreater edatabaseCreater;

    Context context;
    SQLiteDatabase db;
    String[][] columnNames, columnTypes;
    String[] tableNames;
    int[] columnNos;
    toast maketext;

    public  Remainder(Context context)
    {
        this.context =context;
        maketext = new toast(context);

        edatabaseCreater = new DatabaseCreater(context);
    }

    public SQLiteDatabase settingDatabase() {
        String[][] columnnames = {
                {"_id", "title", "isdesp", "desp", "date", "done","nothr","notmin","isalarm"}


        };

        String[][] columntypes = {
                {"INTEGER", "VARCHAR(100)", "INTEGER",  "VARCHAR(100)", "VARCHAR(100)","INTEGER" ,"INTEGER", "INTEGER", "INTEGER"}


        };

        String[] tablenames = {"remainder"};

        int[] columnnos = {9};

        columnNames = columnnames;
        columnTypes = columntypes;
        columnNos = columnnos;
        tableNames = tablenames;

        edatabaseCreater.intialise(tableNames, columnNos, columnNames, columnTypes, 5);
        db = edatabaseCreater.getWritableDatabase();
        return db;
    }

    public void update(String[] values, int intvalues[], SQLiteDatabase db,int oid)
    {

        Asyncupdate asyncupdate = new Asyncupdate(values, intvalues, db, oid);
        asyncupdate.execute();
    }

    public class  Asyncupdate extends AsyncTask<Void,Void,Void>
    {

        String[] values;
        int[] intvalues;
        SQLiteDatabase db;
        int oid;
        public Asyncupdate(String[] values, int intvalues[], SQLiteDatabase db,int oid) {

            this.values = values;
            this.intvalues = new int[5];
            this.intvalues[4] = intvalues[2];
            this.intvalues[2] = intvalues[0];
            this.intvalues[3] = intvalues[1];
            this.db = db;
            this.oid = oid;

        }

        @Override
        protected Void doInBackground(Void... params) {

            int tempp = 0;
            Calendar tempcalendar2 = Calendar.getInstance();
            String selection = "_id = "+oid;
            int month =tempcalendar2.get(Calendar.MONTH) +1;
            String todaydate = tempcalendar2.get(Calendar.DAY_OF_MONTH) + "/" +month  + "/" + tempcalendar2.get(Calendar.YEAR);
            boolean issamedate = todaydate.equals(values[2]);
            Log.d("todolist", "is same date " + issamedate);
            if (issamedate) {
                Log.d("todolist", "notify vlalue ");
                tempp = 1;
            }

            if(!(values[1].equals("")))
            {
                intvalues[0] =1;
            }
            else {
                intvalues[0] =0;
            }
            intvalues[1] =0;

            ContentValues contentValues = new ContentValues();

            int j = 0, k = 0;
            for(int i=1;i<9;i++)
            {
                if(columnTypes[0][i].equals("INTEGER"))
                {
                    Log.d("remainder","name "+ columnNames[0][i]+" j "+j + " i "+i);
                    contentValues.put(columnNames[0][i],intvalues[j++]);

                }
                else
                {
                    contentValues.put(columnNames[0][i],values[k++]);

                }
            }
            long x =  edatabaseCreater.update(db, tableNames[0],contentValues,selection,null);

            Log.d("inserting remainder","adding in remainder " + x);
            if(tempp==1) {
                Today today = new Today(context);
                today.settingDatabase();
                selection = "type = 3 and oid = " + oid;
                int col[] = {0};
                Cursor cc = today.select(db, 0, col, selection, null, null, null, null);
                while (cc.moveToNext()) {
                    int tid = cc.getInt(cc.getColumnIndex("_id"));
                    selection = "_id = " + tid;
                    today.deleterow(selection, 0, null, db);
                    selection = "tid = " + tid;
                    today.deleterow(selection, 5, null, db);

                }

                int todint[] = {3,oid,intvalues[4],1,intvalues[2],intvalues[3],intvalues[0]};
                String todstr[] = {values[0],values[1]};

                today.settingDatabase();
                today.insert(todstr,todint);

            }

            return null;

            }

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

    public int delete(SQLiteDatabase db, int tableno,String selection ,String[] whereargs)
    {
        if(db!=null)
            return edatabaseCreater.deleterow(db,tableNames[tableno],selection,whereargs);

        else
            return -1;
    }
    public void insert(String[] values, int intvalues[], SQLiteDatabase db) {
        Asyncinsert asyncinsert = new Asyncinsert(values, intvalues, db);
        asyncinsert.execute();
    }

    class  Asyncinsert extends AsyncTask<Void,Void,Long>
    {

        String[] values;
        int[] intvalues;
        SQLiteDatabase db;
        public Asyncinsert(String[] values, int intvalues[], SQLiteDatabase db) {

            this.values = values;
            this.intvalues = new int[5];
            this.intvalues[4] = intvalues[2];
            this.intvalues[2] = intvalues[0];
            this.intvalues[3] = intvalues[1];
            this.db = db;

        }

        @Override
        protected Long doInBackground(Void... params) {

            int tempp = 0;
            Calendar tempcalendar2 = Calendar.getInstance();
            int month =tempcalendar2.get(Calendar.MONTH) +1;
            String todaydate = tempcalendar2.get(Calendar.DAY_OF_MONTH) + "/" +month  + "/" + tempcalendar2.get(Calendar.YEAR);
            boolean issamedate = todaydate.equals(values[2]);
            Log.d("todolist", "is same date " + issamedate);
            if (issamedate) {
                Log.d("todolist", "notify vlalue ");
                tempp = 1;
            }
            
            if(!(values[1].equals("")))
            {
                intvalues[0] =1;
            }
            else {
                intvalues[0] =0;
            }
            intvalues[1] =0;
            
            ContentValues contentValues = new ContentValues();

            int j = 0, k = 0;
            for(int i=1;i<9;i++)
            {
                if(columnTypes[0][i].equals("INTEGER"))
                {
                    Log.d("remainder","name "+ columnNames[0][i]+" j "+j + " i "+i);
                    contentValues.put(columnNames[0][i],intvalues[j++]);

                }
                else
                {
                    contentValues.put(columnNames[0][i],values[k++]);
                   
                }
            }
            long x =  edatabaseCreater.insert(db, contentValues, tableNames[0]);

            Log.d("inserting remainder","adding in remainder " + x);
            if(tempp==1)
            {
                 Today today = new Today(context);
                String[] reqcolumnNames = {columnNames[0][0]};
                String selection = "" + columnNames[0][1] + " = ? and " + columnNames[0][4] + " = ?  and " + columnNames[0][2] + " = ? and " + columnNames[0][6] + " = ? and " + columnNames[0][7] + " = ? and " + columnNames[0][8] + " = ?";
                String listn = Integer.toString(intvalues[0]);
                String notificationvalue = Integer.toString(intvalues[2]);
                String selectionArgs[] = {values[0], values[2], listn, notificationvalue,Integer.toString(intvalues[3]),Integer.toString(intvalues[4])};
                Cursor tempcursor = edatabaseCreater.select(db, tableNames[0], reqcolumnNames, selection, selectionArgs, null, null, null);
                Log.d("remainder", "the count is "+tempcursor.getCount());
                int rid = 0;
                while (tempcursor.moveToNext()) {
                    rid = tempcursor.getInt(tempcursor.getColumnIndex(columnNames[0][0]));
                    Log.d("oid","inserted  is "+rid);
                }

                int todint[] = {3,rid,intvalues[4],1,intvalues[2],intvalues[3],intvalues[0]};
                String todstr[] = {values[0],values[1]};

                today.settingDatabase();
                today.insert(todstr,todint);

            }
            return x;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            if(aLong>0)
            {
                maketext.makeText("Insertion Successful");
            }
        }
    }


}
