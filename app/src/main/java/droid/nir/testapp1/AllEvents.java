package droid.nir.testapp1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import droid.nir.defcon3.FirstScreen;
import droid.nir.testapp1.noveu.Events.Add_Event;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import droid.nir.databaseHelper.Events;
import droid.nir.databaseHelper.Todolist;
import droid.nir.testapp1.noveu.NavDrw.setNav;
import droid.nir.testapp1.noveu.bonjoursettings.ToolBarSettings.PrimarySettings;
import droid.nir.testapp1.noveu.constants.constants;

public class AllEvents extends ActionBarActivity implements View.OnClickListener {

    Toolbar toolbar;
    SQLiteDatabase db;
    Events events;
    TextView hiddentext;
    RecyclerView recyclerView;
    Context context;
    Activity activity;
    RecyclerView.LayoutManager mLayoutManager;
    Pending_Recycler_Data pending_recycler_data;
    timecorrection timecorrection;
    int ischanged=0;
    SharedPreferences sharedPreferences;
    toast maketext;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.events);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupNav();

        events = new Events(this);
        context= this;
        activity= this;
        hiddentext = (TextView) findViewById(R.id.hiddentext);
        recyclerView = (RecyclerView) findViewById(R.id.tasklist);
        mLayoutManager = new LinearLayoutManager(this);
        timecorrection = new timecorrection();

        maketext= new toast(this);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(AllEvents.this);
        setuprecycler();

    }

    private void setupNav() {
        new setNav(this,this).setupNavigation(R.id.nav_event);
    }

    @Override
    protected void onResume() {
        super.onResume();


        sharedPreferences = getSharedPreferences("sharedprefs", 0);
        ischanged = sharedPreferences.getInt("ischanged", 0);

        if (ischanged == 1) {
            rundelayedrefresh();
        }
    }
    private void setuprecycler() {

        db = events.settingDatabase();
        AsyncGet asyncGet = new AsyncGet();
        asyncGet.execute();


    }

    @Override
    public void onClick(View v) {
        int id= v.getId();

        switch (id)
        {
            case R.id.fab:

                int date,month,year;
                String todaydate;
                Calendar calendar = Calendar.getInstance();
                date = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH) ;
                year = calendar.get(Calendar.YEAR);
                todaydate = date + "/" + month + "/" + year;
                Intent goinginent2 = new Intent(this, Add_Event.class);
                goinginent2.putExtra("customdate", todaydate);
                goinginent2.putExtra("e1", date);
                goinginent2.putExtra("e2", month);
                goinginent2.putExtra("e3", year);
                startActivity(goinginent2);
                break;
        }

    }

    class  AsyncGet extends AsyncTask<Void,Void,Void>  {

        TextView alldone_title;
        TextView alldone_promo;
        ImageView alldone_pic;
        int ispresent =0;
        List<custom_data2> arrayList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alldone_title = (TextView) activity.findViewById(R.id.alldone_title);
            alldone_promo = (TextView) activity.findViewById(R.id.hiddentext);
            alldone_pic = (ImageView) activity.findViewById(R.id.alldone_pic);

        }

        @Override
        protected Void doInBackground(Void... params) {
            int[] columno = {0,1,3,4,6,7,9};

            String orderby = " _id desc";
            Cursor cursor1 = events.select(db, 0, columno, null, null, null, null, orderby);

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
                Import.setBackGroundColor(context, activity, R.id.home_back, R.color.tsecondary);
                recyclerView.setVisibility(View.GONE);
                Import.allDone(context, alldone_pic, alldone_title, alldone_promo);
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(ispresent==1)
            {
                Import.setBackGroundColor(context,activity,R.id.home_back,R.color.white);
                recyclerView.setVisibility(View.VISIBLE);
                Import.allDoneUndo(context, alldone_pic, alldone_title, alldone_promo);

                pending_recycler_data = new Pending_Recycler_Data(context ,arrayList,4,-1);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(pending_recycler_data);




            }
        }
    }

    private List<custom_data2> setupcards(Cursor cursor) {

        int iswholeday=0;
        List<custom_data2> arrayList = new ArrayList<>();
        Log.d("checkfort1oday", " inserting event items " + cursor.getCount());
        while(cursor.moveToNext())
        {

            custom_data2 customData = new custom_data2();


            customData.id = cursor.getInt(cursor.getColumnIndex("_id"));
            customData.title = cursor.getString(cursor.getColumnIndex("title"));
            customData.date = cursor.getString(cursor.getColumnIndex("fromdate"));
            customData.done = cursor.getInt(cursor.getColumnIndex("done"));

            int notify = cursor.getInt(cursor.getColumnIndex("notify"));
            iswholeday = cursor.getInt(cursor.getColumnIndex("wholeday"));


            String tempselection = " eid = ?";
            String tempargs[] = {Integer.toString(customData.id)};
            if(iswholeday!=1)
            {
                customData.temp1 =0;
                int tempcolumno[] = {2,3,4,5,6,7};
                Cursor tempcursor = events.select(db,1,tempcolumno,tempselection,tempargs,null,null,null);
                while(tempcursor.moveToNext())
                {
                    customData.customstatement1 =tempcursor.getString(tempcursor.getColumnIndex("fromdate"));
                    customData.customstatement2 =tempcursor.getString(tempcursor.getColumnIndex("todate"));
                    int  frommin = tempcursor.getInt(tempcursor.getColumnIndex("fromtimemin"));
                    int fromhr = tempcursor.getInt(tempcursor.getColumnIndex("fromtimehr"));

                    customData.customstatement3 =timecorrection.formatime(fromhr,frommin);


                   int tomin = tempcursor.getInt(tempcursor.getColumnIndex("totimemin"));
                    int tohr  = tempcursor.getInt(tempcursor.getColumnIndex("totimehr"));

                    customData.customstatement4 = timecorrection.formatime(tohr,tomin);



                }

            }
            else {
                customData.customstatement1 = getResources().getString(R.string.ewholeday);
                customData.temp1 =1;
            }

            if(notify>0)
            {
                notify =1;

                int tempcolumno[] = {2,3,4};
                Cursor tempcursor = events.select(db,2,tempcolumno,tempselection,tempargs,null,null,null);

                while(tempcursor.moveToNext())
                {


                    int min=tempcursor.getInt(tempcursor.getColumnIndex("timemin"));

                    int hr = tempcursor.getInt(tempcursor.getColumnIndex("timehr"));


                    customData.time = timecorrection.formatime(hr,min);

                }
                customData.paddingleft =(int) context.getResources().getDimension(R.dimen.circlepaddingleft);;
            }
            else {
                customData.paddingleft = (int) context.getResources().getDimension(R.dimen.circlepaddingleft2);
                if(!customData.title.equals(""))
                {
                    char ch = customData.title.charAt(0);
                    ch = Character.toUpperCase(ch);
                    customData.time = Character.toString(ch);
                }
                else {
                    customData.time = Character.toString('E');
                }
            }


            arrayList.add(arrayList.size(),customData);
        }

        return arrayList;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        maketext.makeText("You have selected " + item.getItemId());
        return super.onContextItemSelected(item);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.refresh:
                refresh();
                break;
            default:
                PrimarySettings.primarySetting(this, this, id);
        }
        return super.onOptionsItemSelected(item);
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
