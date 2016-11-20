package droid.nir.testapp1.noveu.dB.initial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import java.util.Arrays;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Tasks.data.SharedData;
import droid.nir.testapp1.noveu.Util.AutoRefresh;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.Util.TimeUtil;
import droid.nir.testapp1.noveu.dB.DBProvider;
import droid.nir.testapp1.noveu.dB.Project;
import droid.nir.testapp1.noveu.dB.Tasks;

/**
 * Created by droidcafe on 9/19/2016.
 */
public class initial_tasks {

    Context context;

    public Void execute(Context context) {

        this.context = context;
        AutoRefresh.setRefreshSharedPref(context);


        String[] initial_tasks = context.getResources().getStringArray(R.array.initial_task);
        int[] initial_task_reminder = context.getResources().getIntArray(R.array.initial_task_reminder);
        int[] initial_task_notes = context.getResources().getIntArray(R.array.initial_task_notes);
        int[] initial_task_subtasks = context.getResources().getIntArray(R.array.initial_task_subtasks);
        int[] initial_task_projects = context.getResources().getIntArray(R.array.initial_task_project);

        for (int i = 0; i < initial_tasks.length; i++) {
            String[] passData = new String[4];
            int[] passInt = new int[11];
            Arrays.fill(passData, "");
            Arrays.fill(passInt, 0);

            passData[0] = initial_tasks[i];
            passData[1] = TimeUtil.getTodayDate();

            passInt[0] = initial_task_projects[i];
            passInt[1] = initial_task_reminder[i];
            passInt[2] = initial_task_notes[i];
            passInt[3] = initial_task_subtasks[i];
            passInt[4] = 0; //done

            Log.d("it","inserting "+i);
            Tasks.insert(passData, passInt, context, SharedData.list, SharedData.subTaskdone);
        }
        initial_count(context);
        return null;
    }


    /**
     * assign initial_count of tasks for projects
     * @param context
     */
    public static void initial_count(Context context) {

        Cursor cursor = Project.select(context,0,new int[]{0,1},null,null,null,null,null);
        Log.d("it","count "+cursor.getCount());
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndex(Project.columnNames[0][0]));
            String name = cursor.getString(cursor.getColumnIndex(Project.columnNames[0][1]));

            String selection = " pid = "+id;
            Cursor cursor1 = Tasks.select(context,0,new int[]{0},selection,null,null,null,null);
            int count = cursor1.getCount();
            Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, "project");
            ContentValues contentValues = new ContentValues();
            contentValues.put(Project.columnNames[0][1],name);
            contentValues.put(Project.columnNames[0][2],count);
            selection = "_id = "+id;
            Log.d("ip", "count " + count);
            context.getContentResolver().update(uri,contentValues,selection,null);


        }

    }
}
