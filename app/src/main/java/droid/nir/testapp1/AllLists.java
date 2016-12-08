package droid.nir.testapp1;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import droid.nir.alarmManager.NotificationCreater;
import droid.nir.databaseHelper.Today;
import droid.nir.databaseHelper.Todolist;
import droid.nir.testapp1.noveu.Util.Log;

public class AllLists extends ActionBarActivity implements View.OnClickListener {

    Toolbar toolbar;
    SQLiteDatabase db;
    Todolist todolist;
    TextView hiddentext;
    RecyclerView recyclerView;
    Context context;
    RecyclerView.LayoutManager mLayoutManager;
    Pending_Recycler_Data pending_recycler_data;
    timecorrection timecorrection;
    int ischanged=0;
    SharedPreferences sharedPreferences;
    FloatingActionButton fab;
    toast maketext;
    String titletext ="";
    int oid=0,listno=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_lists);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.todolists);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Navigation_Fragment navfrag = (Navigation_Fragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_fragment);
        navfrag.setup((DrawerLayout) findViewById(R.id.maindrawerlayout), toolbar,2);

        todolist = new Todolist(this);
        context= this;
        hiddentext = (TextView) findViewById(R.id.hiddentext);
        recyclerView = (RecyclerView) findViewById(R.id.decision_recycler);
        mLayoutManager = new LinearLayoutManager(this);
        timecorrection = new timecorrection();

        maketext= new toast(this);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(AllLists.this);
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

        db = todolist.settingDatabase();
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

                break;
        }
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


                fab.attachToRecyclerView(recyclerView);

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

    @Override
    public boolean onContextItemSelected(MenuItem item) {

       String tempid = Integer.toString(item.getGroupId());
        int oid = Integer.parseInt(tempid.substring(1));

        int choice = item.getItemId();

        int[] passAsync = {choice , oid};
        Log.d("alllists","calling edit" +oid +" "+choice);
        AsyncContextMenu asyncContextMenu = new AsyncContextMenu();
        asyncContextMenu.execute(choice,oid);
        return super.onContextItemSelected(item);


    }

    class AsyncContextMenu extends AsyncTask<Integer,Void,Void>
    {
        int ispresent =1;
        int success =0,choice;
        @Override
        protected Void doInBackground(Integer... params) {
           oid = params[1];
            choice = params[0];

            String selectin = "_id = " + oid;
            int columno[] = new int[4];
            Cursor cursor ;

            if(params[0]==1||params[0]==2)
            {
                columno[0] = 1;
                columno[1] =3;

                cursor = todolist.select(db,0,columno,selectin,null,null,null,null);

                if(cursor.getCount()==0)
                {
                    ispresent=0;
                    publishProgress();
                }
                else {

                    while (cursor.moveToNext())
                    {
                        titletext = cursor.getString(cursor.getColumnIndex("title"));
                        listno = cursor.getInt(cursor.getColumnIndex("listno"));
                        if(params[0]==1)
                        {

                            List<String> prolist  = new ArrayList<>();

                            if (listno > 0) {
                                int reqcolum[] = {2};
                                String selecttion = " tid = "+oid;
                                Cursor tempcursor = todolist.select(db,1,reqcolum,selecttion , null,null,null,null);
                                while (tempcursor.moveToNext()) {
                                    String current = tempcursor.getString(tempcursor.getColumnIndex("listitem"));
                                    prolist.add(current);
                                }

                            } else {
                                prolist.add(prolist.size(),getResources().getString(R.string.nolistitem));
                            }


                            String list =prolist.toString();
                            // list = list.replace("[", "");
                            //list = list.replace("]" ,"");
                            list = list.replace(",", "\n");

                            String sharetext = "\n"+list;
                            Intent shar = new Intent();
                            shar.setAction(Intent.ACTION_SEND);
                            shar.setType("text/plain");
                            shar.putExtra(Intent.EXTRA_TEXT, sharetext);
                            shar.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                            shar.putExtra(Intent.EXTRA_SUBJECT,"" +titletext);

                            startActivity(Intent.createChooser(shar, getResources().getString(R.string.shareusing)));

                        }
                        else {
                            DialogFragment alertDialog = MyDialogFragment.newInstance(getResources().getString(R.string.delete)+" "+titletext,9,getResources().getString(R.string.tdeletewarn));
                            alertDialog.show(getFragmentManager(),"dialogs");

                        }

                    }

                }
            }
            else if (params[0] == 3) {
                columno[0] = 4;
                cursor = todolist.select(db,0,columno,selectin,null,null,null,null);

                if(cursor.getCount()==0)
                {
                    ispresent=0;
                    publishProgress();
                }
                else {
                    int date,month,year;
                    String todaydate = "";
                    while (cursor.moveToNext())
                        todaydate = cursor.getString(cursor.getColumnIndex("date"));

                    if (todaydate.equals("")) {
                        ispresent = 0;
                        publishProgress();
                    } else {
                        int first = todaydate.indexOf("/");
                        int second = todaydate.indexOf("/",first+1);


                        Log.d("showlist",""+first+" "+second+" ");
                        date = Integer.parseInt(todaydate.substring(0,first));
                        month = Integer.parseInt(todaydate.substring(first+1,second));
                        year = Integer.parseInt(todaydate.substring(second+1));

                        Log.d("showlist ",""+date+" ,"+month+" ,"+year);

                    }

                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            if(ispresent==0)
            {
                maketext.makeText("Sorry an error occured!Please try again");
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (choice==0){

            }
            else if(choice==1)
            {

            }
            else if (choice == 2) {

            }
        }
    }
    public void doPositiveClick() {

        int x= deletelistitem();
        if(x>0)
        {
            maketext.makeText(getResources().getString(R.string.deletionsucess));
            SharedPreferences sharedPreferences = getSharedPreferences("sharedprefs",0);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putInt("ischanged",1);
            editor.apply();


        }
        else {
            maketext.makeText(getResources().getString(R.string.deletionfailed));
        }
    }

    public void doNegativeClick() {
    }

    public int deletelistitem()
    {
        Today today = new Today(this);
        String selection=" _id = "+oid;
        int xx = todolist.delete(db, 0, selection, null);
        selection=" tid = "+oid;
        todolist.delete(db,1,selection,null);
        todolist.delete(db,2,selection,null);

        selection ="type = 2 and oid = "+oid;
        today.settingDatabase();
        today.deleterow(selection, 0, null, db);

        String x = "2";
        x = x.concat(Integer.toString(oid));
        int pendingintentid = Integer.parseInt(x);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent callintent = new Intent(this, NotificationCreater.class);
        callintent.putExtra("title", "" + titletext);
        String oiid = Integer.toString(oid);
        callintent.putExtra("oid",""+oiid);
        callintent.putExtra("text", "" + getResources().getString(R.string.thereare)+" "+listno+" "+getResources().getString(R.string.itemslist));
        String type = Integer.toString(2);
        callintent.putExtra("type",type);
        callintent.setAction("nir.droid.NEWNOTIFICATION");


        notificationManager.cancel(pendingintentid);

        String pent = Integer.toString(oid);
        pent = "333".concat(pent);
        int iid = Integer.parseInt(pent);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, iid, callintent, PendingIntent.FLAG_NO_CREATE);
        AlarmManager alarmManager= (AlarmManager) this.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);




        return xx;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_lists, menu);
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
