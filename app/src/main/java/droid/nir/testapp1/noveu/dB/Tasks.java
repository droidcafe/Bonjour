package droid.nir.testapp1.noveu.dB;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import droid.nir.testapp1.noveu.Util.AutoRefresh;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.constants.IntentActions;
import droid.nir.testapp1.noveu.sync.receivers.DbChangeReceiver;
import droid.nir.testapp1.toast;

/**
 * Created by droidcafe on 2/25/2016.
 */
public class    Tasks {

    Context context;
    public static String[][] columnNames = {
                    // 0     1     2      3      4       5          6         7
           /* 0 */ {"_id","name","pid","date","isrem","isnotes","issubtask","done"},
            {"_id","tid","date","istime"},
             /* 2 */{"_id","rid","timehr","timemin","isalarm","isrepeat"},
            {"_id","aid","mode"},
             /* 4 */{"_id","tid","notes"},
            {"_id","tid","subtask","subtaskorder","done"}
    };
    public static String[][] columnTypes = {
            {"INTEGER","VARCHAR(350)","INTEGER","VARCHAR(30)","INTEGER","INTEGER","INTEGER","INTEGER"},
            {"INTEGER","INTEGER","VARCHAR(30)","INTEGER"},
            {"INTEGER","INTEGER","INTEGER","INTEGER","INTEGER","INTEGER"},
            {"INTEGER","INTEGER","INTEGER"},
            {"INTEGER","INTEGER","VARCHAR(100)"},
            {"INTEGER","INTEGER","VARCHAR(100)","INTEGER","INTEGER"}

    };
    public static String[] tableNames = {"tasks","reminder","alarm","repeat","notes","subtasks"};
    public static int[] columnNos = {8,4,6,3,3,5};
    toast maketext;

    public Tasks(Context context)
    {
        this.context =context;
        maketext = new toast(context);

    }



    /**
     * string = name , date, remdate , note
     * 0       1        2      3        4
     * <p></p>
     * passint = projectid, isrem, isnotes, issubtask, done, istime, timehr, timemin, isalarm, isrepeat, repeatmode
     *              0            1     2        3         4      5      6        7       8        9           10
     */

    public static void insert(String[] passDatas, int[] passInt,
                              Context context, List<String> subtasklist,
                              List<Integer> subtaskdone){


        //DBProvider dbProvider =new DBProvider();
       String task = passDatas[0];
        ContentValues contentTask =new ContentValues();
        int int_index=0,string_index =0;

        Project.updateProject(context, passInt[0]);




        for(int i=1;i<columnNos[0];i++)
        {
            if(columnTypes[0][i].equals("INTEGER"))
                contentTask.put(columnNames[0][i],passInt[int_index++]);
            else
                contentTask.put(columnNames[0][i],passDatas[string_index++]);
        }

        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[0]);
        Uri insertedUri = context.getContentResolver().insert(uri, contentTask);
        int tid = Integer.parseInt(insertedUri.getLastPathSegment());
        Log.d("tasks","inserted is "+insertedUri.toString());

        /**
         * Remainder is present
         */
        if(passInt[1]==1)
        {
            //reminder is present
            ContentValues contentReminder = new ContentValues(3);
            contentReminder.put(columnNames[1][1],tid);
            contentReminder.put(columnNames[1][2],passDatas[string_index++]);
            contentReminder.put(columnNames[1][3],passInt[int_index++]);

            uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[1]);
            insertedUri = context.getContentResolver().insert(uri, contentReminder);

            int rid= Integer.parseInt(insertedUri.getLastPathSegment());

            if(passInt[5]==1)
            {
                ContentValues contentAlarm = new ContentValues(5);
                contentAlarm.put(columnNames[2][1],rid);
                int i=2;
                while (i<columnNos[2])
                    contentAlarm.put(columnNames[2][i++],passInt[int_index++]);

                uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[2]);
                insertedUri = context.getContentResolver().insert(uri, contentAlarm);

                if(passInt[9]==1)
                {
                    int aid = Integer.parseInt(insertedUri.getLastPathSegment());
                    ContentValues contentRepeat = new ContentValues(3);
                    contentRepeat.put(columnNames[3][1],aid);
                    contentRepeat.put(columnNames[3][2],passInt[10]);

                    uri =  Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[3]);
                    context.getContentResolver().insert(uri,contentRepeat);

                }
            }
        }
        /**
         * notes is present
         *
         */
        if(passInt[2]==1)
        {
            ContentValues contentNotes = new ContentValues(2);
            contentNotes.put(columnNames[4][1],tid);
            contentNotes.put(columnNames[4][2],passDatas[3]);

             uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS,tableNames[4]);
            insertedUri = context.getContentResolver().insert(uri, contentNotes);

            string_index++;
        }

        if(passInt[3]==1)
        {
            /**
             * subtask present
             */
            for (int i=0;i<subtasklist.size();i++)
            {
                ContentValues contentSubTask = new ContentValues(3);
                contentSubTask.put(columnNames[5][1],tid);
                contentSubTask.put(columnNames[5][2],subtasklist.get(i));
                contentSubTask.put(columnNames[5][3],i+1);
                contentSubTask.put(columnNames[5][4],subtaskdone.get(i));

                 uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS,tableNames[5]);
                insertedUri = context.getContentResolver().insert(uri, contentSubTask);
            }
        }

        DbChangeReceiver.broadCastDbChange(context,IntentActions.ACTION_TASK_INSERT,new int[]{tid,passInt[1],passInt[4]},passDatas[1]);

    }

    public static void update(String[] passDatas, int[] passInt, Context context,
                              List<String> subtasklist,
                              List<Integer> subTaskdone, int tid, int isChange) {

        //DBProvider dbProvider =new DBProvider();
        String task = passDatas[0];
        String selection = columnNames[0][0]+ " = "+tid;
        ContentValues contentTask =new ContentValues();
        int int_index=0,string_index =0;
        for(int i=1;i<columnNos[0];i++)
        {
            if(columnTypes[0][i].equals("INTEGER"))
                contentTask.put(columnNames[0][i],passInt[int_index++]);
            else
                contentTask.put(columnNames[0][i],passDatas[string_index++]);
        }

        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[0]);

        int rows = context.getContentResolver().update(uri, contentTask, selection, null);

        Log.d("tasks","inserted is "+rows);

        /**
         * Remainder is present
         */
        Uri insertedUri;
        if(passInt[1]==1)
        {
            //reminder is present
            selection = "tid = "+tid;
            int rid=0;
            ContentValues contentReminder = new ContentValues(3);
            contentReminder.put(columnNames[1][1],tid);
            contentReminder.put(columnNames[1][2],passDatas[string_index++]);
            contentReminder.put(columnNames[1][3],passInt[int_index++]);

            uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[1]);
            rows = context.getContentResolver().update(uri,contentReminder,selection,null);

            if(rows==0)
            {
                insertedUri = context.getContentResolver().insert(uri,contentReminder);
                rid= Integer.parseInt(insertedUri.getLastPathSegment());
            }
            else{
                int reqcolumn[] = {0};

                Cursor cursor = Tasks.select(context,1,reqcolumn,selection,null,null,null,null);
                while (cursor.moveToNext())
                rid = cursor.getInt(cursor.getColumnIndex(Tasks.columnNames[1][0]));
            }

            if(passInt[5]==1)
            {
                selection = ""+columnNames[2][1] +" = "+rid;
                ContentValues contentAlarm = new ContentValues(5);
                contentAlarm.put(columnNames[2][1],rid);
                int i=2;
                while (i<columnNos[2])
                    contentAlarm.put(columnNames[2][i++],passInt[int_index++]);

                uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS,tableNames[2]);
                rows = context.getContentResolver().update(uri,contentAlarm,selection,null);

                if(rows==0)
                {
                    context.getContentResolver().insert(uri,contentAlarm);
                }

            }
        }
        else{
            uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[1]);
            context.getContentResolver().delete(uri,selection,null);
        }
        /**
         * notes is present
         *
         */
        selection = ""+columnNames[1][1] + " = "+tid;
        if(passInt[2]==1)
        {
            ContentValues contentNotes = new ContentValues(2);
            contentNotes.put(columnNames[4][1],tid);
            contentNotes.put(columnNames[4][2],passDatas[3]);

            uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[4]);
            rows = context.getContentResolver().update(uri, contentNotes, selection, null);
            if(rows==0)
            insertedUri = context.getContentResolver().insert(uri, contentNotes);

            string_index++;
        }

        if(passInt[3]==1)
        {
            /**
             * subtask present
             */
            uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS,tableNames[5]);
            context.getContentResolver().delete(uri,selection,null);
            for (int i=0;i<subtasklist.size();i++)
            {
                ContentValues contentSubTask = new ContentValues(3);
                contentSubTask.put(columnNames[5][1],tid);
                contentSubTask.put(columnNames[5][2],subtasklist.get(i));
                contentSubTask.put(columnNames[5][3],i+1);
                contentSubTask.put(columnNames[5][4],subTaskdone.get(i));


                insertedUri = context.getContentResolver().insert(uri, contentSubTask);
            }

        }

        DbChangeReceiver.broadCastDbChange(context, IntentActions.ACTION_TASK_UPDATE, new int[]{tid,passInt[1],passInt[4]},passDatas[1]);

    }

    public  static Cursor select(Context context, int tableno, int[] columnno,
                                 String selection, String[] selectionArgs,
                                 String groupby, String having, String orderby) {
        ParentDb parentDb =  ParentDb.getInstance(context);
        SQLiteDatabase db = parentDb.returnSQl();

        if (db != null) {
            String[] reqcolumns = new String[columnno.length];
            for (int i = 0; i < columnno.length; i++) {
                reqcolumns[i] = columnNames[tableno][columnno[i]];
            }

            Cursor tempcursor = parentDb.select(db, tableNames[tableno],
                    reqcolumns, selection,
                    selectionArgs, groupby,
                    having, orderby);

            return tempcursor;
        } else {
            return null;
        }


    }

    public  static Cursor select(Context context, boolean distinct,int tableno,
                                 int[] columnno, String selection, String[] selectionArgs,
                                 String groupby, String having, String orderby,String limit) {
        ParentDb parentDb = ParentDb.getInstance(context);
        SQLiteDatabase db = parentDb.returnSQl();


        if (db != null) {
            String[] reqcolumns = new String[columnno.length];
            //  reqcolumns = null;
            for (int i = 0; i < columnno.length; i++) {
                reqcolumns[i] = columnNames[tableno][columnno[i]];
            }

            Cursor tempcursor = parentDb.select(db,distinct, tableNames[tableno],
                    reqcolumns, selection, selectionArgs, groupby, having, orderby,limit);
            return tempcursor;
        } else {
            return null;
        }


    }

    /**
     * for updating only certain columns from required table.
     * all columns  updated must be of integer type
     * @param context
     * @param tableNo the req table
     * @param reqCol the req column no to be updated
     * @param passInt the new values to be entered to db
     * @param selection the selection of rows which is to be updated
     * @param selectionArgs the selection args
     * @return
     */
    public static int update(Context context,int tableNo,int[] reqCol ,
                             int[] passInt,String selection, String[] selectionArgs) {

        return  update(context , tableNo , reqCol,
                       passInt, null, null, selection , selectionArgs);
    }

    /**
     * for updating only certain columns from required table . column can be of
     * either string or integer type. for only integer column use overloaded update .
     * check the class
     * @param context
     * @param tableNo the req table
     * @param reqColInt the req integer column nos to be updated
     * @param passInt the new integer values to be entered to db
     * @param reqColString the req string column nos which is to be update
     * @param passString the new string values
     * @param selection the selection of rows which is to be updated
     * @param selectionArgs the selection args
     * @return
     */
    public static int update(Context context,int tableNo,int[] reqColInt ,
                             int[] passInt, int[] reqColString, String[] passString,
                             String selection, String[] selectionArgs) {

        AutoRefresh.setRefreshSharedPref(context);
        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[tableNo]);
        ContentValues contentValues = new ContentValues();

        if (passInt != null)
            for (int i=0;i<passInt.length;i++)
                contentValues.put(columnNames[tableNo][reqColInt[i]],passInt[i]);
        if( passString != null)
            for(int i=0;i< passString.length; i++)
                contentValues.put(columnNames[tableNo][reqColString[i]] , passString[i]);


        return context.getContentResolver().update(uri, contentValues, selection, selectionArgs);
    }


    public static int delete(Context context, int tableNo , String selection, String[] selectionArgs)
    {
        Uri uri  = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[tableNo]);
        return context.getContentResolver().delete(uri,selection,selectionArgs);
    }
}