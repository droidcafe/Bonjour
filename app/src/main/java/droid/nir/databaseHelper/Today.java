package droid.nir.databaseHelper;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import droid.nir.testapp1.noveu.Util.Log;

import java.util.Calendar;

import droid.nir.alarmManager.AlaramReceiver2;
import droid.nir.alarmManager.NotificationCreater;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.TimeUtil;
import droid.nir.testapp1.noveu.alarm.AlarmUtils;
import droid.nir.testapp1.toast;
import droid.nir.testapp1.timecorrection;

/**
 * Created by user on 7/22/2015.
 */
public class Today {


    DatabaseCreater tdatabaseCreater;

    Context context;
    SQLiteDatabase db;
    String[][] columnNames, columnTypes;
    String[] tableNames;
    int[] columnNos;
    toast maketext;
    timecorrection timecorrection;


    public Today(Context context) {
        this.context = context;
        maketext = new toast(context);
        tdatabaseCreater = new DatabaseCreater(context);
        timecorrection = new timecorrection();
    }

    public SQLiteDatabase settingDatabase() {
        String[][] columnNames = {
                {"_id","type","oid","title","isalarm","isnotify","notifyhr","notifymin"},
                {"_id","tid","statement"},
                {"_id","tid","iswholeday"},
                {"_id","tid","fromdate","fromtimehr","fromtimemin","todate","totimehr","totimemin"},
                {"_id","tid","noofitems"},
                {"_id","tid","isdesp","desp"}//add extra column whethter there is notification or not
        };
        String[][] columnTypes = {
                {"INTEGER","INTEGER","INTEGER","VARCHAR(200)","INTEGER","INTEGER","INTEGER","INTEGER"},
                {"INTEGER","INTEGER","VARCHAR(500)"},
                {"INTEGER","INTEGER","INTEGER"},
                {"INTEGER","INTEGER","VARCHAR(50)","INTEGER","INTEGER","VARCHAR(50)","INTEGER","INTEGER"},
                {"INTEGER","INTEGER","INTEGER"},
                {"INTEGER","INTEGER","INTEGER","VARCHAR(250)"}
        };
        String[] tableNames = {"today","todaypending","todayevent1","todayevent2","todaylist","todayremainder"};
        int[] columnNos = {8,3,3,8,3,4};

        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        this.tableNames = tableNames;
        this.columnNos = columnNos;

        db = tdatabaseCreater.getWritableDatabase();

        return db;
    }

    public Cursor select(SQLiteDatabase db, int tableno, int[] columnno, String selection, String[] selectionArgs, String groupby, String having, String orderby) {
        if (db != null) {
            String[] reqcolumns = new String[columnno.length];
            //  reqcolumns = null;
            for (int i = 0; i < columnno.length; i++) {
                reqcolumns[i] = columnNames[tableno][columnno[i]];
            }

            Cursor tempcursor = tdatabaseCreater.select(db, tableNames[tableno], reqcolumns, selection, selectionArgs, groupby, having, orderby);
            return tempcursor;
        } else {
            return null;
        }


    }


    public void insert(String[] stringvalues,int[] values) {
        Asyncinsert asyncinsert = new Asyncinsert(stringvalues,values);
        asyncinsert.execute();
    }

    public class Asyncinsert extends AsyncTask<Void, String, Void> {
        int[] values;
        String[] stringvalues;
        ContentValues contentValues;

        Asyncinsert(String[] stringvalues,int values[]) {
            this.values = values;
            this.stringvalues = stringvalues;

        }

        @Override
        protected Void doInBackground(Void... params) {

            contentValues = new ContentValues();
            int i,j=0;


            Log.d("today", "second value " + values[5] + " " + values[5] / 100);
            for (i = 1; i < columnNos[0]; i++) {

                if(columnTypes[0][i].equals("INTEGER"))
                {
                    contentValues.put(columnNames[0][i], values[j++]);


                }
                else {
                    contentValues.put(columnNames[0][i],stringvalues[0]);
                }


            }

       //     contentValues.put(columnNames[0][i], notifytime);

            Log.d("today", "today getting inserted " + tdatabaseCreater.insert(db, contentValues, tableNames[0]));

            Log.d("today", "");

            String[] reqcolumnNames = {columnNames[0][0]};
            String datetimings;
            String selection = "" + columnNames[0][1] + " = ? and " + columnNames[0][2] + " = ?  and " + columnNames[0][3] + " = ?" ;
            String ff = Integer.toString(values[0]);
            String fff = Integer.toString(values[1]);
            String selectionArgs[] = {ff, fff, stringvalues[0]};
            Cursor tempcursor = tdatabaseCreater.select(db, tableNames[0], reqcolumnNames, selection, selectionArgs, null, null, null);
            int pid = -1;
            while (tempcursor.moveToNext()) {
                pid = tempcursor.getInt(tempcursor.getColumnIndex(columnNames[0][0]));
            }

            if(pid!=-1)
            {
                Log.d("today","got pid "+pid);
                switch (values[0])
                {
                    case 0:
                        Log.d("today","notify and alarm values are "+values[2] +" "+values[3]);
                        if(values[3]==1)
                        {
                            int isalarm =0;
                            if(values[2]==1)
                            {
                                isalarm=1;
                            }
                           datetimings =  setuppendingnotification(isalarm,values,stringvalues);
                            SharedPreferences sharedPreferences = context.getSharedPreferences("sharedprefs",0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("lastnoti",""+datetimings);
                            Log.d("today","editor putstring "+datetimings);
                            editor.apply();
                        }
                        insertpending(stringvalues,pid);
                        break;
                    case 1:


                        insertevent(values, stringvalues,pid);
                        break;
                    case 2:
                       datetimings=  insertlist(stringvalues[0],values,pid);
                        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedprefs",0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("lastnoti",""+datetimings);
                        Log.d("today","editor putstring "+datetimings);
                        editor.apply();
                        break;
                    case 3:
                        insertremainder(values, stringvalues,pid);
                        break;
                }
            }

            else
            {
                //publishProgress(pid);
            }

            return null;
        }






        private void setuppendingalarm() {

            Intent intent = new Intent(context, AlaramReceiver2.class);
            String pent = Integer.toString(values[1]);
            pent = "9".concat(pent);
            int iid = Integer.parseInt(pent);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,values[4]);
            calendar.set(Calendar.MINUTE,values[5]);
            calendar.set(Calendar.SECOND,0);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    iid, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            TimeUtil.setAlarm(context, pendingIntent, calendar);

            Log.d("today","Alarm Successfully Created for decision");


        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

           // if(values[0]==-1)
            //maketext.makeText("Sorry Insertion Unsuccessful!Please try again");
           maketext.makeText("the notification timings are " + values[0]);
        }
    }

    private void insertpending(String[] stringvalues,int pid) {
        Log.d("today","insertpendinng");

        ContentValues tempContentValues = new ContentValues();
        tempContentValues.put(columnNames[1][1], pid);
        tempContentValues.put(columnNames[1][2],stringvalues[1]);

        Log.d("today", "today pending " + tdatabaseCreater.insert(db, tempContentValues, tableNames[1]));


    }



    private void insertevent(int[] values, String[] stringvalues,int pid) {
        Log.d("today", "in eventinsert 1");

        ContentValues tempContentValues = new ContentValues();
        tempContentValues.put(columnNames[2][1], pid);
        tempContentValues.put(columnNames[2][2], values[6]);
        String statement = "";


        Log.d("today", "event today1 " + tdatabaseCreater.insert(db, tempContentValues, tableNames[2]));

        if(values[6]==0)
        {
            ContentValues tempContentValuess = new ContentValues();
            tempContentValuess.put(columnNames[3][1], pid);


            //Log.d("today", "orginal " + values[6] + " " + a + " " + s + "orginal " + values[8] + " " + values[8] / 100);

            tempContentValuess.put(columnNames[3][2], stringvalues[1]);
            tempContentValuess.put(columnNames[3][5],stringvalues[2]);
            tempContentValuess.put(columnNames[3][3],values[7]);
            tempContentValuess.put(columnNames[3][4],values[8]);
            tempContentValuess.put(columnNames[3][6],values[9]);
            tempContentValuess.put(columnNames[3][7],values[10]);


            Log.d("today", "event today2 " + tdatabaseCreater.insert(db, tempContentValuess, tableNames[3]));

            if(values[3]==1)
            {
                statement = context.getResources().getString(R.string.eten);

            }
            else if(values[3]==2)
            {
                statement = context.getResources().getString(R.string.ehalf);
            }
            else if(values[3] == 3) {
                String mm = timecorrection.formatime(values[7],values[8]);
                statement = context.getResources().getString(R.string.ecustom)+" " +mm;
            }

            if(!(stringvalues[1].equals(stringvalues[2])))
            {
                Calendar tempcalendar22 = Calendar.getInstance();
                int month = tempcalendar22.get(Calendar.MONTH) +1;
                String fromdateofevent = tempcalendar22.get(Calendar.DAY_OF_MONTH)+"/"+month+"/"+tempcalendar22.get(Calendar.YEAR);

                tempcalendar22.add(Calendar.DAY_OF_MONTH,1);
                String dateofevent = tempcalendar22.get(Calendar.DAY_OF_MONTH)+"/"+month+"/"+tempcalendar22.get(Calendar.YEAR);

                ContentValues contentValues = new ContentValues();
                contentValues.put("title",stringvalues[0]);
                contentValues.put("location",stringvalues[3]);
                contentValues.put("date",dateofevent);
                contentValues.put("fromdate",fromdateofevent);
                contentValues.put("wholeday",values[6]);
                contentValues.put("notify",values[3]);
                contentValues.put("notes",values[11]);
                contentValues.put("done", 0);
                contentValues.put("noofdays", values[12]);

                String selecttn = " _id  = "+values[1];
                Log.d("today","event updated "+tdatabaseCreater.update(db,"event",contentValues,selecttn,null));

            }

            else {
                Log.d("today","evetns of same days");
            }

        }

        else {
            statement =context.getResources().getString(R.string.ewholeday);
        }

        if(values[3]>0)
        {
            int isalarm =0;
            if(values[2]==1)
            {
                isalarm=1;
            }
            String datetimings =  setupeventnotification(isalarm,stringvalues[0],values,statement);
            SharedPreferences sharedPreferences = context.getSharedPreferences("sharedprefs",0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("lastnoti",""+datetimings);
            Log.d("today","editor putstring "+datetimings);
            editor.apply();
        }





    }

    private String setupeventnotification(int isalarm,String stringvalues, int[] values,String statement) {
        Log.d("today", "setting notification of event");


        Intent callintent = new Intent(context, NotificationCreater.class);
        callintent.putExtra("title",""+stringvalues);
        String oid = Integer.toString(values[1]);
        callintent.putExtra("oid",""+oid);
        callintent.putExtra("text", "" + statement);
        String type = Integer.toString(1);
        callintent.putExtra("type", type);
        String alarm = Integer.toString(isalarm);
        callintent.putExtra("isalarm",alarm);
        callintent.setAction("nir.droid.NEWNOTIFICATION");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,values[4]);
        calendar.set(Calendar.MINUTE,values[5]);
        calendar.set(Calendar.SECOND,0);
        String pent = Integer.toString(values[1]);
        pent = "8888".concat(pent);
        int iid = Integer.parseInt(pent);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, iid, callintent, PendingIntent.FLAG_UPDATE_CURRENT);

        String datetimings ="of type event "+ calendar.get(Calendar.DAY_OF_MONTH) +" /"+calendar.get(Calendar.MONTH) +" /"+calendar.get(Calendar.YEAR) +" /"+calendar.get(Calendar.HOUR_OF_DAY) +" /"+calendar.get(Calendar.MINUTE) +" /"+calendar.get(Calendar.SECOND)  +" isalarm "+ 1;

        //  .makeText(this, "" + tempcalendar3.get(Calendar.MINUTE) + " " + tempcalendar3.get(Calendar.SECOND), Toast.LENGTH_LONG).show();
        Log.d("today", " " + calendar.get(Calendar.MINUTE));
        Log.d("today", " " + datetimings);
        TimeUtil.setAlarm(context, pendingIntent, calendar);

        Log.d("today", "Alarm of decision Successfully Created of id "+values[1]);

        return  datetimings;
    }



    private String insertlist(String title, int[] values,int pid) {
        Log.d("today", "insertlist");


        ContentValues tempContentValuess = new ContentValues();
        tempContentValuess.put(columnNames[4][1], pid);
        tempContentValuess.put(columnNames[4][2], values[6]);

        String datetimings = "";
        if(values[3]!=0)
        {
            int[] passint = {pid,values[1],values[4],values[5],values[6]};
           datetimings =  setuplistnotification(title,passint);
        }

        Log.d("today", "today todolist insert" + tdatabaseCreater.insert(db, tempContentValuess, tableNames[4]));

        return  datetimings;

    }

    private String setuplistnotification(String title, int[] passint) {

        String statement = "There are "+passint[4]+" items in the list";

        Intent callintent = new Intent(context, NotificationCreater.class);
        callintent.putExtra("title", "" + title);
        String oid = Integer.toString(passint[1]);
        callintent.putExtra("oid",""+oid);
        callintent.putExtra("text", "" + statement);
        String type = Integer.toString(2);
        callintent.putExtra("type",type);
        callintent.setAction("nir.droid.NEWNOTIFICATION");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,passint[2]);
        calendar.set(Calendar.MINUTE,passint[3]);
        calendar.set(Calendar.SECOND,0);

        String pent = Integer.toString(passint[1]);
        pent = "333".concat(pent);
        int iid = Integer.parseInt(pent);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, iid, callintent, PendingIntent.FLAG_UPDATE_CURRENT);

        String datetimings ="of type list "+calendar.get(Calendar.DAY_OF_MONTH) +" /"+calendar.get(Calendar.MONTH) +" /"+calendar.get(Calendar.YEAR) +" /"+calendar.get(Calendar.HOUR_OF_DAY) +" /"+calendar.get(Calendar.MINUTE) +" /"+calendar.get(Calendar.SECOND);

        Log.d("today", " " + calendar.get(Calendar.MINUTE));
        Log.d("today", " " + calendar.get(Calendar.DAY_OF_MONTH) + " /" + calendar.get(Calendar.MONTH) + " /" + calendar.get(Calendar.YEAR) + " /" + calendar.get(Calendar.HOUR_OF_DAY) + " /" + calendar.get(Calendar.MINUTE) + " /" + calendar.get(Calendar.SECOND));

        TimeUtil.setAlarm(context, pendingIntent, calendar);

        Log.d("today", "Alarm of list Successfully Created of id "+passint[1]);

        return datetimings;
    }

    private void  insertremainder(int[] values, String[] stringvalues,int pid) {


        ContentValues tempContentValuess = new ContentValues();
        tempContentValuess.put(columnNames[5][1], pid);
        tempContentValuess.put(columnNames[5][2], values[6]);
        tempContentValuess.put(columnNames[5][3],stringvalues[1]);

        Log.d("today", "remainder table1 " + tdatabaseCreater.insert(db, tempContentValuess, tableNames[5]));

        String datetimings = "";

        if(values[6]==0)
        {
            stringvalues[1] = context.getResources().getString(R.string.nodesp);
        }
            int[] passint = {pid,values[1],values[4],values[5],values[2]};
            datetimings =  setupremaindernotification(stringvalues, passint);
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedprefs",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastnoti",""+datetimings);
        editor.apply();
        Log.d("today","insertremainder "+datetimings);
    }

    private String setupremaindernotification(String[] stringvalues, int[] values) {

        Intent callintent = new Intent(context, NotificationCreater.class);
        callintent.putExtra("title",""+stringvalues[0]);
        String oid = Integer.toString(values[1]);
        callintent.putExtra("oid",""+oid);
        callintent.putExtra("text", "" + stringvalues[1]);
        String type = Integer.toString(3);
        callintent.putExtra("type", type);
        String alarm = Integer.toString(values[4]);
        callintent.putExtra("isalarm",alarm);
        callintent.setAction("nir.droid.NEWNOTIFICATION");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,values[2]);
        calendar.set(Calendar.MINUTE,values[3]);
        calendar.set(Calendar.SECOND,0);
        String pent = Integer.toString(values[1]);
        pent = "7777".concat(pent);
        int iid = Integer.parseInt(pent);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, iid, callintent, PendingIntent.FLAG_UPDATE_CURRENT);
        String datetimings = "of type remainder "+calendar.get(Calendar.DAY_OF_MONTH) +" /"+calendar.get(Calendar.MONTH) +" /"+calendar.get(Calendar.YEAR) +" /"+calendar.get(Calendar.HOUR_OF_DAY) +" /"+calendar.get(Calendar.MINUTE) +" /"+calendar.get(Calendar.SECOND)  +" isalarm "+ 1;

        //  .makeText(this, "" + tempcalendar3.get(Calendar.MINUTE) + " " + tempcalendar3.get(Calendar.SECOND), Toast.LENGTH_LONG).show();
        Log.d("today", " " + calendar.get(Calendar.MINUTE));
        Log.d("today", " " + datetimings);
        TimeUtil.setAlarm(context, pendingIntent, calendar);

        Log.d("today", "Alarm of decision Successfully Created of id "+values[1]);

        return  datetimings;

    }


    public void deleterow(String valuess , int intvalues, String[] whereArgs, SQLiteDatabase db) {

        String[] values = {tableNames[intvalues],valuess };
        Asyncdeleterow asyncdeleterow = new Asyncdeleterow(values,whereArgs,db);
        asyncdeleterow.execute();

    }

    public class Asyncdeleterow extends AsyncTask<Void, Void, Void>
    {
        SQLiteDatabase db;
        String[] values , whereArgs;
        Asyncdeleterow(String[] values,String[] whereArgs,SQLiteDatabase db)
        {
            this.values = values;
            this.whereArgs = whereArgs;
            this.db = db;
        }
        @Override
        protected Void doInBackground(Void... params) {

            int y = tdatabaseCreater.deleterow(db,values[0],values[1],whereArgs);
            Log.d("today","deleted no is "+y);
            return null;
        }
    }

    public String setuppendingnotification(int isalarm,int[] values,String[] stringvalues) {

        Log.d("today", "setting notification of pending");
        Intent callintent = new Intent(context, NotificationCreater.class);
        callintent.putExtra("title", "" + stringvalues[0]);
        String oid = Integer.toString(values[1]);
        callintent.putExtra("oid",""+oid);
        callintent.putExtra("text", "" + stringvalues[1]);
        String type = Integer.toString(0);
        callintent.putExtra("type", type);
        String alarm = Integer.toString(isalarm);
        callintent.putExtra("isalarm",alarm);
        callintent.setAction("nir.droid.NEWNOTIFICATION");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,values[4]);
        calendar.set(Calendar.MINUTE, values[5]);
        calendar.set(Calendar.SECOND, 0);
        String pent = Integer.toString(values[1]);
        pent = "111".concat(pent);
        int iid = Integer.parseInt(pent);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, iid, callintent, PendingIntent.FLAG_UPDATE_CURRENT);

        String datetimings ="of type pending "+ calendar.get(Calendar.DAY_OF_MONTH) +" /"+calendar.get(Calendar.MONTH) +" /"+calendar.get(Calendar.YEAR) +" /"+calendar.get(Calendar.HOUR_OF_DAY) +" /"+calendar.get(Calendar.MINUTE) +" /"+calendar.get(Calendar.SECOND)  +" isalarm "+ 1;

        //  .makeText(this, "" + tempcalendar3.get(Calendar.MINUTE) + " " + tempcalendar3.get(Calendar.SECOND), Toast.LENGTH_LONG).show();
        Log.d("today", " " + calendar.get(Calendar.MINUTE));
        Log.d("today", " " + datetimings);
        TimeUtil.setAlarm(context, pendingIntent, calendar);

        Log.d("today", "Alarm of decision Successfully Created of id "+values[1]);

        return  datetimings;
    }
}
