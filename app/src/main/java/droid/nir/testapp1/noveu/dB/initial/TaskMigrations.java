package droid.nir.testapp1.noveu.dB.initial;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import droid.nir.databaseHelper.Todolist;
import droid.nir.testapp1.Bonjour;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.dB.Project;
import droid.nir.testapp1.noveu.dB.Tasks;
import droid.nir.testapp1.noveu.dB.metaValues.dBmetaData;

/**
 * Created by droidcafe on 7/24/2016.
 */
public class TaskMigrations {

    public static void migrate() {
        Context context = Bonjour.getContext();
        int update = Import.getSharedPref(context, SharedKeys.update);
        int olddbVersion = Import.getSharedPref(context, SharedKeys.legacyDbVersion_old);

        if (update == 1 && olddbVersion < dBmetaData.DB_VERSION_CHANGE_TO_TASKS) {
            new AsyncMigrate().execute();
        }
    }

    public static class AsyncMigrate extends AsyncTask<Void, Void, Void> {

        Context context;
        Todolist todolist;
        Tasks tasks;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            context = Bonjour.getContext();
            todolist = new Todolist(context);
            tasks = new Tasks(context);
        }

        @Override
        protected Void doInBackground(Void... params) {

            SQLiteDatabase db = todolist.settingDatabase();
            Cursor todoCursor = todolist.select(db, 0, null, null, null, null, null, null);
            while (todoCursor.moveToNext()) {
                String[] passDatas = new String[4];
                int[] passInt = new int[11];
                List<Integer> subtaskdone = null;
                List<String> subtasks = null;

                int id = todoCursor.getInt(todoCursor.getColumnIndex("_id"));
                passDatas[0] = todoCursor.getString(todoCursor.getColumnIndex("title"));
                passDatas[1] = todoCursor.getString(todoCursor.getColumnIndex("date"));

                passInt[0] = Project.getDefaultProject(context);
                passInt[1] = todoCursor.getInt(todoCursor.getColumnIndex("notification"));
                passInt[2] = 0; /** notes */
                int size = todoCursor.getInt(todoCursor.getColumnIndex("listno"));
                if (size > 0) { /** subtasks **/
                    passInt[3] = 1;
                    subtaskdone = new ArrayList<>(size);
                    subtasks = new ArrayList<>(size);

                    int columnreq[] = {2};
                    String itemSelection = "tid = " + id;
                    Cursor itemsCursor = todolist.select(db, 1, columnreq, itemSelection, null, null, null, null);
                    while (itemsCursor.moveToNext()) {
                        String items = itemsCursor.getString(itemsCursor.getColumnIndex("listitem"));
                        subtasks.add(items);
                        subtaskdone.add(0);
                    }
                } else {
                    passInt[3] = 0; /** subtasks **/
                }

                passInt[4] = 0; /** done */
                if (passInt[1] == 1) { /** reminder **/
                    int columnreq[] = {2, 3};
                    String itemSelection = "tid = " + id;
                    Cursor remCursor = todolist.select(db, 2, columnreq, itemSelection, null, null, null, null);
                    while (remCursor.moveToNext()) {
                        passInt[5] = 1;
                        passInt[6] = remCursor.getInt(remCursor.getColumnIndex("nhr"));
                        passInt[7] = remCursor.getInt(remCursor.getColumnIndex("nmin"));
                    }
                    passInt[8] = 0;
                }

                Log.d("tm","migrating "+id);
                Tasks.insert(passDatas, passInt, context, subtasks, subtaskdone);
            }
            return null;
        }
    }


}
