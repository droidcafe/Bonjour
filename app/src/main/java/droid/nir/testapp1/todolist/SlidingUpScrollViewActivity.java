/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package droid.nir.testapp1.todolist;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import droid.nir.databaseHelper.Todolist;
import droid.nir.testapp1.Pending_Recycler_Data;
import droid.nir.testapp1.R;
import droid.nir.testapp1.custom_data;
import droid.nir.testapp1.timecorrection;
import droid.nir.testapp1.toast;

public class SlidingUpScrollViewActivity{}

        /*extends SlidingUpBaseActivity<ObservableScrollView> implements ObservableScrollViewCallbacks {

    SQLiteDatabase db;
    Todolist todolist;
    TextView hiddentext;
    RecyclerView recyclerView;
    Context context;
    RecyclerView.LayoutManager mLayoutManager;
    Pending_Recycler_Data pending_recycler_data;
    droid.nir.testapp1.timecorrection timecorrection;
    int ischanged=0;
    SharedPreferences sharedPreferences;
    Toolbar ltoolbar;
    toast maketext;
    String titletext ="";
    int oid=0,listno=0;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_slidingupscrollview;
    }

    @Override
    protected ObservableScrollView createScrollable() {
        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll);
        scrollView.setScrollViewCallbacks(this);
        Log.d("scrollslide", "scrollactivity");
        return scrollView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("scrollslide", "oncreate");

        ltoolbar = (Toolbar) findViewById(R.id.lapp_bar);
        setSupportActionBar(ltoolbar);
        getSupportActionBar().setTitle(R.string.todolists);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        todolist = new Todolist(this);
        context= this;
        hiddentext = (TextView) findViewById(R.id.hiddentext);
        recyclerView = (RecyclerView) findViewById(R.id.decision_recycler);
        mLayoutManager = new LinearLayoutManager(this);
        timecorrection = new timecorrection();

        maketext= new toast(this);
      //  setuprecycler();
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

        db = todolist.settingDatabase();
        AsyncGet asyncGet = new AsyncGet();
        asyncGet.execute();


    }
    class  AsyncGet extends AsyncTask<Void,Void,Void>
    {

        int ispresent =0;
        List<custom_data> arrayList = new ArrayList<>();
        @Override
        protected Void doInBackground(Void... params) {
            int[] columnno2 = {0,1,2,3,4};

            String orderby = " _id desc";
            Cursor cursor1 = todolist.select(db, 0, columnno2, null, null, null, null, orderby);

            if (cursor1.getCount() == 0) {
                ispresent =0;
                publishProgress();


            }
            else {
                ispresent=1;
                arrayList = setupcards(cursor1);
                Log.d("allllists","finished cards "+arrayList.size());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            if (ispresent == 0) {
                hiddentext.setText(R.string.nolists);
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
                //  recyclerView.setHasFixedSize(true);
                pending_recycler_data = new Pending_Recycler_Data(context ,arrayList,5);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(pending_recycler_data);



            }


        }
    }

    private List<custom_data> setupcards(Cursor cursor2) {

        List<custom_data> arrayList = new ArrayList<>();
        Log.d("checkfortoday", " inserting pending items " + cursor2.getCount());

        while(cursor2.moveToNext())
        {
            custom_data customData = new custom_data();

            customData.title = cursor2.getString(cursor2.getColumnIndex("title"));
            int notif = cursor2.getInt(cursor2.getColumnIndex("notification"));
            customData.id = cursor2.getInt(cursor2.getColumnIndex("_id"));
            customData.date = cursor2.getString(cursor2.getColumnIndex("date"));
            int listno = cursor2.getInt(cursor2.getColumnIndex("listno"));
            customData.customstatement1 = getResources().getString(R.string.youhave)+" "+listno +" "+getResources().getString(R.string.noof);
            if(notif==1)
            {
                String tempselection = " tid = ?";
                String tempargs[] = {Integer.toString(customData.id)};
                int tempcolumn[] = {2,3};
                Cursor tempcursor = todolist.select(db,2,tempcolumn,tempselection,tempargs,null,null,null);

                while(tempcursor.moveToNext()) {
                    int min = tempcursor.getInt(tempcursor.getColumnIndex("nmin"));

                    int hr = tempcursor.getInt(tempcursor.getColumnIndex("nhr"));


                    customData.time = timecorrection.formatime(hr,min);
                    customData.paddingleft =(int) context.getResources().getDimension(R.dimen.circlepaddingleft);
                }
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
                    customData.time = Character.toString('L');
                }

            }

            arrayList.add(arrayList.size(),customData);
        }

        return arrayList;

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
        },3000);

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

}*/
