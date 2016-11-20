package droid.nir.testapp1;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import droid.nir.testapp1.noveu.Util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import droid.nir.databaseHelper.Remainder;
import droid.nir.databaseHelper.Todolist;

public class    AllRemainders extends ActionBarActivity implements View.OnClickListener {

    Toolbar toolbar;
    SQLiteDatabase db;
    Remainder remainder;
    TextView hiddentext;
    RecyclerView recyclerView;
    toast maketext;
    Context context;
    RecyclerView.LayoutManager mLayoutManager;
    Pending_Recycler_Data pending_recycler_data;
    timecorrection timecorrection;
    int ischanged=0;
    SharedPreferences sharedPreferences;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_remainders);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.reminders));

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Navigation_Fragment navfrag = (Navigation_Fragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_fragment);
        navfrag.setup((DrawerLayout) findViewById(R.id.maindrawerlayout), toolbar,3);

        remainder = new Remainder(this);
        db = remainder.settingDatabase();
        context= this;
        maketext= new toast(this);
        hiddentext = (TextView) findViewById(R.id.hiddentext);
        recyclerView = (RecyclerView) findViewById(R.id.decision_recycler);
        mLayoutManager = new LinearLayoutManager(this);
        timecorrection = new timecorrection();

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(AllRemainders.this);
        setuprecycler();
    }

    @Override
    protected void onResume() {
        super.onResume();


        sharedPreferences = getSharedPreferences("sharedprefs",0);
        ischanged = sharedPreferences.getInt("ischanged", 0);

        if (ischanged == 1) {
            rundelayedrefresh();
        }
    }

    private void setuprecycler() {


        AsyncGet asyncGet = new AsyncGet();
        asyncGet.execute();
    }
    String todaydate;
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.fab:

                int date,month,year;
                Calendar calendar = Calendar.getInstance();
                date = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH) + 1;
                year = calendar.get(Calendar.YEAR);
                todaydate = date + "/" + month + "/" + year;
                setupremainderdialog();
                break;

        }

    }


    private void setupremainderdialog() {

        DialogFragment alertDialog = MyDialogFragment.newInstance(getResources().getString(R.string.rnewreminder), 7, todaydate);
        alertDialog.show(getFragmentManager(), "dialogs");
    }
    class  AsyncGet extends AsyncTask<Void,Void,Void>
    {

        int ispresent =0;
        List<custom_data> arrayList = new ArrayList<>();
        @Override
        protected Void doInBackground(Void... params) {
            int[] columnno2 = {0,1,2,3,4,5,6,7,8};

            String orderby = " _id desc";
            Cursor cursor1 = remainder.select(db, 0, columnno2, null, null, null, null, orderby);

            if (cursor1.getCount() == 0) {
                ispresent =0;
                publishProgress();


            }
            else {
                ispresent=1;
                arrayList = setupcards(cursor1);
                Log.d("allllists", "finished cards " + arrayList.size());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            if (ispresent == 0) {
                hiddentext.setText(getResources().getString(R.string.noreminders));
                recyclerView.setVisibility(View.GONE);
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(ispresent==1)
            {
                hiddentext.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                pending_recycler_data = new Pending_Recycler_Data(context ,arrayList,6);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(pending_recycler_data);


                fab.attachToRecyclerView(recyclerView);

            }
        }
    }

    private List<custom_data> setupcards(Cursor cursor2) {

        List<custom_data> arrayList = new ArrayList<>();
        Log.d("checkfortoday", " inserting pending items " + cursor2.getCount());

        while(cursor2.moveToNext()) {
            custom_data customData = new custom_data();

            customData.title = cursor2.getString(cursor2.getColumnIndex("title"));
            customData.id = cursor2.getInt(cursor2.getColumnIndex("_id"));
            customData.date = cursor2.getString(cursor2.getColumnIndex("date"));
            int isdesp = cursor2.getInt(cursor2.getColumnIndex("isdesp"));

            if(isdesp==1)
            {
                customData.customstatement1  = cursor2.getString(cursor2.getColumnIndex("desp"));
            }
            else {
                customData.customstatement1 =getResources().getString(R.string.nodesp)  ;

            }

            int hr = cursor2.getInt(cursor2.getColumnIndex("nothr"));
            int min = cursor2.getInt(cursor2.getColumnIndex("notmin"));


            customData.time =timecorrection.formatime(hr,min);
            arrayList.add(arrayList.size(),customData);
        }

        return arrayList;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_remainders, menu);
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
        else if(id==R.id.refresh)
                refresh();


        return super.onOptionsItemSelected(item);
    }

    public void doPositiveClick(String[] passvalues, int[] passint) {
        if (passvalues[0].equals(""))
            maketext.makeText(getResources().getString(R.string.remtitle));
        else {
            SQLiteDatabase db = remainder.settingDatabase();

            remainder.insert(passvalues, passint, db);
            rundelayedrefresh();
        }
    }

    public void doNegativeClick() {

    }
    private void rundelayedrefresh() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                setuprecycler();
                //maketext.makeText(getResources().getString(R.string.refreshing));
                Log.d("mainactivity", "Refreshing");
            }
        }, 3000);

        ischanged=0;
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putInt("ischanged",0);
        editor.apply();
    }

    public void refresh() {
        setuprecycler();
        ischanged=0;
        //maketext.makeText(getResources().getString(R.string.refreshing));
        Log.d("mainactivity", "Refreshing");
    }

    private void translatefab(float slideoffset)
    {
        if(fab!=null)
        {
            fab.setTranslationX(slideoffset*300);
            Log.d("alldecisions","sliding");
        }
    }

    public void ondrawerslide(float slideoffset)
    {
        translatefab(slideoffset);
        Log.d("alldecisions", "call sliding");
    }


}

