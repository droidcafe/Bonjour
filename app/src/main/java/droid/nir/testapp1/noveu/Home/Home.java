package droid.nir.testapp1.noveu.Home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateChangedListener;

import java.util.List;

import droid.nir.testapp1.CustomDate;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Home.Adapters.TaskAdapter;
import droid.nir.testapp1.noveu.Home.data.dataHome;
import droid.nir.testapp1.noveu.NavDrw.setNav;
import droid.nir.testapp1.noveu.Tasks.Add_minimal;
import droid.nir.testapp1.noveu.Tasks.Loaders.DeleteTask;
import droid.nir.testapp1.noveu.Tasks.Loaders.LoadTask;
import droid.nir.testapp1.noveu.Tasks.TaskUtil;
import droid.nir.testapp1.noveu.Util.AutoRefresh;
import droid.nir.testapp1.noveu.Util.FirebaseUtil;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.bonjoursettings.ToolBarSettings.PrimarySettings;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.dB.ParentDb;
import droid.nir.testapp1.noveu.dB.Tasks;
import droid.nir.testapp1.noveu.dB.metaValues.dBmetaData;
import droid.nir.testapp1.noveu.recycler.TaskAdapterItemTouchHelperCallback;
import droid.nir.testapp1.noveu.sync.alarms.DailySyncAlarm;
import droid.nir.testapp1.noveu.sync.fcm.FCMUtil;
import droid.nir.testapp1.noveu.welcome.Initial;
import droid.nir.testapp1.noveu.welcome.about.About;
import droid.nir.testapp1.noveu.welcome.auth.SignIn;

public class Home extends AppCompatActivity
        implements OnDateChangedListener, View.OnClickListener {


    public static String dateselected;
    Dialog dialog;
    static Context context;
    static Activity activity;
    static FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            handleIntentExtra(bundle);
        }

        context = this;
        activity = this;

        SQLiteDatabase db = checkInitial();
        setbasics();
        if (db != null && !db.needUpgrade(dBmetaData.DATABASE_VERSION) && Import.checkTable(db, Tasks.tableNames[0])) {
            setuptasklist();
            //   ParentDb.getInstance(context).close();
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseUtil.recordScreenView(activity, "Home", mFirebaseAnalytics);

    }

    private void handleIntentExtra(Bundle bundle) {
        String from = (String) bundle.get("uplink");

        if (from != null && from.equals("fcm")) {
            Log.d("home", "intent data " + from);
            FCMUtil.handleFCMIntent(this, this, bundle);
        }
        Log.d("home","getting print");
        String printMessage = bundle.getString("snackMessage");
        if (printMessage != null) {
            Log.d("home","print "+printMessage);
            final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.homeparent);
           // Import.makeSnack(coordinatorLayout, printMessage);
            Snackbar.make(coordinatorLayout, printMessage, Snackbar.LENGTH_LONG).show();

        }

    }


    private SQLiteDatabase checkInitial() {


        int version = Import.getSharedPref(this,SharedKeys.Version);

        if (version != constants.VERSION) {
            Intent welcome_intent = new Intent(this, About.class);
            welcome_intent.putExtra("version", version);
            Log.d("ho", "continue " + version);
            startActivity(welcome_intent);
            finish();
            return null;
        }
        Log.d("home", "continuing");
        SQLiteDatabase db = continueInitial();
        return db;
    }

    private SQLiteDatabase continueInitial() {
        boolean isAlarmSet = DailySyncAlarm.isDailySyncSet(context);
        if (!isAlarmSet) {
            DailySyncAlarm.setSyncAlarmNow(context);
        }
        int db_ops_status = Import.getSharedPref(context, SharedKeys.db_ops_status);
        if (db_ops_status != constants.db_ops_status_modes[1]) {
            Initial.startDBops(this);
        }
        int signed_status = Import.getSharedPref(this, SharedKeys.user_signed_status);
        String user_name = Import.getSharedPref(SharedKeys.user_name, this);
        if (signed_status != 1 || user_name == null) {
            Intent auth_intent = new Intent(this, SignIn.class);
            startActivity(auth_intent);
            finish();
        }

        return ParentDb.getInstance(context).returnSQl();

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (AutoRefresh.isRefreshNeeded(this))
            runDelayedRefresh();
        // if(Import.getSharedPref(this,SharedKeys.delete) == 1)
        //    showDeleteSnack();
    }

    private void setuptasklist() {

        new AsyncLoad().execute();
    }


    private void setbasics() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.tasks));

        findViewById(R.id.fab).setOnClickListener(this);

        new setNav(this, this).setupNavigation(R.id.nav_tasks);


    }


    private void translatefab(float slideoffset) {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setTranslationX(slideoffset * 300);
        }
    }

    public void ondrawerslide(float slideoffset) {
        translatefab(slideoffset);
    }


    @Override
    public void onDateChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {

        int selectedday = calendarDay.getDay();
        int selectedmonth = calendarDay.getMonth() + 1;
        int selectedyr = calendarDay.getYear();
        dateselected = selectedday + "/" + selectedmonth + "/" + selectedyr;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.calendarbutton:
                if (dateselected.equals("")) {

                } else {
                    Intent gotodate = new Intent(this, CustomDate.class);
                    gotodate.putExtra("datevalue", "" + dateselected);

                    startActivity(gotodate);
                    dialog.dismiss();
                }

                break;
            case R.id.calendarcancelbutton:
                dialog.dismiss();

                break;
            case R.id.fab:
                startActivity(new Intent(this, Add_minimal.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.calendarselect:

                showcalendialogue();
                return true;

            case R.id.refresh:
                refresh();
                break;
            default:
                PrimarySettings.primarySetting(this, this, id, mFirebaseAnalytics);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == constants.SUCCESS_CODE) {
            switch (requestCode) {
                case constants.WELCOME_REQUESTCODE:
                    continueInitial();
                    break;
            }
        }
    }


    private void showcalendialogue() {

        dialog = new Dialog(this);

        dialog.setTitle(getResources().getString(R.string.choosedate));
        dialog.setContentView(R.layout.activity_trial_calendar2);

        dialog.show();

        MaterialCalendarView materialCalendarView = (MaterialCalendarView) dialog.findViewById(R.id.calendarView);
        TextView calendarbuton = (TextView) dialog.findViewById(R.id.calendarbutton);
        TextView calendarcancelbutton = (TextView) dialog.findViewById(R.id.calendarcancelbutton);

        materialCalendarView.setOnDateChangedListener(this);
        calendarbuton.setOnClickListener(this);
        calendarcancelbutton.setOnClickListener(this);
    }

    private void runDelayedRefresh() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        }, 1200);


    }

    public void refresh() {
        setuptasklist();
        AutoRefresh.setRefreshDone(this);
    }


    public static class AsyncLoad extends AsyncTask<Void, Void, List<dataHome>> {

        RecyclerView recyclerView;
        boolean safeState = true;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (activity == null || context == null) {
                safeState = false;
                return;
            }
            recyclerView = (RecyclerView) activity.findViewById(R.id.tasklist);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        @Override
        protected List<dataHome> doInBackground(Void... params) {

            if (!safeState) {
                return null;
            }
            LoadTask loadTask = new LoadTask(activity, context);
            String preselection = "" + Tasks.columnNames[0][8] + " = 0";
            loadTask.loadPreSelection(preselection, null);
            return loadTask.loadAllTasks();


        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<dataHome> data) {
            super.onPostExecute(data);

            if (!safeState || data == null) {
                return;
            }
            TextView alldone_title = (TextView) activity.findViewById(R.id.alldone_title);
            TextView alldone_promo = (TextView) activity.findViewById(R.id.hiddentext);
            ImageView alldone_pic = (ImageView) activity.findViewById(R.id.alldone_pic);


            if (data.isEmpty()) {

                Import.setBackGroundColor(context, activity, R.id.home_back, R.color.hprimary);
                Import.setBackGroundColor(context, activity, R.id.toolbar, R.color.hprimary);
                Import.setStatusBarColor(context, activity, R.color.hprimary_dark);
                recyclerView.setVisibility(View.GONE);
                Import.allDone(context, alldone_pic, alldone_title, alldone_promo);

                return;
            }
            Import.setBackGroundColor(context, activity, R.id.home_back, R.color.white);
            Import.setBackGroundColor(context, activity, R.id.toolbar, R.color.tprimary);
            Import.setStatusBarColor(context, activity, R.color.tprimary_dark);
            recyclerView.setVisibility(View.VISIBLE);
            Import.allDoneUndo(context, alldone_pic, alldone_title, alldone_promo);

            TaskAdapter taskAdapter = new TaskAdapter(context, activity, data, mFirebaseAnalytics);
            recyclerView.setAdapter(taskAdapter);
            recyclerView.setHasFixedSize(true);

            ItemTouchHelper.Callback callback = new TaskAdapterItemTouchHelperCallback(taskAdapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //  new ParentDb(this).returnSQl().close();
    }

    /**
     * function for showing snack after deleting tasks
     * with action undo. <p></p>
     * usually shown before actual deletion taken place
     *
     * @param tid - id of task to be deleted
     * @return
     */
    public boolean showDeleteSnack(final int tid) {
        if (activity == null)
            return false;

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) activity.findViewById(R.id.homeparent);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, context.getString(R.string.task_delete_successful), Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String redoMessage = activity.getResources().getString(R.string.redo_task);
                        Snackbar snackbar1 = Snackbar.make(coordinatorLayout, redoMessage, Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                        DeleteTask.safeDelete = false;

                        TaskUtil.setUndo(context, tid, 0);
                        new AsyncLoad().execute();
                    }
                });

        snackbar.show();

        return true;
    }

    /**
     * function for showing snack after deleting tasks. without undo action
     * usually shown after deletion taken place
     *
     * @return
     */
    public boolean showDeleteSnack() {
        if (activity == null)
            return false;

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) activity.findViewById(R.id.homeparent);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, context.getString(R.string.task_delete_successful), Snackbar.LENGTH_LONG);
        snackbar.show();
        return true;
    }
}