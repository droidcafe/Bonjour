package droid.nir.testapp1.noveu.Tasks.data;

import android.widget.TextView;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Tasks.TaskUtil;

/**
 * Created by droidcafe on 3/26/2016.
 */
public class TaskVitalData {

    public String name,date;
    public int pid ,isnotes,issubtask,done,isrem;

    public static TaskVitalData initialise(int mode , String task , String dateselected)
    {
        TaskVitalData taskVitalData = new TaskVitalData();
        taskVitalData.isrem = TaskUtil.isReminder(mode);
        taskVitalData.isnotes = TaskUtil.isNotes(mode);
        taskVitalData.issubtask = TaskUtil.isSubTask(mode);
        taskVitalData.name =  task;
        taskVitalData.date = dateselected;

        return taskVitalData;
    }
}
