package droid.nir.testapp1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import droid.nir.defcon3.FirstScreen;
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

import droid.nir.databaseHelper.Pending;
import droid.nir.testapp1.noveu.NavDrw.setNav;
import droid.nir.testapp1.noveu.constants.constants;


public class AllDecisions extends ActionBarActivity implements View.OnClickListener {

    Toolbar toolbar;
    SQLiteDatabase db;
    Pending pending;
    TextView hiddentext;
    RecyclerView recyclerView;
    Context context;
    Activity activity;
    RecyclerView.LayoutManager mLayoutManager;
    Pending_Recycler_Data pending_recycler_data;
    timecorrection timecorrection;
    int ischanged=0;
    SharedPreferences sharedPreferences;
    FloatingActionButton fab;
    toast maketext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_decisions);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.pendingdecisions);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        new setNav(this,this).setupNavigation(R.id.nav_pending);
        pending = new Pending(this);
        context= this;
        activity = this;
        hiddentext = (TextView) findViewById(R.id.hiddentext);
        recyclerView = (RecyclerView) findViewById(R.id.tasklist);
        mLayoutManager = new LinearLayoutManager(this);
        timecorrection = new timecorrection();

        fab= (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(AllDecisions.this);
        maketext= new toast(this);
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

        db = pending.settingDatabase();
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
                month = calendar.get(Calendar.MONTH) + 1;
                year = calendar.get(Calendar.YEAR);
                todaydate = date + "/" + month + "/" + year;
                Intent goinginent1 = new Intent(this, Add_Pending.class);
                goinginent1.putExtra("customdate", todaydate);
                goinginent1.putExtra("p1", date);
                goinginent1.putExtra("p2", month);
                goinginent1.putExtra("p3", year);
                startActivity(goinginent1);

                break;
        }
    }

    class  AsyncGet extends AsyncTask<Void,Void,Void>
    {
        TextView alldone_title;
        TextView alldone_promo;
        ImageView alldone_pic;
        int ispresent =0;
        List<custom_data> arrayList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alldone_title = (TextView) activity.findViewById(R.id.alldone_title);
            alldone_promo = (TextView) activity.findViewById(R.id.hiddentext);
            alldone_pic = (ImageView) activity.findViewById(R.id.alldone_pic);

        }

        @Override
        protected Void doInBackground(Void... params) {
            int[] columnno2 = {0,1,14,12,13,7,8,11};

            String orderby = " _id desc";
            Cursor cursor1 = pending.select(db, 0, columnno2, null, null, null, null, orderby);

            if (cursor1.getCount() == 0) {
                ispresent =0;
                publishProgress();


            }
            else {
                ispresent=1;
                arrayList = setupcards(cursor1);
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

                pending_recycler_data = new Pending_Recycler_Data(context ,arrayList,3);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(pending_recycler_data);

            }
        }
    }

    public  List<custom_data>  setupcards(Cursor cursor1)
    {
        List<custom_data> arrayList = new ArrayList<>();
        Log.d("checkfortoday", " inserting pending items " + cursor1.getCount());
        while (cursor1.moveToNext())
        {

            custom_data customData = new custom_data();
            String statement = "";
            customData.title = cursor1.getString(cursor1.getColumnIndex("title"));
            customData.id= cursor1.getInt(cursor1.getColumnIndex("_id"));

            int min =cursor1.getInt(cursor1.getColumnIndex("timemin"));
            int hr = cursor1.getInt(cursor1.getColumnIndex("timehr"));



            customData.time = timecorrection.formatime(hr,min);
            customData.date = cursor1.getString(cursor1.getColumnIndex("date"));

           // customData.done = cursor1.getInt(cursor1.getColumnIndex("done"));

            String decisiontype = cursor1.getString(cursor1.getColumnIndex("decisiontype"));
            if(decisiontype.equals("Custom"))
            {
                String tempselection = " pid = ?";
                String tempargs[] = {Integer.toString(customData.id)};
                int customvalue = cursor1.getInt(cursor1.getColumnIndex("custom"));
                if(customvalue==1)
                {
                    int tempcolumn[] = {2};
                    Cursor tempcursor = pending.select(db,4,tempcolumn,tempselection,tempargs,null,null,null);

                    while (tempcursor.moveToNext())
                    {
                        String custom = tempcursor.getString(tempcursor.getColumnIndex("custom"));

                        statement = getResources().getString(R.string.shouldi) + " "+custom + " ?";

                    }
                }
                else
                {
                    int tempcolumn[] = {2,5,6};
                    String teselection = " _id = ?";
                    Cursor tempcursor = pending.select(db,0,tempcolumn,teselection,tempargs,null,null,null);

                    while (tempcursor.moveToNext())
                    {
                        int desp = tempcursor.getInt(tempcursor.getColumnIndex("desp"));
                        if(desp!=0)
                        {
                            int tempcolumnn[] = {2};
                            Cursor tempcursorr = pending.select(db,1,tempcolumnn,tempselection,tempargs,null,null,null);

                            while (tempcursorr.moveToNext())
                            {
                                String custom = tempcursorr.getString(tempcursorr.getColumnIndex("description"));
                                statement = custom;

                            }
                        }
                        else
                        {
                            int nocons = tempcursor.getInt(tempcursor.getColumnIndex("nocons"));
                            int nopros =tempcursor.getInt(tempcursor.getColumnIndex("nopros"));

                            if((nopros==0)&&(nocons==0))
                            {
                                statement =  getResources().getString(R.string.pnoprocon) ;

                            }
                            else if(nopros>nocons)
                            {
                                statement =  getResources().getString(R.string.pprobig) ;

                            }
                            else if(nopros<nocons)
                            {
                                statement =  getResources().getString(R.string.pconbig) ;
                            }
                            else {
                                statement =  getResources().getString(R.string.pprosame) ;
                            }
                        }
                    }
                }
            }
            else {
                String[] tempar = getResources().getStringArray(R.array.decision_arrays);
                String[] tempar1 = getResources().getStringArray(R.array.decision_arrays1);

                int yy=0;
                while((yy<tempar1.length)&&!(decisiontype.equals(tempar1[yy])))
                {
                    yy++;
                }
                if (yy==tempar1.length)
                statement = getResources().getString(R.string.shouldi) + " "+decisiontype +" ?";
                else {
                    statement = getResources().getString(R.string.shouldi) + " "+tempar[yy] +" ?";
                }
            }

            customData.customstatement1 = statement;

            arrayList.add(arrayList.size(),customData);
        }

        return  arrayList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_decisions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                refresh();
                break;
            case R.id.action_help:

                startActivity(new Intent(this, droid.nir.testapp1.About.class));

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
                String[] mailid = {constants.dev_mail};
                Import.composeEmail(activity, mailid, "FeedBack", null);
                break;
            case R.id.action_rate:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.app_uri)));
                startActivity(intent);
                break;
            case R.id.action_about:

                startActivity(new Intent(this, FirstScreen.class));
                break;

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
