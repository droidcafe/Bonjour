package droid.nir.testapp1.noveu.Home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateChangedListener;

import java.util.List;

import droid.nir.databaseHelper.MainDatabase;
import droid.nir.defcon3.FirstScreen;
import droid.nir.testapp1.About;
import droid.nir.testapp1.CustomDate;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Home.Adapters.TaskAdapter;
import droid.nir.testapp1.noveu.Home.data.dataHome;
import droid.nir.testapp1.noveu.NavDrw.setNav;
import droid.nir.testapp1.noveu.Tasks.Add_minimal;
import droid.nir.testapp1.noveu.Tasks.Loaders.LoadTask;
import droid.nir.testapp1.noveu.Util.AutoRefresh;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.dB.ParentDb;
import droid.nir.testapp1.noveu.dB.Project;
import droid.nir.testapp1.noveu.dB.Tasks;
import droid.nir.testapp1.noveu.dB.initial.TaskMigrations;
import droid.nir.testapp1.noveu.dB.metaValues.dBmetaData;
import droid.nir.testapp1.noveu.recycler.ItemTouchHelperCallback;
import droid.nir.testapp1.noveu.sync.alarms.DailySyncAlarm;
import droid.nir.testapp1.noveu.welcome.Initial;

public class Home extends AppCompatActivity
        implements OnDateChangedListener, View.OnClickListener {


    public static String dateselected;
    Dialog dialog;
    SharedPreferences sharedPreferences;
    static Context context;
    static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        activity = this;

        SQLiteDatabase db = checkInitial();
        setbasics();
        if (db != null && !db.needUpgrade(dBmetaData.DATABASE_VERSION) && Import.checkTable(db, Tasks.tableNames[0])) {
            setuptasklist();
            ParentDb.getInstance(context).close();
        }

    }


    private SQLiteDatabase checkInitial() {

        sharedPreferences = getSharedPreferences(SharedKeys.prefname, 0);
        int version= sharedPreferences.getInt(SharedKeys.Version, -1);

        if (version != constants.VERSION) {
            Initial.startInitialops(this,this ,version);
        }
        SQLiteDatabase db = ParentDb.getInstance(context).returnSQl();

        boolean isAlarmSet = DailySyncAlarm.isDailySyncSet(context);
        if(!isAlarmSet)
            DailySyncAlarm.setSyncAlarmNow(context);

        return db;
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (AutoRefresh.isRefreshNeeded(this))
            runDelayedRefresh();
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
//                startActivity(new Intent(this, Add_Reminder_new.class));
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {


            case R.id.calendarselect:

                showcalendialogue();
                return true;

            case R.id.action_settings:

                break;
            case R.id.refresh:
                refresh();
                break;
            case R.id.action_help:

                startActivity(new Intent(this, About.class));

                break;
            case R.id.action_share:

                String sharetext = getResources().getString(R.string.sharetext);
                Intent shar = new Intent();
                shar.setAction(Intent.ACTION_SEND);
                shar.setType("text/plain");
                shar.putExtra(Intent.EXTRA_TEXT, sharetext);
                startActivity(Intent.createChooser(shar, getResources().getString(R.string.shareusing)));
                break;
            case R.id.action_feedback:
                String[] mailid = {"wiizzapps@gmail.com"};
                composeEmail(mailid, "FeedBack", null);
                break;
            case R.id.action_rate:

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=droid.nir.testapp1"));
                startActivity(intent);
                break;
            case R.id.action_about:

                startActivity(new Intent(this, FirstScreen.class));


                break;


        }
        return super.onOptionsItemSelected(item);
    }

    public void composeEmail(String[] addresses, String subject, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        //   intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void showhelpdialogue() {
        startActivity(new Intent(this, FirstScreen.class));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("firsttime", 0);
        editor.commit();
        //  MainActivity.this.finish();


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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recyclerView = (RecyclerView) activity.findViewById(R.id.tasklist);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        @Override
        protected List<dataHome> doInBackground(Void... params) {


            LoadTask loadTask = new LoadTask(activity, context);
            return loadTask.loadAllTasks();


        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<dataHome> data) {
            super.onPostExecute(data);

            TaskAdapter taskAdapter = new TaskAdapter(context, activity, data);
            recyclerView.setAdapter(taskAdapter);
            recyclerView.setHasFixedSize(true);

            ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(taskAdapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

      //  new ParentDb(this).returnSQl().close();
    }
}