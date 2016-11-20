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
public class inital_projects  {

    
    public Void execute(Context context) {

        Log.d("initil_projects","starting");
        String[] initial_projects = context.getResources().getStringArray(R.array.initial_project);

        Project project = new Project(context);

        for(int i=0;i<initial_projects.length;i++)
        {
            int size = (i == 2 ) ? 0 : 1;
            project.insert(initial_projects[i],size);
        }
        Project.setDefaultProject(context, constants.DEFAULT_PROJECT_ID);
        initial_tasks.initial_count(context);
        return null;
    }


}
