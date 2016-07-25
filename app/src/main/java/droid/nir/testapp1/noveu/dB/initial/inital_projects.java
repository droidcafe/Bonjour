package droid.nir.testapp1.noveu.dB.initial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import droid.nir.testapp1.noveu.Util.Log;

import java.net.URI;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.dB.DBProvider;
import droid.nir.testapp1.noveu.dB.Project;
import droid.nir.testapp1.noveu.dB.Tasks;

/**
 * Created by droidcafe on 2/24/2016.
 */
public class inital_projects extends AsyncTask<Context,Void,Void> {

    @Override
    protected Void doInBackground(Context... params) {

        Log.d("initil_projects","starting");
        String[] initial_projects = params[0].getResources().getStringArray(R.array.initial_project);

        Project project = new Project(params[0]);

        //project.settingDatabase();
        for(int i=0;i<initial_projects.length;i++)
        {
            project.insert(initial_projects[i],0);
        }
        Project.setDefaultProject(params[0],constants.DEFAULT_PROJECT_ID);
        initial_count(params[0]);
        return null;
    }

    private static void initial_count(Context context) {

      Cursor cursor = Project.select(context,0,new int[]{0,1},null,null,null,null,null);
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
            context.getContentResolver().update(uri,contentValues,selection,null);


        }

    }
}
