package droid.nir.alarmManager;

import android.app.AlarmManager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import droid.nir.testapp1.noveu.Util.Log;


import java.util.Calendar;


import droid.nir.databaseHelper.Events;
import droid.nir.databaseHelper.Pending;
import droid.nir.databaseHelper.Remainder;
import droid.nir.databaseHelper.Today;
import droid.nir.databaseHelper.Todolist;
import droid.nir.testapp1.MainActivity;
import droid.nir.testapp1.R;
import droid.nir.testapp1.toast;

/**
 * Created by user on 7/19/2015.
 */
public class CheckForToday extends Service {

    toast  toast;

    private CheckForUpdatesTask mTask;
    private Events events;
    private Pending pending;
    private SQLiteDatabase db;
    private Today today;
    private Remainder remainder;
    private Todolist todolist;
    NotificationManager mManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         super.onStartCommand(intent, flags, startId);
      //  Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        (mTask = new CheckForUpdatesTask()).execute(CheckForToday.this);
        return  START_REDELIVER_INTENT;
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("alarm schedule", "Starting service " + startId);
        toast = new toast(this);

        Context context = CheckForToday.this;
      //  (mTask = new CheckForUpdatesTask()).execute(CheckForToday.this);
    }


    public class Asyncschedule extends  AsyncTask<Context,Void,Void>
    {
        int choice;
        public  Asyncschedule(int x)
        {
            choice =x;
        }

        @Override
        protected Void doInBackground(Context... params) {

            switch (choice)
            {
                case 0:
                    schedulenow(params[0]);
                    break;
                case 1:
                    schedule(params[0]);
                    break;
            }


            return  null;
        }
    }

    public void schedulenow(Context context)
    {
        Intent alarmintent = new Intent(context, CheckForToday.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 84985, alarmintent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.d("alarm", "Service set for now");

    }

    public void schedule(Context context)
    {
        toast = new toast(context);

        //context.startService(new Intent(context, AlarmService.class));
        Intent alarmintent = new Intent(context, CheckForToday.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 8983455, alarmintent, PendingIntent.FLAG_UPDATE_CURRENT);


        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE,1);
        calendar.set(Calendar.SECOND, 0);

        //calendar.add(Calendar.SECOND, 5);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

     //   alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.d("alarm", "Service set ");
//        Toast.makeText(this, "Service Set", Toast.LENGTH_LONG).show();
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedprefs",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("isalarm", 8999);
        editor.commit();
       // Log.d("today","editor putstring "+datetimings);

//        toast.makeText("Alarm set at "+calendar.get(Calendar.HOUR_OF_DAY));
        /**/
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class CheckForUpdatesTask extends AsyncTask<Context,Integer,Void>{

        Context context;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        }

        @Override
        protected Void doInBackground(Context... params) {


            context = params[0];

            events = new Events(context);
            pending = new Pending(context);
            todolist = new Todolist(context);
            today = new Today(context);
            remainder = new Remainder(context);

            publishProgress(11222);
            Calendar tempcalendar22 = Calendar.getInstance();
            int month = tempcalendar22.get(Calendar.MONTH)+1;

            String dateofevent = tempcalendar22.get(Calendar.DAY_OF_MONTH)+"/"+month+"/"+tempcalendar22.get(Calendar.YEAR);

            Log.d("alarm schedule", "pre executing for "+dateofevent    );

            //uncommetnt here for notification
          //  setnotification(context, dateofevent);


            db = events.settingDatabase();
            pending.settingDatabase();
            todolist.settingDatabase();
            today.settingDatabase();
            remainder.settingDatabase();

            int notpresent =    1;

            int collno[] = {3};
           /* String ss = " type = 123 and oid = 11111 ";
            Cursor cc= today.select(db, 0, collno, ss, null, null, null, null);
            Log.d("checkfor today"," count of cc "+cc.getCount());
            while(cc.moveToNext())
            {
                String ddd= cc.getString(cc.getColumnIndex("title"));
                Log.d("today","first column is"+ddd);
                if(ddd.equals(dateofevent))
                {
                    notpresent =0;
                    Log.d("today","it is present. no deletion");
                }
                else {
                    notpresent=1;
                    Log.d("today","not present .deleting");
                }
            }*/

         //  if(notpresent==1)
            {
                Log.d("today", "not present .deleting");

                SharedPreferences sharedPreferences = getSharedPreferences("sharedprefs",0);
                SharedPreferences.Editor editor= sharedPreferences.edit();

                editor.putString("lastdate",dateofevent);
                editor.commit();
                today.deleterow(null, 0, null, db);
                today.deleterow(null,1,null,db);
                today.deleterow(null,2,null,db);
                today.deleterow(null,3,null,db);
                today.deleterow(null,4,null,db);
                today.deleterow(null,5,null,db);
              //  String[] tempstring ={dateofevent};
                //int tempint[] = {123,11111,0,0,0,0};
                //today.insert(tempstring,tempint);


                String selection = " date = ?";
                String selectionargs[] = {dateofevent};
                int[] columno = {0,1,4,3};

                Cursor cursor = events.select(db, 0, columno, selection, selectionargs, null, null, null);

                Log.d("checkfortoday"," inserting event items "+cursor.getCount());
                while(cursor.moveToNext())
                {

                    int notifyhr=0,notifymin=0,isalarm=0,fromhr=0,frommin=0,tohr=0,tomin=0,iswholeday=0,isnotes =0,noofdaysleft=0;
                    String fromdate="",todate="",location="";
                    int id = cursor.getInt(cursor.getColumnIndex("_id"));
                    int notify = cursor.getInt(cursor.getColumnIndex("notify"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    iswholeday = cursor.getInt(cursor.getColumnIndex("wholeday"));

                    String tempselection = " eid = ?";
                    String tempargs[] = {Integer.toString(id)};
                    if(iswholeday!=1)
                    {
                        int tempcolumno[] = {2,3,4,5,6,7};
                        Cursor tempcursor = events.select(db,1,tempcolumno,tempselection,tempargs,null,null,null);
                        while(tempcursor.moveToNext())
                        {
                            fromdate =tempcursor.getString(tempcursor.getColumnIndex("fromdate"));
                            todate  =tempcursor.getString(tempcursor.getColumnIndex("todate"));
                            fromhr = tempcursor.getInt(tempcursor.getColumnIndex("fromtimehr"));
                            frommin = tempcursor.getInt(tempcursor.getColumnIndex("fromtimemin"));
                            tohr  = tempcursor.getInt(tempcursor.getColumnIndex("totimehr"));
                            tomin = tempcursor.getInt(tempcursor.getColumnIndex("totimemin"));

                            if(!(fromdate.equals(todate)))
                            {
                                String tempss = " _id = "+id;
                                int tempcolmn[] = {2,5,8};

                                Cursor cc = events.select(db,0,tempcolumno,tempss,null,null,null,null);
                                Log.d("checkfortody","Getting extra info "+cc.getCount());
                                while (cc.moveToNext())
                                {
                                    location = cc.getString(cc.getColumnIndex("location"));
                                    isnotes = cc.getInt(cc.getColumnIndex("notes"));
                                    noofdaysleft =0;// cc.getInt(cc.getColumnIndex("noofdays"));


                                }
                            }
                        }

                    }

                    if(notify>0)
                    {


                        int tempcolumno[] = {2,3,4};
                        Cursor tempcursor = events.select(db,2,tempcolumno,tempselection,tempargs,null,null,null);

                        while(tempcursor.moveToNext())
                        {
                            isalarm = tempcursor.getInt(tempcursor.getColumnIndex("alarm"));
                            notifyhr = tempcursor.getInt(tempcursor.getColumnIndex("timehr"));
                            notifymin =tempcursor.getInt(tempcursor.getColumnIndex("timemin"));
                        }
                    }

                    int passint[] = {1,id,isalarm,notify,notifyhr,notifymin,iswholeday,fromhr,frommin,tohr,tomin,isnotes,noofdaysleft};
                    String passtring[] = {title,fromdate,todate,location};

                    today.insert(passtring,passint);


                }


                int[] columnno2 = {0,1,14,12,13,7,8};

                Cursor cursor1 = pending.select(db,0,columnno2,selection,selectionargs,null,null,null);

                Log.d("checkfortoday"," inserting pending items "+cursor1.getCount());
                while (cursor1.moveToNext())
                {

                    String statement = "";
                    String title = cursor1.getString(cursor1.getColumnIndex("title"));
                    int id = cursor1.getInt(cursor1.getColumnIndex("_id"));
                    int notif = cursor1.getInt(cursor1.getColumnIndex("notificationtype"));
                    int isnotify,isalarm;
                    if(notif==0||notif==2)
                    {
                        isnotify=1;
                    }
                    else {
                        isnotify =0;
                    }
                    if(notif==2||notif==1)
                    {
                        isalarm=1;
                    }
                    else {
                        isalarm=0;
                    }
                    int nothr =cursor1.getInt(cursor1.getColumnIndex("timehr"));
                    int notmin =cursor1.getInt(cursor1.getColumnIndex("timemin"));

                    String decisiontype = cursor1.getString(cursor1.getColumnIndex("decisiontype"));
                    if(decisiontype.equals("Custom"))
                    {
                        String tempselection = " pid = ?";
                        String tempargs[] = {Integer.toString(id)};
                        int customvalue = cursor1.getInt(cursor1.getColumnIndex("custom"));
                        if(customvalue==1)
                        {
                            int tempcolumn[] = {2};
                            Cursor tempcursor = pending.select(db,4,tempcolumn,tempselection,tempargs,null,null,null);

                            while (tempcursor.moveToNext())
                            {
                                String custom = tempcursor.getString(tempcursor.getColumnIndex("custom"));
                                statement = "Should I "+custom + " ?";

                            }
                        }
                        else
                        {
                            int tempcolumn[] = {2,5,6};
                            String teselection = " _id = ?";
                            Cursor tempcursor = pending.select(db,0,tempcolumn,teselection,tempargs,null,null,null);

                            while (tempcursor.moveToNext())
                            {
                                int desp = tempcursor.getInt(tempcursor.getColumnIndex("desp"));
                                if(desp!=0)
                                {
                                    int tempcolumnn[] = {2};
                                    Cursor tempcursorr = pending.select(db,1,tempcolumnn,tempselection,tempargs,null,null,null);

                                    while (tempcursorr.moveToNext())
                                    {
                                        String custom = tempcursorr.getString(tempcursorr.getColumnIndex("description"));
                                        statement = custom;

                                    }
                                }
                                else
                                {
                                    int nocons = tempcursor.getInt(tempcursor.getColumnIndex("nocons"));
                                    int nopros =tempcursor.getInt(tempcursor.getColumnIndex("nopros"));

                                    if((nopros==0)&&(nocons==0))
                                    {
                                        statement = "No pros and cons available";

                                    }
                                    else if(nopros>nocons)
                                    {
                                        statement = "Pros outnumber the cons!";

                                    }
                                    else if(nopros<nocons)
                                    {
                                        statement = "Sorry!Cons outnumber the pros";
                                    }
                                    else {
                                        statement = "Equal number of pros and cons!";
                                    }
                                }
                            }
                        }
                    }
                    else {
                        statement = "Should I "+decisiontype +" ?";
                    }

                    String[] passstr = {title,statement};
                    int[] passin = {0,id,isalarm,isnotify,nothr,notmin};

                    today.insert(passstr,passin);

                }

                int columnno3[] = {0,1,2,3};
                Cursor cursor2 = todolist.select(db,0,columnno3,selection,selectionargs,null,null,null);
                Log.d("checkfortoday"," inserting todolist items "+cursor2.getCount());
                while(cursor2.moveToNext())
                {
                    int notifhr =0 , notifmin =0;
                    String title = cursor2.getString(cursor2.getColumnIndex("title"));
                    int notif = cursor2.getInt(cursor2.getColumnIndex("notification"));
                    int id = cursor2.getInt(cursor2.getColumnIndex("_id"));
                    int listno = cursor2.getInt(cursor2.getColumnIndex("listno"));
                    if(notif==1) {
                        String tempselection = " tid = ?";
                        String tempargs[] = {Integer.toString(id)};
                        int tempcolumn[] = {2, 3};
                        Cursor tempcursor = todolist.select(db, 2, tempcolumn, tempselection, tempargs, null, null, null);

                        while (tempcursor.moveToNext()) {
                            notifhr = tempcursor.getInt(tempcursor.getColumnIndex("nhr"));
                            notifmin = tempcursor.getInt(tempcursor.getColumnIndex("nmin"));
                        }

                    }

                        String[] passtring = {title};
                        int passint[] = {2,id,0,notif,notifhr,notifmin,listno};

                        today.insert(passtring,passint);

                }


                Log.d("AlarmService", "count is " + cursor.getCount());

                int columno3[] = {0,1,2,3,4,6,7,8};

                Cursor cursor3 = remainder.select(db,0,columno3,selection,selectionargs,null,null,null);
                Log.d("checkfortoday","count of remainders is"+cursor3.getCount());
                while (cursor3.moveToNext())
                {
                    String passtring[] = new String[2];
                    int passint[] = new int[7];

                    passtring[0] = cursor3.getString(cursor3.getColumnIndex("title"));
                    passtring[1] = cursor3.getString(cursor3.getColumnIndex("desp"));
                    passint[1] = cursor3.getInt(cursor3.getColumnIndex("_id"));
                    passint[2] = cursor3.getInt(cursor3.getColumnIndex("isalarm"));
                    passint[3] = 1;
                    passint[4] = cursor3.getInt(cursor3.getColumnIndex("nothr"));
                    passint[5] = cursor3.getInt(cursor3.getColumnIndex("notmin"));
                    passint[6] = cursor3.getInt(cursor3.getColumnIndex("isdesp"));
                    passint[0] = 3;

                    today.insert(passtring,passint);

                }


            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

          //  toast.makeText("id is "+values[0]);
            super.onProgressUpdate(values);

          /*  NotificationManager alarmNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class), 0);
            NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                    context).setContentTitle("Alarm").setSmallIcon(R.mipmap.ic_launcher)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Alarm"))
                    .setContentText("haie from check here  "+values[0]);


            alamNotificationBuilder.setContentIntent(contentIntent);
            alarmNotificationManager.notify(1, alamNotificationBuilder.build());*/

            Log.d("AlarmService", "Notification sent.");

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("AlarmService", "finished sent.");
           // db.close();
            stopSelf();
        }
    }

    private void setnotification(Context context,String dateofevent) {


        NotificationManager alarmNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(context, MainActivity.class), 0);
        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                context).setContentTitle("Alarm").setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Alarm"))
                .setContentText("Setting data for "+dateofevent);


        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }
}
