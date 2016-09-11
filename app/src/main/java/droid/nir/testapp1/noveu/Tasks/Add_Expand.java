package droid.nir.testapp1.noveu.Tasks;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wnafee.vector.MorphButton;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import droid.nir.testapp1.Bonjour;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Dialogue.DialogueSelectorTasks;
import droid.nir.testapp1.noveu.Home.Home;
import droid.nir.testapp1.noveu.Tasks.Loaders.DeleteTask;
import droid.nir.testapp1.noveu.Tasks.Loaders.LoadTaskHelper;
import droid.nir.testapp1.noveu.Tasks.data.SharedData;
import droid.nir.testapp1.noveu.Tasks.data.TaskVitalData;
import droid.nir.testapp1.noveu.Util.AutoRefresh;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.dB.DBProvider;
import droid.nir.testapp1.noveu.dB.Project;
import droid.nir.testapp1.noveu.dB.Tasks;
import droid.nir.testapp1.noveu.social.share.TaskShare;

/**
 * mode values
 * 1= r
 * 3 = s
 * 5 = n
 * 4 r+s
 * 6 n+r
 * 8 s+n
 * 9 r+S+n
 * r = reminder(1,4,6,9),s =sub(3,4,8,9), n = notes(5,6,8,9)
 * <p/>
 * <p>
 * remmode values
 * <p/>
 * 1 date
 * 2 reminder
 * 4 alarm
 * 8 repeat
 * 3 date+reminder
 * 7 date +alarm+reminder
 * 11 date + reminder + repeat
 * 15 all
 * </p>
 * <p/>
 * choice 0 - from add_minimal
 * 1- from home
 */
public class Add_Expand extends AppCompatActivity
        implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>,
        TaskMore.OnFragmentInteractionListener {


    static Import anImport;
    static int repeatselected, alarmselected;
    static String dateselected;
    static int mode = 0, remmode = 0, projectid, timehr, timemin, inital_remmode;
    static Context context;
    static Activity activity;
    static String extras;
    static boolean isMoreVisible, isShared;
    static int choice, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__expand);

        SharedData.clearAll();
        if (((EditText) findViewById(R.id.new_task)).requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }


        anImport = new Import(this);
        context = this;
        activity = this;

        setupinitial();

        getArguments();
        setupviews();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("mode", mode);
        outState.putInt("remmode", remmode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mode = savedInstanceState.getInt("mode");
        remmode = savedInstanceState.getInt("remmode");
    }

    private void setupinitial() {

        mode = 0;
        remmode = 0;
        inital_remmode = 0;
        extras = "";
        choice = 0;
        id = -1;
        projectid = Project.getDefaultProject(context);
        isMoreVisible = false;
        isShared = false;
    }

    private void getArguments() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("text/")) {
                getSharedText(intent);
            }
            return;
        }
        Bundle bundle_expand = getIntent().getExtras();

        choice = bundle_expand.getInt("choice");
        if (choice == 0) {
            getArgumentsMinimal(bundle_expand);

        } else if (choice == 1) {
            id = bundle_expand.getInt("taskid");
            extras = getIntent().getStringExtra("extra");
            new AsyncLoad().execute(id);
            findViewById(R.id.moreimage).setVisibility(View.VISIBLE);

        }

    }

    private void getSharedText(Intent intent) {
        String task = intent.getStringExtra(Intent.EXTRA_TEXT);
        String task_title = intent.getStringExtra(Intent.EXTRA_SUBJECT);

        projectid = Project.getDefaultProject(context);
        getLoaderManager().initLoader(1, null, this);

        if (task_title != null) {
            ((TextView) findViewById(R.id.new_task)).setText(task_title);
            SharedData.notes = task;
            setText(R.id.notes, R.id.notesimage, 5);
            remmode = 0;
        } else {
            ((TextView) findViewById(R.id.new_task)).setText(task);
        }

        isShared = true;
    }


    private void getArgumentsMinimal(Bundle bundle_expand) {

        String task = bundle_expand.getString("task");
        if (!task.equals(""))
            ((TextView) findViewById(R.id.new_task)).setText(task);

        projectid = bundle_expand.getInt("projectid");

        getLoaderManager().initLoader(1, null, this);

        remmode = bundle_expand.getInt("mode");
        Log.d("ae", "remmode " + remmode);
        if (remmode == 1)
            remmode = 0;
        else if (remmode == 2) {
            dateselected = bundle_expand.getString("date");
            setDateText(dateselected);
            remmode = 1;

        } else if (remmode == 3) {
            dateselected = bundle_expand.getString("date");
            timehr = bundle_expand.getInt("timehr");
            timemin = bundle_expand.getInt("timemin");

            String dateText = "" + dateselected + " ," + Import.formatTime(timehr, timemin);
            setDateText(dateText);

            remmode += 2;

        }
    }

    private static void handleIntentExtra(String extras) {
        switch (extras) {
            case constants.share_task_intent_extra:
                shareTask();
        }
    }


    private void setupviews() {

        TextView proname = (TextView) findViewById(R.id.projectname);
        TextView rem = (TextView) findViewById(R.id.reminder);
        TextView subtasks = (TextView) findViewById(R.id.subtasks);
        TextView notes = (TextView) findViewById(R.id.notes);

        anImport.settypefaces("Raleway-Light.ttf", proname);
        anImport.settypefaces("Raleway-Light.ttf", rem);
        anImport.settypefaces("Raleway-Light.ttf", subtasks);
        anImport.settypefaces("Raleway-Light.ttf", notes);

        rem.setOnClickListener(this);
        subtasks.setOnClickListener(this);
        notes.setOnClickListener(this);
        proname.setOnClickListener(this);

        // findViewById(R.id.share_pic).setOnClickListener(this);
        findViewById(R.id.fab).setOnClickListener(this);
        findViewById(R.id.moreimage).setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add__expand, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.projectname:
                showProjectDialogue();
                break;

            case R.id.reminder:
                setReminder();
                break;

            case R.id.subtasks:
                setSubTasks();
                break;
            case R.id.notes:
                setNotes();
                break;

            case R.id.fab:
                saveTask();
                break;
            case R.id.moreimage:
                Log.d("ae", " more " + isMoreVisible);
                if (isMoreVisible)
                    hideMore();
                else
                    showMore();
                break;

        }
    }

    private void showMore() {
        Log.d("ae", "adding");
        ((FloatingActionButton) findViewById(R.id.fab)).show();
        findViewById(R.id.fab).setVisibility(View.GONE);
        TaskMore moreFragment = TaskMore.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_up_bottom, R.anim.fragment_slide_down_bottom,
                R.anim.fragment_slide_up_bottom, R.anim.fragment_slide_down_bottom);
        transaction.add(R.id.morelayout, moreFragment);
        transaction.addToBackStack(moreFragment.toString());

        transaction.commit();
        isMoreVisible = true;
    }

    private void hideMore() {
        Log.d("ae", "hiding");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate();

        ((FloatingActionButton) findViewById(R.id.fab)).show();

        isMoreVisible = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isMoreVisible) {
            ((FloatingActionButton) findViewById(R.id.fab)).show();
            isMoreVisible = false;
        }
    }

    private void saveTask() {

        TextView taskfield = (TextView) findViewById(R.id.new_task);
        String task = taskfield.getText().toString();

        if (!TaskUtil.isSaveableTask(task, mode)) {
            finish();
            return;
        }

        if (task.equals(""))
            task = getResources().getString(R.string.randomtask);
        new AsyncSave().execute(task);

        Log.d("ae", "choice " + choice + " id " + id);
        finish();
    }

    private static void shareTask() {

        String task = ((TextView) activity.findViewById(R.id.new_task)).getText().toString();
        TaskVitalData taskVitalData = TaskVitalData.initialise(mode, task, dateselected);
        int[] passInt = {0, TaskUtil.isTime(remmode), timehr, timemin, TaskUtil.isAlarm(remmode)};

        TaskShare taskShare = new TaskShare(passInt, Project.getProjectName(context, projectid), taskVitalData);
        Intent shareIntent = taskShare.share(context);
        context.startActivity(Intent.createChooser(shareIntent, context.getResources().getString(R.string.shareusing)));
    }

    private void deleteTask() {

        DeleteTask.setDelayedDelete(activity,id, 5000);
        finish();
    }

    private void showProjectDialogue() {

        DialogFragment dialogFragment = DialogueSelectorTasks.newInstance(2);
        dialogFragment.show(getFragmentManager(), "dialogs");
    }

    private void setNotes() {

        Log.d("ae", "note " + SharedData.notes);

        Intent notes_intent = new Intent(this, Add_Notes.class);

        Bundle bundle = new Bundle();
        bundle.putInt("mode", mode);

        notes_intent.putExtras(bundle);
        startActivityForResult(notes_intent, constants.NOTES_REQUESTCODE);
    }

    private void setSubTasks() {

        Intent subtask_intent = new Intent(this, Add_SubTasks.class);

        Bundle bundle = new Bundle();
        bundle.putInt("mode", mode);

        subtask_intent.putExtras(bundle);
        startActivityForResult(subtask_intent, constants.SUBTASK_REQUESTCODE);
    }

    private void setReminder() {

        Intent reminder_intent = new Intent(this, Add_Reminder_new.class);

        Bundle reminder_bundle = new Bundle();
        reminder_bundle.putInt("remmode", remmode);


        if (remmode == constants.permitMode[1])
            reminder_bundle.putString("dateselected", dateselected);
        else if (remmode >= constants.permitMode[2]) {
            reminder_bundle.putString("dateselected", dateselected);
            reminder_bundle.putInt("timehr", timehr);
            reminder_bundle.putInt("timemin", timemin);
            if (remmode == constants.permitMode[4] || remmode == constants.permitMode[5]) {
                reminder_bundle.putInt("repeatselected", repeatselected);
            }
//            if(remmode == constants.permitMode[4] || remmode== constants.permitMode[5])
//            {
//                reminder_bundle.putInt("alarmselected",alarmselected);
//            }

        }

        reminder_intent.putExtras(reminder_bundle);
        startActivityForResult(reminder_intent, constants.REMINDER_REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == constants.SUCCESS_CODE) {

            switch (requestCode) {
                case constants.REMINDER_REQUESTCODE:
                    reminderReturn(data);
                    break;
                case constants.SUBTASK_REQUESTCODE:
                    subTaskReturn(data);
                    break;
                case constants.NOTES_REQUESTCODE:
                    notesReturn(data);
                    break;
            }

        }
    }

    private void reminderReturn(Intent data) {

        Log.d("ae", "note " + SharedData.notes);
        Bundle bundle = data.getExtras();
        remmode = bundle.getInt("mode", constants.permitMode[0]);
        Log.d("ae", "remmode " + remmode);
        Log.d("ae", "mode " + mode);

        if (remmode == constants.permitMode[0]) {

        } else {
            dateselected = bundle.getString("dateselected");
            if (remmode == constants.permitMode[1]) {
                setDateText(dateselected);
            } else {
                timehr = bundle.getInt("timehr");
                timemin = bundle.getInt("timemin");
                String dateText = "" + dateselected + " ," + Import.formatTime(timehr, timemin);
                setDateText(dateText);

                if (remmode == constants.permitMode[4] || remmode == constants.permitMode[5])
                    alarmselected = bundle.getInt("alarmselected");
                else
                    alarmselected = -1;
                if (remmode == constants.permitMode[4] || remmode == constants.permitMode[5]) {
                    repeatselected = bundle.getInt("repeatselected");
//                    Toast.makeText(this,"repeat "+repeatselected, Toast.LENGTH_LONG);
                } else
                    repeatselected = -1;
            }
        }

        Log.d("ae", "note " + SharedData.notes);


    }

    private void notesReturn(Intent data) {
        int submode = data.getIntExtra("mode", 0);
        Log.d("ae", "return " + submode + " " + mode);
        if (mode == 5 || mode == 6 || mode == 8 || mode == 9) {
            if (submode == 0 || submode == -1) {
                invalidateText(R.id.notes, R.id.notesimage, 5);
            }
        } else {
            if (submode > 0) {
                setText(R.id.notes, R.id.notesimage, 5);
            }
        }
    }

    private void subTaskReturn(Intent data) {
        TextView subtask_size = (TextView) findViewById(R.id.subtasks_size);
        int submode = data.getIntExtra("mode", 0);
        int size = data.getIntExtra("size", -1);                             /**size is index of completed in the list*/

        size = (size == -1) ? submode : size;
        Log.d("adde", "submode " + submode + "size " + size);
        if (mode == 3 || mode == 4 || mode == 8 || mode == 9) {
            if ((submode < 1) || (submode == 1 && size == 0)) {
                invalidateText(R.id.subtasks, R.id.subtasksimage, 3);
                subtask_size.setText("");
            } else {
                subtask_size.setText("" + size);
            }


        } else {
            if ((submode == 1 && size != 0) || (submode > 1)) {
                setText(R.id.subtasks, R.id.subtasksimage, 3);
                subtask_size.setText("" + size);
            } else {
                subtask_size.setText("");
            }
        }
    }

    private static void setDateText(String dateText) {
        TextView dateview = (TextView) activity.findViewById(R.id.reminder);
        dateview.setText(dateText);
        MorphButton reminderimage = (MorphButton) activity.findViewById(R.id.reminderimage);
        anImport.changeColorState(dateview, reminderimage, R.color.taskselected_list);

        setMode(1);
        Log.d("ae", "mode " + mode);

    }

    private static void setText(int textid, int imageid, int mode) {
        TextView textView = (TextView) activity.findViewById(textid);

        MorphButton textimage = (MorphButton) activity.findViewById(imageid);
        anImport.changeColorState(textView, textimage, R.color.taskselected_list);

        setMode(mode);
        Log.d("ae", "mode " + Add_Expand.mode);

    }

    private void invalidateText(int textid, int imageid, int mode) {
        TextView textView = (TextView) findViewById(textid);
        MorphButton textimage = (MorphButton) findViewById(imageid);
        anImport.changeColorState(textView, textimage, R.color.taskpermit);

        Add_Expand.mode -= mode;
    }


    public void renameProject(String project, int id) {
        ((TextView) findViewById(R.id.projectname)).setText(project);
        projectid = id;
    }


    private static void setMode(int i) {
        if (i == 1) {
            if (!(mode == 1 || mode == 4 || mode == 6 || mode == 9))
                mode += i;

        } else if (i == 3) {
            if (!(mode == 3 || mode == 4 || mode == 8 || mode == 9))
                mode += i;
        } else if (i == 5) {
            if (!(mode == 5 || mode == 6 || mode == 8 || mode == 9))
                mode += i;
        }
    }

    @Override
    public void onMoreInteraction(Uri uri) {

        Log.d("ae", "receive " + uri);
        UriMatcher moreUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        moreUriMatcher.addURI(DBProvider.AUTHORITY, DBProvider.BASE_PATH_TASKS + "/sharetask", 0);
        moreUriMatcher.addURI(DBProvider.AUTHORITY, DBProvider.BASE_PATH_TASKS + "/deletetask", 1);

        switch (moreUriMatcher.match(uri)) {
            case 0: /** share task */
                shareTask();
                break;
            case 1: /** delete task */
                deleteTask();
                break;
            default:
                Log.d("ae", "no match " + moreUriMatcher.match(uri));
                break;

        }
    }

    /**
     * string = name , date, remdate , note
     * 0       1        2      3
     * <p></p>
     * passint = projectid, isrem, isnotes, issubtask, done, istime, timehr, timemin, isalarm,isrepeat,rmode
     * 0            1     2        3         4      5      6        7       8     9   10
     */

    public static class AsyncSave extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AutoRefresh.setRefreshSharedPref(context);
        }

        @Override
        protected Void doInBackground(String... params) {

            String[] passData = new String[4];
            int[] passInt = new int[11];
            Arrays.fill(passData, "");
            Arrays.fill(passInt, 0);

            passData[0] = params[0];

            passInt[0] = projectid;
            passInt[4] = 0; //done

            Log.d("ae", " mode " + mode + " remmode " + remmode);
            if (mode == 1 || mode == 4 || mode == 6 || mode == 9) {
                /* reminder is present*/
                passInt[1] = 1;
                passData[2] = dateselected;
                passData[1] = dateselected;
                if (remmode > 2) {
                    passInt[5] = 1;
                    passInt[6] = timehr;
                    passInt[7] = timemin;
                    if (remmode == constants.permitMode[3] || remmode == constants.permitMode[5]) //alarm present
                        passInt[8] = 1;
                    if (remmode == constants.permitMode[4] || remmode == constants.permitMode[5]) //repeat present
                        passInt[9] = 1;
                    passInt[10] = repeatselected;
                }

            } else {

                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH) + 1;
                String date = calendar.get(Calendar.DAY_OF_MONTH)
                        + "/" + month + "/" + calendar.get(Calendar.YEAR);

                passData[1] = date;

            }

            if (mode == 3 || mode == 4 || mode == 8 || mode == 9) {
                //subtast present
                passInt[3] = 1;
            }

            if (mode == 5 || mode == 6 || mode == 8 || mode == 9) {
                //notes present
                passInt[2] = 1;
                passData[3] = SharedData.notes;
            }

            Log.d("ae","pid "+passInt[0]);
            if (choice == 0)
                Tasks.insert(passData, passInt, context, SharedData.list, SharedData.subTaskdone);
            else if (choice == 1) {
                Tasks.update(passData, passInt, context, SharedData.list, SharedData.subTaskdone, id);
            }
            SharedData.clearAll();
//            if (isShared)
//                System.exit(1);
            return null;
        }
    }


    public static class AsyncLoad extends AsyncTask<Integer, Integer, Void> {

        String task, proname;

        @Override
        protected Void doInBackground(Integer... params) {

            loadTask(params[0]);
            return null;
        }

        /**
         * 1 = set task
         * 2 = set project
         * 3 = set reminder
         * 4 = set subtask
         * 5 = set notes
         */

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


            Log.d("ae", "progress " + task);
            switch (values[0]) {
                case 1:
                    ((EditText) activity.findViewById(R.id.new_task)).setText(task, TextView.BufferType.EDITABLE);
                    break;
                case 2:
                    ((TextView) activity.findViewById(R.id.projectname)).setText(proname);
                    break;
                case 3:
                    if (remmode == 1) {
                        setDateText(dateselected);
                    } else if (remmode > 2) {
                        setDateText(dateselected + " " + Import.formatTime(timehr, timemin));
                    }
                    break;
                case 4:
                    setText(R.id.subtasks, R.id.subtasksimage, 3);
                    break;
                case 5:
                    setText(R.id.notes, R.id.notesimage, 5);
                    break;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (extras != null) {
                handleIntentExtra(extras);
            }
        }

        private void loadTask(Integer id) {

            TaskVitalData taskVitalData = LoadTaskHelper.loadTasksVitals(context, id);
            if (taskVitalData != null) {
                task = taskVitalData.name;
                dateselected = taskVitalData.date;
                publishProgress(1);
                Log.d("ae", "pid " + taskVitalData.pid);
                getProject(taskVitalData.pid);
                if (taskVitalData.isrem == 1) {
                    getReminder(id);
                    inital_remmode = remmode;
                }
                if (taskVitalData.isnotes == 1)
                    getNotes(id);
                if (taskVitalData.issubtask == 1)
                    getSubTask(id);
            } else {
                /**
                 *
                 * tasks dont exists handle here
                 */
            }
        }

        private void getProject(int projectid) {

            proname = Project.getProjectName(context, projectid);
            Add_Expand.projectid = projectid;
            publishProgress(2);

        }

        private boolean getReminder(Integer id) {

            int[] reminder = LoadTaskHelper.loadReminder(context, id);
            if (reminder != null) {
                if (reminder[1] == 1) {
                    int time[] = LoadTaskHelper.loadTime(context, reminder[0]);
                    if (time != null) {
                        remmode = 3;
                        timehr = time[0];
                        timemin = time[1];

                        publishProgress(3);
                        if (time[2] == 1)
                            remmode += 4;
                        if (time[3] == 1)
                            remmode += 8;
                    } else {
                        remmode = 1;
                    }

                } else {
                    remmode = 1;
                    publishProgress(3);
                }
                return true;
            } else {
                return false;
            }
        }

        private boolean getSubTask(Integer id) {

            List[] sub = LoadTaskHelper.loadSubTasksComplete(context, id);
            List<String> subTasks = sub[0];
            if (subTasks.size() > 0) {
                SharedData.list = subTasks;
                SharedData.subTaskdone = sub[1];
                publishProgress(4);
                return true;
            }

            return false;

        }

        private boolean getNotes(int id) {

            String notes = LoadTaskHelper.loadNotes(context, id);
            if (notes != null) {
                SharedData.notes = notes;
                publishProgress(5);
                return true;
            }
            return false;
        }
    }


    /**
     * used for loading project dialogue data on clicking on project
     *
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri uri;
        //id =1 load project using projectid
        if (id == 1) {
            uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, Project.tableNames[0]);
            String selection = "_id = " + projectid;

            return new CursorLoader(this, uri,
                    new String[]{Project.columnNames[0][1]}, selection, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case 1:
                //load project using projectid
                while (data.moveToNext()) {
                    String proname = data.getString(data.getColumnIndex(Project.columnNames[0][1]));
                    ((TextView) findViewById(R.id.projectname)).setText(proname);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
