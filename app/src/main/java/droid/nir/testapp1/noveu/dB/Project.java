package droid.nir.testapp1.noveu.dB;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;

import java.util.ArrayList;

import droid.nir.testapp1.Bonjour;
import droid.nir.testapp1.noveu.Projects.data.ProjectData;
import droid.nir.testapp1.noveu.Projects.data.ProjectList;
import droid.nir.testapp1.noveu.Tasks.TaskUtil;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.toast;

/**
 * Created by droidcafe on 2/23/2016.
 */
public class Project {

    static Context context;

    /**
     * different delete modes
     * safe - delete after moving all task to new project
     * quick - delete the project by deleting all tasks in that project
     */
    public enum deleteMode {
        safe, quick
    }

    public static String[][] columnNames = {
            {"_id", "proname", "itemsno"}
    };
    public static String[][] columnTypes = {{"INTEGER", "VARCHAR(100)", "INTEGER"}
    };
    public static String[] tableNames = {"project"};
    public static int[] columnNos = {3};
    toast maketext;

    public Project(Context context) {
        this.context = context;
        maketext = new toast(context);


    }


    public void update(String proname, int[] intvalues, SQLiteDatabase db) {
        /*
        intvalues[0]= id  1=noofitems
         */
        Asyncupdate asyncupdate = new Asyncupdate(proname, intvalues, db);
        asyncupdate.execute();
    }


    public class Asyncupdate extends AsyncTask<Void, Void, Void> {
        String name;
        int id, noofitems;

        public Asyncupdate(String values, int[] intvalues, SQLiteDatabase db) {
            name = values;
            id = intvalues[0];
            noofitems = intvalues[1];
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentValues contentValues = new ContentValues(2);
            contentValues.put(columnNames[0][1], name);
            contentValues.put(columnNames[0][2], noofitems);
            String selection = "_id = " + id;
            context.getContentResolver().update(DBProvider.CONTENT_URI_TASKS, contentValues, selection, null);
            return null;
        }

    }

    public static Cursor select(Context context, int tableno, int[] columnno, String selection, String[] selectionArgs, String groupby, String having, String orderby) {

        ParentDb parentDb = ParentDb.getInstance(context);
        SQLiteDatabase db = parentDb.returnSQl();
        if (db != null) {
            String[] reqcolumns = new String[columnno.length];
            //  reqcolumns = null;
            for (int i = 0; i < columnno.length; i++) {
                reqcolumns[i] = columnNames[tableno][columnno[i]];
            }

            Cursor tempcursor = parentDb.select(db, tableNames[tableno], reqcolumns, selection, selectionArgs, groupby, having, orderby);
            return tempcursor;
        } else {
            return null;
        }


    }

   /* public int delete(SQLiteDatabase db, int tableno, String selection, String[] whereargs) {
        if (db != null)
            return edatabaseCreater.deleterow(db, tableNames[tableno], selection, whereargs);

        else
            return -1;
    }*/

    public void insert(String name, int noofitems) {
        Log.d("projects", "inserting " + name);
        Asyncinsert asyncinsert = new Asyncinsert(name, noofitems);
        asyncinsert.execute();
    }

    /**
     * method to delete a project
     *
     * @param context
     * @param tableNo
     * @param selection
     * @param selectionArgs
     * @param pids
     * @param mode
     */
    public static void delete(Context context, int tableNo, String selection, String[] selectionArgs, Integer pids[], deleteMode mode) {

        if (pids[0] == getDefaultProject(context)) {
            /**
             * set next project default as the one having highest task count
             */
        }
        new AsyncDelete(context, mode).execute(pids);
        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[tableNo]);
        context.getContentResolver().delete(uri, selection, selectionArgs);

    }

    /**
     * background class for deleting projects
     * if deletemode == safe the params[0] = pid to be deleted
     * params[1] = new pid
     * else if deletemode == quick params[0] = pid to be deleted
     */
    static class AsyncDelete extends AsyncTask<Integer, Void, Void> {

        deleteMode mode;
        Context context;

        public AsyncDelete(Context context, deleteMode mode) {
            this.mode = mode;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            int pid = params[0];

            if (mode == deleteMode.safe) {
                int new_pid = params[1];
                TaskUtil.changeProject(context, new_pid, pid);
                return null;
            }

            String selection = "pid = ?";
            String[] selectionArgs = {Integer.toString(pid)};
            Tasks.delete(context,0,selection,selectionArgs);
            return null;
        }
    }

    class Asyncinsert extends AsyncTask<Void, Void, Uri> {
        String name;
        int noofitems;

        public Asyncinsert(String name, int noofitems) {
            this.name = name;
            this.noofitems = noofitems;
        }

        @Override
        protected Uri doInBackground(Void... params) {

            Log.d("project", " " + name + " " + noofitems);
            ContentValues contentValues = new ContentValues();
            contentValues.put(columnNames[0][1], name);
            contentValues.put(columnNames[0][2], noofitems);
            Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[0]);
            return context.getContentResolver().insert(uri, contentValues);
            // return null;

        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);

            int id = Integer.parseInt(uri.getLastPathSegment());
            Log.d("project", "insert " + id);

        }
    }

    public static String getProjectName(Context context, int projectId) {
        String selection = "_id = " + projectId;
        Cursor cursor = Project.select(context, 0, new int[]{1}, selection, null, null, null, null);

        while (cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndex(Project.columnNames[0][1]));
        }

        return "";
    }

    public static int getProjectSize(int projectId) {

        Context context = Bonjour.getContext();
        String selection = "_id = " + projectId;
        Cursor cursor = Project.select(context, 0, new int[]{2}, selection, null, null, null, null);

        while (cursor.moveToNext()) {
            return cursor.getInt(cursor.getColumnIndex(Project.columnNames[0][2]));
        }
        return -1;
    }

    public static int getDefaultProject(Context context) {
        String defaultProject = (String) Import.getSettingSharedPref(context, SharedKeys.general_project_default, 1);
        int projectId = Integer.parseInt(defaultProject);

        return projectId;
//        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname, 0);
//        return sharedPreferences.getInt(SharedKeys.projectDefault, -1);
    }

    public static void setDefaultProject(Context context, int id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SharedKeys.projectDefault, id);
        editor.commit();
    }

    public static ProjectData getProject(Context context, int projectId) {

        ProjectData projectData = new ProjectData();
        String selection = "_id = " + projectId;
        Cursor cursor = Project.select(context, 0, new int[]{1, 2}, selection, null, null, null, null);

        if (cursor.getCount() <= 0)
            return null;
        while (cursor.moveToNext()) {
            projectData.ProjectName = cursor.getString(cursor.getColumnIndex(Project.columnNames[0][1]));
            projectData.ProjectSize = cursor.getInt(cursor.getColumnIndex(Project.columnNames[0][2]));


        }
        return projectData;
    }

    public static ArrayList<String> getProjectNames(Context context) {

        Cursor cursor = Project.select(context, 0, new int[]{1}, null, null, null, null, null);
        ArrayList<String> projectNames = new ArrayList<>(cursor.getCount());

        if (cursor.getCount() <= 0)
            return null;
        while (cursor.moveToNext()) {
            projectNames.add(cursor.getString(cursor.getColumnIndex(Project.columnNames[0][1])));
        }
        return projectNames;
    }

    public static ProjectList getProjects(Context context) {

        Cursor cursor = Project.select(context, 0, new int[]{0, 1}, null, null, null, null, null);
        ProjectList projectList = new ProjectList();
        projectList.projectNames = new ArrayList<>(cursor.getCount());
        projectList.projectIds = new ArrayList<>(cursor.getCount());

        if (cursor.getCount() <= 0)
            return null;
        while (cursor.moveToNext()) {
            projectList.projectNames.add(cursor.getString(cursor.getColumnIndex(Project.columnNames[0][1])));
            projectList.projectIds.add(cursor.getInt(cursor.getColumnIndex(Project.columnNames[0][0])));
        }
        return projectList;
    }

    /**
     * increases the project size on inserting a new task to the project
     *
     * @param context
     * @param projectId     the id of project whose size is to be changed
     * @param increase_size the no by which size of project is to be increased
     */
    public static int updateProject(Context context, int projectId, int increase_size) {

        Log.d("pr", "size " + increase_size);
        ProjectData projectData = getProject(context, projectId);
        if (projectData == null)
            return -1;
        projectData.ProjectSize += increase_size;
        ContentValues contentValues = new ContentValues(2);
        contentValues.put(columnNames[0][1], projectData.ProjectName);
        contentValues.put(columnNames[0][2], projectData.ProjectSize);

        String selection = "_id = " + projectId;
        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, "project");

        return context.getContentResolver().update(uri, contentValues, selection, null);

    }


    /**
     * increases the project size on inserting a new task to the project
     *
     * @param context
     * @param projectId     the id of project whose size is to be changed
     * @param increase_size the no by which size of project is to be increased
     */
    public static int updateProject(Context context, int projectId, int oldprojectId, int increase_size) {

        updateProject(context, oldprojectId, -increase_size);
        return updateProject(context, projectId, increase_size);
    }

    /**
     * add a new project
     *
     * @param text the new or updated project name
     */

    public static int doPositiveInsert(Context context, String text) {
        Log.d("ProjectManager", "" + text);


        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, "project");
        ContentValues contentValues = new ContentValues(2);
        contentValues.put(Project.columnNames[0][1], text);
        contentValues.put(Project.columnNames[0][2], 0);
        Uri uriInsert = context.getContentResolver().insert(uri, contentValues);

        return Integer.parseInt(uriInsert.getLastPathSegment());

    }

}


