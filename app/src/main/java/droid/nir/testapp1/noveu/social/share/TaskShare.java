package droid.nir.testapp1.noveu.social.share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.List;

import droid.nir.testapp1.Bonjour;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Tasks.Loaders.LoadTaskHelper;
import droid.nir.testapp1.noveu.Tasks.data.SharedData;
import droid.nir.testapp1.noveu.Tasks.data.TaskVitalData;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.Util.TimeUtil;
import droid.nir.testapp1.noveu.dB.Project;

/**
 * Created by droidcafe on 7/9/2016.
 */

/**
 * timedata 0 -timehr 1- timemin 2-isalarm
 * reminderdata 0 - rid 1 - istime
 */
public class TaskShare  {

    TaskVitalData taskVitalData;
    String project;
    int[]  timedata , reminderdata;


    public TaskShare()
    {
    }
    public TaskShare(int[] passInt,String project , TaskVitalData taskVitalData)
    {
        this.taskVitalData = taskVitalData;
        Log.d("ts","gates r "+taskVitalData.isrem +" n "+taskVitalData.isnotes +" s "+taskVitalData.issubtask);
        reminderdata= new int[2];
        timedata = new int[3];
        reminderdata[0] = passInt[0];
        reminderdata[1] = passInt[1];

        for (int i = 0;i<timedata.length;i++) {
            Log.d("ts"," "+passInt[reminderdata.length + i]);
            timedata[i] = passInt[reminderdata.length + i];
        }
        this.project = project;
        Log.d("ts",project);
    }

    @SuppressLint("StringFormatMatches")
    public Intent share(Context context)
    {
        String title  = taskVitalData.name;
        String share = " "+title + "\n";
        if (taskVitalData.issubtask == 1)
        {
            String todo = "", done ="";
            Log.d("ts"," s "+SharedData.list.toString() + " d "+SharedData.subTaskdone.toString());
            for (int i = 0; i < SharedData.subTaskdone.size(); i++) {
                int status = SharedData.subTaskdone.get(i);
                Log.d("ts","status "+status);
                if(status == 0)
                    todo = todo.concat(SharedData.list.get(i) + "\n");
                else if(status == 1)
                    done = done.concat(SharedData.list.get(i) + "\n");
            }
            Log.d("ts","t "+todo +" "+done);
            if( !todo.equals(""))
                share = share.concat(Import.getString(context,R.string.task_share_todo) + "\n" + todo);
            if(!done.equals("")) {
                share = share.concat(Import.getString(context, R.string.task_share_done) + "\n" + done);
            }
        }
        Log.d("ts"," share "+share);
        if(taskVitalData.isnotes == 1) {
            Log.d("ts"," s "+SharedData.notes);
            if (!SharedData.notes.equals("")) {
                share = share.concat(Import.getString(context, R.string.task_share_notes)
                        + "\n" + SharedData.notes + "\n");
            }
        }
        Log.d("ts"," share "+share);

        if (taskVitalData.isrem == 1) {
            share = share.concat(context.getString(R.string.task_share_rem, taskVitalData.date));
            if (reminderdata[1] == 1) {
                String time , alarm;
                if (timedata[2] == 1) {
                    alarm = "with";
                }
                else {
                    alarm = "without";
                }
                time = TimeUtil.formatTime(timedata[0] , timedata[1]);

                share = share.concat(" "+context.getResources().getString(R.string.task_share_rem2, time , alarm));
            }
        }
        Log.d("ts"," share "+share);
        return Master.share(title , share);


    }
    public boolean loadTask(Context context , Integer tid) {

        taskVitalData = LoadTaskHelper.loadTasksVitals(context, tid);
        if (taskVitalData != null) {
            project = Project.getProjectName(taskVitalData.pid);
            if (taskVitalData.isrem == 1) {
                reminderdata = LoadTaskHelper.loadReminder(context, tid);
                if(reminderdata[1] == 1)
                    timedata = LoadTaskHelper.loadTime(context, reminderdata[0]);
            }

            if (taskVitalData.isnotes == 1)
                SharedData.notes = LoadTaskHelper.loadNotes(context, tid);
            if (taskVitalData.issubtask == 1)
            {
                List[] sub = LoadTaskHelper.loadSubTasksComplete(context, tid);
                List<String> subTasks = sub[0];
                if (subTasks.size() > 0) {
                    SharedData.list = subTasks;
                    SharedData.subTaskdone = sub[1];
                }
            }
            return true;
        }
        return false;
    }




    public class AsyncShare extends AsyncTask<Integer,Void,Void>
    {

        @Override
        protected Void doInBackground(Integer... params) {
            int tid = params[0];
            if(loadTask(Bonjour.getContext() , tid))
                share(Bonjour.getContext());
            return null;
        }
    }

}
