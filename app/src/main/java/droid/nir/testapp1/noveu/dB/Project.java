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
import droid.nir.testapp1.noveu.Projects.data.ProjectList;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;

import droid.nir.databaseHelper.DatabaseCreater;
import droid.nir.testapp1.noveu.Projects.data.ProjectData;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.toast;

/**
 * Created by droidcafe on 2/23/2016.
 */
public class Project {

    static Context context;
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

    public static void delete(Context context, int tableNo, String selection, String[] selectionArgs, int pid) {

        if(pid == getDefaultProject(context))
        {
            /**
             * set next project default as the one having highest task count
             */
        }
        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, tableNames[tableNo]);
        context.getContentResolver().delete(uri, selection, selectionArgs);

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
        String defaultProject = (String) Import.getSettingSharedPref(context, SharedKeys.general_project_default,1);
        int projectId =Integer.parseInt(defaultProject);

        return projectId;
//        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname, 0);
//        return sharedPreferences.getInt(SharedKeys.projectDefault, -1);
    }

    public static void setDefaultProject(Context context, int id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedKeys.prefname, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SharedKeys.projectDefault , id);
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

        Cursor cursor = Project.select(context, 0, new int[]{0,1}, null, null, null, null, null);
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
     * @param projectId the id of project whose size is to be changed
     */
    public static void updateProject(Context context, int projectId) {

        ProjectData projectData = getProject(context, projectId);
        if (projectData == null)
            return;
        projectData.ProjectSize++;
        ContentValues contentValues = new ContentValues(2);
        contentValues.put(columnNames[0][1], projectData.ProjectName);
        contentValues.put(columnNames[0][2], projectData.ProjectSize);

        String selection = "_id = " + projectId;
        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, "project");

        context.getContentResolver().update(uri, contentValues, selection, null);

    }


}


