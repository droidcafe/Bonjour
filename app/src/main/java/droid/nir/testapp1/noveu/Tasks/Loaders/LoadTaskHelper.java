package droid.nir.testapp1.noveu.Tasks.Loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import droid.nir.testapp1.noveu.Util.Log;

import java.util.ArrayList;
import java.util.List;

import droid.nir.testapp1.noveu.Tasks.data.TaskVitalData;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.dB.DBProvider;
import droid.nir.testapp1.noveu.dB.Tasks;
import droid.nir.testapp1.noveu.constants.constants;

/**
 * Created by droidcafe on 3/26/2016.
 */

/**
 * helper class for loading parts of tasks
 *
 */
public class LoadTaskHelper {

    /**
     * load the important details about task from task table
     * @param context
     * @param id the id of task
     * @return  name,pid, date,isrem,isnotes,issubtask,done
     */
    public static TaskVitalData loadTasksVitals(Context context,int id) {

        TaskVitalData taskVitalData = new TaskVitalData();
        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, Tasks.tableNames[0]);

        String[] projection_task = {Tasks.columnNames[0][1], Tasks.columnNames[0][2],
                Tasks.columnNames[0][3], Tasks.columnNames[0][4], Tasks.columnNames[0][5],
                Tasks.columnNames[0][6], Tasks.columnNames[0][7]};

        String selection = " _id  = ?";
        String selectionArgs[] = {String.valueOf(id)};

        Cursor cursor_task = context.getContentResolver().query(uri,
                projection_task, selection, selectionArgs, null);
        Log.d("ns", "notify size" + cursor_task.getCount());

        if(!(cursor_task.getCount()>0))
            return null;
        while (cursor_task.moveToNext()) {

            taskVitalData.name = cursor_task.getString(cursor_task.getColumnIndex(Tasks.columnNames[0][1]));
            taskVitalData.pid = cursor_task.getInt(cursor_task.getColumnIndex(Tasks.columnNames[0][2]));
            taskVitalData.date = cursor_task.getString(cursor_task.getColumnIndex(Tasks.columnNames[0][3]));
            taskVitalData.isrem = cursor_task.getInt(cursor_task.getColumnIndex(Tasks.columnNames[0][4]));
            taskVitalData.isnotes = cursor_task.getInt(cursor_task.getColumnIndex(Tasks.columnNames[0][5]));
            taskVitalData.issubtask = cursor_task.getInt(cursor_task.getColumnIndex(Tasks.columnNames[0][6]));
            taskVitalData.done = cursor_task.getInt(cursor_task.getColumnIndex(Tasks.columnNames[0][7]));

        }

        return taskVitalData;
    }

    /**
     * load the details about reminder from reminder table
     * @param context
     * @param tid the id of task to which the reminder is attache
     * @return an integer array with index 0-id of reminder 1- whether time is set for reminder
     */
   public static int[] loadReminder(Context context,int tid) {

       String projection_reminder[] = {"_id", "istime"};
       String selection_reminder = "tid = ? ";
       String selectionargs[] = {String.valueOf(tid)};
       Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, Tasks.tableNames[1]);
       Cursor remindercursor = context.getContentResolver().query(uri, projection_reminder, selection_reminder, selectionargs, null);

       if(!(remindercursor.getCount()>0))
           return null;

       int istime =0,rid =-1;
       while (remindercursor.moveToNext()) {
            istime = remindercursor.getInt(remindercursor.getColumnIndex(Tasks.columnNames[1][3]));
            rid = remindercursor.getInt(remindercursor.getColumnIndex(Tasks.columnNames[1][0]));
       }


       return new int[]{rid, istime};
   }

    /**
     * loads the details of when about a  task reminder from table alarm
     * @param context
     * @param rid the reminder to which the time details are attached to
     * @return array related to time 0-timehr 1- timemin 2- whether alarm is set 3-is repeat is set
     * 4 - id of row
     */
    public static int[] loadTime(Context context, int rid) {

        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, Tasks.tableNames[2]);
        String projection_time[] = {Tasks.columnNames[2][0],Tasks.columnNames[2][2],Tasks.columnNames[2][3],
                Tasks.columnNames[2][4],Tasks.columnNames[2][5]};

        String selection_time = "rid = ? ";
        String selectionargs[] = {String.valueOf(rid)};
        Cursor timecursor = context.getContentResolver().query(uri, projection_time, selection_time, selectionargs, null);
        int timehr =0,timemin =0,isalarm =0,isrepeat =0,id=0;

        Log.d("lst",""+timecursor.getCount() +" "+rid);
        if(timecursor.getCount() < 0)
            return null;

        while (timecursor.moveToNext()) {

            id= timecursor.getInt(timecursor.getColumnIndex(Tasks.columnNames[2][0]));
            timehr= timecursor.getInt(timecursor.getColumnIndex(Tasks.columnNames[2][2]));
            timemin = timecursor.getInt(timecursor.getColumnIndex(Tasks.columnNames[2][3]));
            isalarm = timecursor.getInt(timecursor.getColumnIndex(Tasks.columnNames[2][4]));
            isrepeat = timecursor.getInt(timecursor.getColumnIndex(Tasks.columnNames[2][5]));
            Log.d("lst"," "+timehr + " "+timemin + isalarm);

        }
        return new int[]{timehr, timemin, isalarm,isrepeat,id};
    }

    /**
     * load repeat details for a task
     * @param context
     * @param aid - the alarm id of task
     * @return - the repeat mode {@link constants#repeatMode }
     */
    public static int loadRepeat(Context context, int aid)
    {
        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, Tasks.tableNames[3]);
        String projection_repeat[] = {Tasks.columnNames[3][2]};
        String selection = "aid = ?";
        String selectionArgs[] = {String.valueOf(aid)};
        Cursor repeatCursor = context.getContentResolver().query(uri,projection_repeat,selection,selectionArgs,null);

        if(!(repeatCursor.getCount() > 0 ))
            return -1;

        while (repeatCursor.moveToNext())
        {
            return  repeatCursor.getInt(repeatCursor.getColumnIndex("mode"));
        }
        return  -1;
    }
    /**
     * helper class to load all subtask of particular task
     * @param context
     * @param id of the task
     * @return the list of subtask in order
     */

    public static List<String> loadSubTasks(Context context, int id)
    {
        List<String> listsubtask;
        String selection_subtask = "tid = ? and done = ?";
        String selectionArgs[]  = {String.valueOf(id),String.valueOf(0)};
        String[] projection_subtask = {Tasks.columnNames[5][2], Tasks.columnNames[5][3]};
        String orderby = Tasks.columnNames[5][3] + " ASC ";

        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, Tasks.tableNames[5]);

        Cursor cursor_subtask = context.getContentResolver().query(uri,projection_subtask,selection_subtask,selectionArgs,orderby);
        listsubtask = new ArrayList<>(cursor_subtask.getCount());

        while (cursor_subtask.moveToNext())
        {
            listsubtask.add(listsubtask.size(),cursor_subtask.getString(cursor_subtask.getColumnIndex(Tasks.columnNames[5][2])));
        }

        return listsubtask;
    }


    /**
     * helper class to load all details of subtask of particular task
     * @param context
     * @param id of the task
     * @return the list of subtask in order and whether done or nott
     */

    public static List[] loadSubTasksComplete(Context context, int id)
    {
        List<String> listsubtask;
        List<Integer> listdone;
        String selection_subtask = "tid = ? ";
        String selectionArgs[]  = {String.valueOf(id)};
        String[] projection_subtask = {Tasks.columnNames[5][2], Tasks.columnNames[5][3], Tasks.columnNames[5][4]};
        String orderby = Tasks.columnNames[5][3] + " ASC ";

        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, Tasks.tableNames[5]);

        Cursor cursor_subtask = context.getContentResolver().query(uri,projection_subtask,selection_subtask,selectionArgs,orderby);
        listsubtask = new ArrayList<>(cursor_subtask.getCount());
        listdone = new ArrayList<>(cursor_subtask.getCount());

        while (cursor_subtask.moveToNext())
        {
            listsubtask.add(listsubtask.size(),cursor_subtask.getString(cursor_subtask.getColumnIndex(Tasks.columnNames[5][2])));
            listdone.add(listdone.size(),cursor_subtask.getInt(cursor_subtask.getColumnIndex(Tasks.columnNames[5][4])));
        }

        return new List[]{listsubtask,listdone};
    }





    /**
     * helper class to load notes of particular task
     * @param context
     * @param id of the task
     * @return the note
     */
    public static String loadNotes(Context context,int id)
    {
        String notes ="";
        String selection_subtask = "tid = ? ";
        String selectionArgs[]  = {String.valueOf(id)};
        String[] projection_subtask = {Tasks.columnNames[4][2]};

       Uri  uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, Tasks.tableNames[4]);

        Cursor cursor_subtask = context.getContentResolver().query(uri,projection_subtask,
                selection_subtask,selectionArgs,null);

        if(!(cursor_subtask.getCount()>0))
            return null;
        while (cursor_subtask.moveToNext())
        {
            notes = cursor_subtask.getString(cursor_subtask.getColumnIndex(Tasks.columnNames[4][2]));
        }

        return notes;
    }

    /**
     * loads partial integer column from tasks like just repeat after displaying notificationn=
     * @param context
     * @param tableNo table required from tasks
     * @param reqColumn the required column
     * @param selection selection string
     * @param selectionArgs selection args
     * @return the integer data fetched in the order given in reqcolumn i- the selected rows j - selected column data for the rows
     */
    public static int[][] loadTaskPartial(Context context, int tableNo,
                                        int[] reqColumn, String selection,
                                        String[] selectionArgs) {
        Uri  uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, Tasks.tableNames[tableNo]);
        String[] projection_task = Import.inttoStringColumn(reqColumn, (tableNo + 1));
        Cursor cursor_task_data = context.getContentResolver().query(uri, projection_task,
                selection, selectionArgs, null);

        int[][] task_data = new int[cursor_task_data.getCount()][cursor_task_data.getColumnCount()];
        int j =0;
        if(cursor_task_data.getCount() <= 0)
            return null;
        while (cursor_task_data.moveToNext()) {
            for (int i =0 ; i < projection_task.length ; i++)
                task_data[j][i] = cursor_task_data.getInt(cursor_task_data.getColumnIndex(projection_task[i]));
            j++;
        }
        return task_data;
    }


    /**
     * loads partial integer column from tasks like just repeat after displaying notificationn=
     * @param context
     * @param tableNo table required from tasks
     * @param reqColumn the required column
     * @param selection selection string
     * @param selectionArgs selection args
     * @return the integer data fetched in the order given in reqcolumn
     */
    public static String loadTaskPartial(Context context, int tableNo,
                                        int reqColumn, String selection,
                                        String[] selectionArgs) {
        Uri  uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, Tasks.tableNames[tableNo]);
        String[] projection_task = Import.inttoStringColumn(new int[]{reqColumn}, (tableNo + 1));
        Cursor cursor_task_data = context.getContentResolver().query(uri, projection_task,
                selection, selectionArgs, null);

        String task_data ="";
        if(cursor_task_data.getCount() <= 0)
            return null;
        while (cursor_task_data.moveToNext()) {
            for (int i =0 ; i < projection_task.length ; i++)
                task_data = cursor_task_data.getString(cursor_task_data.getColumnIndex(projection_task[i]));
        }
        return task_data;
    }

    public static int getDone(Context context , int tid){
        String selection = "_id = ?";
        String selectionArgs[] = {Integer.toString(tid)};

        Uri  uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, Tasks.tableNames[0]);
        String[] projection_task = {"done"};
        Cursor task_done = context.getContentResolver().query(uri,
                projection_task,selection,selectionArgs,null);

        int done = 0;
        while (task_done.moveToNext())
            done = task_done.getInt(task_done.getColumnIndex("done"));

        return done;
    }

    public static int getProject(Context context , int tid){
        String selection = "_id = ?";
        String selectionArgs[] = {Integer.toString(tid)};

        Uri  uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, Tasks.tableNames[0]);
        String[] projection_task = {Tasks.columnNames[0][2]};
        Cursor task_done = context.getContentResolver().query(uri,
                projection_task,selection,selectionArgs,null);

        int pid = 0;
        while (task_done.moveToNext())
            pid = task_done.getInt(task_done.getColumnIndex(Tasks.columnNames[0][2]));

        return pid;
    }
}

