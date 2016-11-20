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
 * Created by user on 7/28/2015.
 */
public class Todolist {


    DatabaseCreater edatabaseCreater;

    Context context;
    SQLiteDatabase db;
    String[][] columnNames, columnTypes;
    String[] tableNames;
    int[] columnNos;
    toast maketext;

    public Todolist(Context context) {
        this.context = context;
        //databaseCreater = Database.DatabaseCreater(context);

        edatabaseCreater = new DatabaseCreater(context);
        maketext = new toast(context);


    }


    public SQLiteDatabase settingDatabase() {
        String[][] columnnames = {
                {"_id", "title", "notification", "listno", "date", "done"},
                {"_id", "tid", "listitem"},
                {"_id", "tid", "nhr", "nmin"}

        };

        String[][] columntypes = {
                {"INTEGER", "VARCHAR(100)", "INTEGER", "INTEGER", "VARCHAR(20)", "INTEGER"},
                {"INTEGER", "INTEGER", "VARCHAR(500)"},
                {"INTEGER", "INTEGER", "INTEGER", "INTEGER"}

        };

        String[] tablenames = {"todolist", "listitems", "tnotify"};

        int[] columnnos = {6, 3, 4};

        columnNames = columnnames;
        columnTypes = columntypes;
        columnNos = columnnos;
        tableNames = tablenames;

        edatabaseCreater.intialise(tableNames, columnNos, columnNames, columnTypes, 5);
        db = edatabaseCreater.getWritableDatabase();
        return db;
    }


    public Cursor select(SQLiteDatabase db, int tableno, int[] columnno, String selection, String[] selectionArgs, String groupby, String having, String orderby) {
        if (db != null) {

            String[] reqcolumns;
            if (columnno != null) {
                reqcolumns = new String[columnno.length];
                for (int i = 0; i < columnno.length; i++) {
                    reqcolumns[i] = columnNames[tableno][columnno[i]];
                }
            }else{
                reqcolumns = null;
            }

            Cursor tempcursor = edatabaseCreater.select(db, tableNames[tableno], reqcolumns, selection, selectionArgs, groupby, having, orderby);
            return tempcursor;
        } else {
            return null;
        }


    }

    public void update(SQLiteDatabase db,int oid, String[] values,int[] intvalues,List<String> prolist,Calendar calendar)
    {
        AsyncUpdate asyncUpdate = new AsyncUpdate(values,intvalues,db,calendar,prolist,oid);
        asyncUpdate.execute();

    }

    class  AsyncUpdate extends AsyncTask<Void,Void,Void>
    { String[] values;
        int[] intvalues;
        SQLiteDatabase db;
        Calendar fromcalendar;
        List<String> prolist;

        int oid;
        AsyncUpdate(String[] values, int[] intvalues, SQLiteDatabase db, Calendar fromcalendar, List<String> prolist,int oid) {
            this.values = values;
            this.intvalues = intvalues;
            this.db = db;
            this.fromcalendar = fromcalendar;
            this.oid = oid;
            this.prolist = prolist;
        }


        @Override
        protected Void doInBackground(Void... params) {

            int tempp = 0;
            String selection = "_id = "+oid;
            Calendar tempcalendar2 = Calendar.getInstance();
            int month =tempcalendar2.get(Calendar.MONTH) +1;
            String todaydate = tempcalendar2.get(Calendar.DAY_OF_MONTH) + "/" + month  + "/" + tempcalendar2.get(Calendar.YEAR);

            // boolean issamedate = DateUtils.isToday(fromcalendar.getTimeInMillis());
            boolean issamedate = todaydate.equals(values[1]);
            Log.d("todolist", "is same date " + issamedate);
            if (issamedate) {
                Log.d("todolist", "notify vlalue ");

                if (intvalues[0] == 1) {

                    //set notification here at
                    intvalues[2] = 1;
                }

                tempp = 1;
            }

            ContentValues contentValues = new ContentValues();

            int j = 0, k = 0;
            for (int i = 1; i < columnNos[0]; i++) {
                if (columnTypes[0][i].equals("INTEGER")) {
                    contentValues.put(columnNames[0][i], intvalues[j++]);
                } else {
                    contentValues.put(columnNames[0][i], values[k++]);
                }


            }
            edatabaseCreater.update(db,tableNames[0],contentValues,selection,null);

            if (tempp == 1) {
                Today today = new Today(context);
                today.settingDatabase();
                selection = "type = 2 and oid = "+oid;
                int col[] = {0};
                Cursor cc = today.select(db,0,col,selection,null,null,null,null);
                while (cc.moveToNext())
                {
                   int tid = cc.getInt(cc.getColumnIndex("_id"));
                    selection = "_id = "+tid;
                    today.deleterow(selection,0,null,db);
                    selection ="tid = "+tid;
                    today.deleterow(selection,4,null,db);

                }

                String[] passstring = {values[0]};
                int[] intval = {2, oid, 0, intvalues[0], intvalues[3], intvalues[4], intvalues[1]};

                today.insert(passstring, intval);


            }
            selection = "tid = "+oid;
            edatabaseCreater.deleterow(db,tableNames[1],selection,null);
            if (intvalues[1] > 0) {
                int i = 0;
                while (i < prolist.size()) {
                    ContentValues tempcontentvalue = new ContentValues();
                    tempcontentvalue.put(columnNames[1][1], oid);
                    tempcontentvalue.put(columnNames[1][2], prolist.get(i));
                    Log.d("inserting todolist", "adding in listitems" + edatabaseCreater.insert(db, tempcontentvalue, tableNames[1]));
                    i++;
                }
            }

            edatabaseCreater.deleterow(db,tableNames[2],selection,null);
            if (intvalues[0] == 1) {
                ContentValues tempcontentvalue = new ContentValues();
                tempcontentvalue.put(columnNames[2][1], oid);
                tempcontentvalue.put(columnNames[2][2], intvalues[3]);
                tempcontentvalue.put(columnNames[2][3], intvalues[4]);
                Log.d("inserting todolist", "adding in listitems" + edatabaseCreater.insert(db, tempcontentvalue, tableNames[2]));
            }



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

    public void insert(String[] values, int[] intvalues, SQLiteDatabase db, List<String> prolist, Calendar calendar) {
        Asyncinsert asyncinsert = new Asyncinsert(values, intvalues, db, calendar, prolist);
        asyncinsert.execute();
    }

    class Asyncinsert extends AsyncTask<Void, Void, Integer> {
        String[] values;
        int[] intvalues;
        SQLiteDatabase db;
        Calendar fromcalendar;
        List<String> prolist;

        Asyncinsert(String[] values, int[] intvalues, SQLiteDatabase db, Calendar fromcalendar, List<String> prolist) {
            this.values = values;
            this.intvalues = intvalues;
            this.db = db;
            this.fromcalendar = fromcalendar;

            this.prolist = prolist;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int tempp = 0;
            Calendar tempcalendar2 = Calendar.getInstance();
           int month =tempcalendar2.get(Calendar.MONTH) +1;
            String todaydate = tempcalendar2.get(Calendar.DAY_OF_MONTH) + "/" + month  + "/" + tempcalendar2.get(Calendar.YEAR);

            // boolean issamedate = DateUtils.isToday(fromcalendar.getTimeInMillis());
            boolean issamedate = todaydate.equals(values[1]);
            Log.d("todolist", "is same date " + issamedate);
            if (issamedate) {
                Log.d("todolist", "notify vlalue ");

                if (intvalues[0] == 1) {

                    //set notification here at
                    intvalues[2] = 1;
                }

                tempp = 1;
            }

            ContentValues contentValues = new ContentValues();

            int j = 0, k = 0;
            for (int i = 1; i < columnNos[0]; i++) {
                if (columnTypes[0][i].equals("INTEGER")) {
                    contentValues.put(columnNames[0][i], intvalues[j++]);
                } else {
                    contentValues.put(columnNames[0][i], values[k++]);
                }


            }

            Log.d("inserting todolist", "adding in todolist" + edatabaseCreater.insert(db, contentValues, tableNames[0]));

            String[] reqcolumnNames = {columnNames[0][0]};
            String selection = "" + columnNames[0][1] + " = ? and " + columnNames[0][4] + " = ?  and " + columnNames[0][3] + " = ? and " + columnNames[0][2] + " = ?";
            String listn = Integer.toString(intvalues[1]);
            String notificationvalue = Integer.toString(intvalues[0]);
            String selectionArgs[] = {values[0], values[1], listn, notificationvalue};
            Cursor tempcursor = edatabaseCreater.select(db, tableNames[0], reqcolumnNames, selection, selectionArgs, null, null, null);
            int tid = 0;
            while (tempcursor.moveToNext()) {
                tid = tempcursor.getInt(tempcursor.getColumnIndex(columnNames[0][0]));
            }

            if (tempp == 1) {
                Today today = new Today(context);
                String[] passstring = {values[0]};
                int[] intval = {2, tid, 0, intvalues[0], intvalues[3], intvalues[4], intvalues[1]};
                today.settingDatabase();
                 today.insert(passstring, intval);


            }
            if (intvalues[1] > 0) {
                int i = 0;
                while (i < prolist.size()) {
                    ContentValues tempcontentvalue = new ContentValues();
                    tempcontentvalue.put(columnNames[1][1], tid);
                    tempcontentvalue.put(columnNames[1][2], prolist.get(i));
                    Log.d("inserting todolist", "adding in listitems" + edatabaseCreater.insert(db, tempcontentvalue, tableNames[1]));
                    i++;
                }
            }

            if (intvalues[0] == 1) {
                ContentValues tempcontentvalue = new ContentValues();
                tempcontentvalue.put(columnNames[2][1], tid);
                tempcontentvalue.put(columnNames[2][2], intvalues[3]);
                tempcontentvalue.put(columnNames[2][3], intvalues[4]);
                Log.d("inserting todolist", "adding in listitems" + edatabaseCreater.insert(db, tempcontentvalue, tableNames[2]));
            }

            return null;
        }
    }

}


