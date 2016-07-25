package droid.nir.testapp1.noveu.Tasks.Loaders;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import droid.nir.testapp1.noveu.Util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Home.data.dataHome;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.TimeUtil;
import droid.nir.testapp1.noveu.dB.Project;
import droid.nir.testapp1.noveu.dB.Tasks;

/**
 * Created by droidcafe on 3/15/2016.
 */
public class LoadTask {


    static Activity activity;
    static Context context;
    String todaydate, tomrdate,yesterdate;
    String preselection;
    String[] preSelectionArgs;

    public LoadTask(Activity activity,Context context)
    {
        LoadTask.activity = activity;
        LoadTask.context =context;
    }

    /**
     * intialisation any preselection conditions for all task like for a particular project only
     * @param preselection
     * @param preSelectionArgs
     */
    public void loadPreSelection(String preselection,String[] preSelectionArgs)
    {
        this.preselection = preselection;
        this.preSelectionArgs = preSelectionArgs;
    }

    /**
     * complete package for loading tasks
     * loads every tasks present
     * and arranges them according to date order
     * @return
     */
    public List<dataHome> loadAllTasks()
    {
        Cursor cursor_yesterday = loadYesterday();
        Cursor cursor_today = loadToday();
        Cursor cursor_tmrw = loadTommorow();

        List<dataHome> taskList = new ArrayList<>();
        List<dataHome> task_today_list = getList(cursor_today,context.getResources().getString(R.string.today)+ "  "+todaydate);
        List<dataHome> task_tmrw_list = getList(cursor_tmrw,context.getResources().getString(R.string.tommorow));
        List<dataHome> task_yesteday_list = getList(cursor_yesterday, context.getResources().getString(R.string.yesterday));


        taskList.addAll(checkList(task_today_list));
        taskList.addAll(checkList(task_tmrw_list));
        taskList.addAll(checkList(task_yesteday_list));

        String dateselection = " date NOT IN (?,?,?)";
        String selectionArgs[] = {todaydate,yesterdate,tomrdate};
        List<String> datelist = getDateList(loadDate(dateselection,selectionArgs));
        for(int i=0;i<datelist.size();i++)
        {
            Cursor datetaskcursor = loadTasks(checkSelection("date = ?"),checkSelectionArgs(datelist.get(i)));
            if(datetaskcursor.getCount()!=0)
            {
                List<dataHome> list = getList(datetaskcursor,datelist.get(i));
                taskList.addAll(checkList(list));
            }
        }
        return taskList;
    }

    /**
     * loads all tasks for today
     * @return
     */

    public  Cursor loadToday() {

        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        todaydate= TimeUtil.getDate(day, month, year);

        return loadTasks(checkSelection("date = ?"),checkSelectionArgs(todaydate));
    }
    /**
     * loads all tasks for yesterday
     * @return
     */


    public Cursor loadYesterday() {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, -1);
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        yesterdate = TimeUtil.getDate(day, month, year);
        return loadTasks(checkSelection("date = ?"), checkSelectionArgs(yesterdate));
    }

    /**
     * loads all tasks for tommorrow
     * @return
     */

    public Cursor loadTommorow() {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, 1);
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        tomrdate = TimeUtil.getDate(day, month, year);
        return loadTasks(checkSelection("date = ?"), checkSelectionArgs(tomrdate));
    }

    /**
     * load all tasks for that particular selection and selectionArgs ..return its cursor
     * @param selection - selection string
     * @param selectionArgs selection arguments
     * @return
     */
    public static Cursor loadTasks(String selection,String[] selectionArgs) {

        int[] columnNo = {0, 1, 2};
        return loadTasks(context,selection,selectionArgs,columnNo);
    }
    /**
     * load all tasks for that particular selection and selectionArgs ..return its cursor
     * @param selection - selection string
     * @param selectionArgs selection arguments
     * @param columnNo required columns
     * @return
     */
    public static Cursor loadTasks(Context context , String selection, String[] selectionArgs, int[] columnNo)
    {
        return Tasks.select(context,0,columnNo,selection,selectionArgs,null,null,null);
    }


    /**
     * gets the list of a particular date
     * @param cursor cursor of all task of the date
     * @param firstItem if a special firstitem should be there. if not null
     * @return
     */
    public static List<dataHome> getList(Cursor cursor, String firstItem) {

        List<dataHome> list;
        boolean isfirstitem;
        if(firstItem!=null)
        {
            list = new ArrayList<>(cursor.getCount() + 1);
            dataHome firstitem = new dataHome();
            firstitem.tasktitle = firstItem;
            firstitem.taskid = -1;

            list.add(list.size(),firstitem);
            isfirstitem =true;
        }
        else
        {
            list = new ArrayList<>(cursor.getCount());
            isfirstitem = false;
        }

        while (cursor.moveToNext()) {
            dataHome object = new dataHome();
            object.tasktitle = cursor.getString(cursor.getColumnIndex(Tasks.columnNames[0][1]));
            int projectid = cursor.getInt(cursor.getColumnIndex(Tasks.columnNames[0][2]));

            object.projectName = Project.getProjectName(context,projectid);
            object.taskid = cursor.getInt(cursor.getColumnIndex(Tasks.columnNames[0][0]));

            list.add(list.size(), object);
        }

        if(isfirstitem)
        {
            if(list.size()==1)
                return null;
        }
        else {
            if(list.size()==0)
            {
                return null;
            }
        }
        return list;
    }



    /**
     * returns a list of dates from the cursor
     * @param cursor cursor of list which is to be converted
     * @return
     */
    private List<String> getDateList(Cursor cursor) {
        List<String> dateList = new ArrayList<>(cursor.getCount());

        while (cursor.moveToNext())
        {
            String date = cursor.getString(cursor.getColumnIndex(Tasks.columnNames[0][3]));
            Log.d("home", "date " + date);
            dateList.add(dateList.size(),date);
        }

        return dateList;
    }

    /**
     * gives cursor of date
     * @param selection date selection criteria
     * @param selectionArgs
     * @return
     */
    private Cursor loadDate(String selection, String[] selectionArgs)
    {
        int[] columnNo = {3};
        return Tasks.select(context,true, 0, columnNo, selection, selectionArgs, null, null,"date desc",null);
    }


    /**
     * checks if any preselction present and if present then concats the selection string with preselction
     * @return
     */
    private String checkSelection(String selection)
    {

        if(preselection!=null)
        {
            selection = preselection.concat(" and "+selection);
        }


        return selection;
    }

    /**
     * checks if any preselection args present and add the new argument accordingly
     * @param date the new selection arg
     * @return
     */

    private String[] checkSelectionArgs(String date)
    {
        String[] selectionArgs ;
        if(preSelectionArgs!=null)
        {
            selectionArgs = new String[preSelectionArgs.length+1];
            int i;
            for(i = 0; i<preSelectionArgs.length;i++)
                selectionArgs[i] = preSelectionArgs[i];

            selectionArgs[i] = date;
        }
        else{
            selectionArgs= new String[1];
            selectionArgs[0] = date;
        }

        return selectionArgs;
    }

    /**
     * checks list if it is null or not
     * @param list
     * @return
     */
    private List<dataHome> checkList(List<dataHome> list)
    {
        if(list==null)
            return new ArrayList<>(0);
        else
            return list;
    }


}
