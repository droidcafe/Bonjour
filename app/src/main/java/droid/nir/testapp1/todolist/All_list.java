package droid.nir.testapp1.todolist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import droid.nir.databaseHelper.Todolist;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.todolist.adapters.Alladapters;
import droid.nir.testapp1.todolist.adapters.CustomLinearLayoutManager;
import droid.nir.testapp1.todolist.adapters.custom_Data;

public class All_list extends ActionBarActivity implements View.OnClickListener {
    Toolbar toolbar;
    Todolist todolist;
    Context context;
    SQLiteDatabase db;
    String todaydate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

      /*  toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.todolists);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Navigation_Fragment navfrag = (Navigation_Fragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_fragment);
        navfrag.setup((DrawerLayout) findViewById(R.id.maindrawerlayout), toolbar, 10);
*/
        context=  this;
        todolist = new Todolist(this);
       // (findViewById(R.id.fab)).setOnClickListener(this);
        db = todolist.settingDatabase();

        Calendar calendar = Calendar.getInstance();

        int month = calendar.get(Calendar.MONTH) + 1;
        todaydate = calendar.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + calendar.get(Calendar.YEAR);

        setuptodayrecycler();
    }

    @Override
    protected void onResume() {


        super.onResume();

    }

    private void setuptodayrecycler()
    {

        new AsyncTasks().execute();
    }

    public  class AsyncTasks extends AsyncTask<Void,Void,Void>
    {

        int ispresent;
        List<custom_Data> arrayList ;
        RecyclerView todaytask;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ispresent =0;
            arrayList = new ArrayList<>();
            todaytask = (RecyclerView)findViewById(R.id.today_tasks);
        }

        @Override
        protected Void doInBackground(Void... params) {
            int[] columnno2 = {0,1,3};

            String orderby = " _id desc";

            String selection  = "date = ? ";
            String tempselection[] = {todaydate};
            Cursor cursor1=  todolist.select(db, 0, columnno2, selection, tempselection, null, null, orderby);;
           /* if(params[0]!=null)
            {
                String selection  = "date = ? ";
                String tempselection[] = {todaydate};
               cursor1=  todolist.select(db, 0, columnno2, selection, tempselection, null, null, orderby);
            }
            else
            {

                cursor1=  todolist.select(db, 0, columnno2,null, null, null, null, orderby);
            }*/

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

        private List<custom_Data> setupcards(Cursor cursor2) {

            List<custom_Data> arrayList = new ArrayList<>();
            Log.d("checkfortoday", " inserting pending items " + cursor2.getCount());

            while(cursor2.moveToNext()) {

                custom_Data tempdata = new custom_Data();
                tempdata.tasktitle = cursor2.getString(cursor2.getColumnIndex("title"));

                arrayList.add(arrayList.size(), tempdata);
            }

        return  arrayList;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            if (ispresent == 0) {
                ((TextView)findViewById(R.id.hiddentext)).setText(R.string.notask);
                todaytask.setVisibility(View.GONE);
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(ispresent==1)
            {

                ((TextView)findViewById(R.id.hiddentext)).setVisibility(View.GONE);
                todaytask.setVisibility(View.VISIBLE);
                //  todaytask.setHasFixedSize(true);

                Alladapters alladapters = new Alladapters(context, arrayList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(All_list.this);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false);

                CustomLinearLayoutManager lm= new CustomLinearLayoutManager(context);
                lm.setOrientation(LinearLayoutManager.VERTICAL);
               // lm.setHasFixedSize(true);
                todaytask.setLayoutManager(linearLayoutManager);
                todaytask.setAdapter(alladapters);

                todaytask.setHasFixedSize(true);
              //  ((TextView)findViewById(R.id.yestertext)).addRu
               // fab.attachToRecyclerView(recyclerView);

            }


        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_list, menu);
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

        switch (v.getId())
        {
            case R.id.fab:
                int date,month,year;
                String todaydate;
                Calendar calendar = Calendar.getInstance();
                date = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH) + 1;
                year = calendar.get(Calendar.YEAR);
                todaydate = date + "/" + month + "/" + year;

                break;
        }
    }
}
