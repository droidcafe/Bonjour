package droid.nir.testapp1.noveu.Tasks.Loaders;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import droid.nir.testapp1.Bonjour;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Home.Home;
import droid.nir.testapp1.noveu.Tasks.data.TaskVitalData;
import droid.nir.testapp1.noveu.Util.AutoRefresh;
import droid.nir.testapp1.noveu.dB.Project;
import droid.nir.testapp1.noveu.dB.Tasks;

/**
 * Created by droidcafe on 7/13/2016.
 */
public class DeleteTask {

    public static int delete(int tid)
    {
        Context context = Bonjour.getContext();
        TaskVitalData taskVitalData = LoadTaskHelper.loadTasksVitals(context, tid);
        if(taskVitalData == null)
            return -1;
        if(taskVitalData.isrem == 1)
            removeReminder(context,tid);
        if(taskVitalData.issubtask == 1)
            removeSubTask(context, tid);
        if(taskVitalData.isnotes == 1)
            removeNote(context, tid);

        Project.updateProject(context, taskVitalData.pid,-1);
        String selection = "_id = ? ";
        String selectionArgs[] = {Integer.toString(tid)};
        return Tasks.delete(context,0, selection, selectionArgs);
    }

    private static void removeReminder(Context context , int tid) {
        int reminderdata[] = LoadTaskHelper.loadReminder(context, tid);
        if(reminderdata[1] == 1)
            removeAlarm(context, reminderdata[0]);

        String selection = "tid = ? ";
        String selectionArgs[] = {Integer.toString(tid)};
        Tasks.delete(context,1, selection, selectionArgs);
    }


    private static void removeNote(Context context, int tid) {
        String selection = "tid = ? ";
        String selectionArgs[] = {Integer.toString(tid)};
        Tasks.delete(context,4, selection, selectionArgs);
    }

    private static void removeSubTask(Context context, int tid) {
        String selection = "tid = ? ";
        String selectionArgs[] = {Integer.toString(tid)};
        Tasks.delete(context,5, selection, selectionArgs);
    }

    private static void removeAlarm(Context context, int rid) {
        int alarmdata[] = LoadTaskHelper.loadTime(context, rid);
        if (alarmdata[3] == 1) {
            removeRepeat(context,alarmdata[4]);
        }

        String selection = "_id = ? ";
        String selectionArgs[] = {Integer.toString(alarmdata[4])};
        Tasks.delete(context,2, selection, selectionArgs);
    }

    private static void removeRepeat(Context context, int aid) {
        String selection = "aid = ? ";
        String selectionArgs[] = {Integer.toString(aid)};
        Tasks.delete(context,3, selection, selectionArgs);
    }

    public static class AsyncDelete extends AsyncTask<Integer, Void, Integer> {
        Context context = Bonjour.getContext();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AutoRefresh.setRefreshSharedPref(context);
        }

        @Override
        protected Integer doInBackground(Integer... params) {

            return DeleteTask.delete(params[0]);
        }

        @Override
        protected void onPostExecute(Integer noofrows) {
            super.onPostExecute(noofrows);
            if (noofrows > 0) {
                if (!new Home().showDeleteSnack())
                    Toast.makeText(context, context.getString(R.string.task_delete_successful), Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(context, context.getString(R.string.task_delete_unsuccessful), Toast.LENGTH_LONG).show();
        }
    }
}
