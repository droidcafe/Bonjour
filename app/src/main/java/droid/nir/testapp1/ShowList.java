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


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import droid.nir.testapp1.noveu.Util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import java.util.List;

import droid.nir.alarmManager.NotificationCreater;
import droid.nir.databaseHelper.Today;
import droid.nir.databaseHelper.Todolist;

public class ShowList extends ActionBarActivity implements View.OnClickListener {

    toast maketext;
    int oid,listno;
    TextView title,notification,nottime,dateview,hiddentext,ldate;
    View view;
    ListView listView;
    Cursor cursor;
    List<String> prolist;
    Todolist todolist;
    SQLiteDatabase db;
    Today today;
    String titletext,date;
    ArrayAdapter<String> mArrayAdapter;
    ShareActionProvider mshare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.todolist);
        setContentView(R.layout.activity_show_list);

        maketext = new toast(this);
        todolist = new Todolist(this);
        today = new Today(this);

        db = todolist.settingDatabase();

        Intent showintent = getIntent();
       if(showintent!=null)
       {

           oid = showintent.getExtras().getInt("oid");

          // maketext.makeText("The passed id and oid is  "+oid);
       }
        else {
           maketext.makeText("Sorry!Please Try Again!Failed Pipe");
       }

        title = (TextView) findViewById(R.id.listtitle);
        notification= (TextView) findViewById(R.id.lnotificationn);
        nottime = (TextView) findViewById(R.id.lnottime);
        listView = (ListView) findViewById(R.id.todolist);
        dateview = (TextView) findViewById(R.id.ldateview);
        hiddentext = (TextView) findViewById(R.id.hiddentext);
        ldate = (TextView) findViewById(R.id.ldate);
        view =findViewById(R.id.firstline);
        prolist = new ArrayList<>();
        mArrayAdapter = new ArrayAdapter<String>(this,R.layout.todo_list_item, R.id.todolistitem,prolist);
        listView.setAdapter(mArrayAdapter);
        mArrayAdapter.setNotifyOnChange(true);


    }

    @Override
    protected void onResume() {
        super.onResume();

        setuplist();
    }

    private void setuplist() {

       AsyncFill asyncFill = new AsyncFill();
        asyncFill.execute();

    }

    @Override
    public void onClick(View v) {
        int id= v.getId();

        switch (id)
        {
            case R.id.fab:
                int date,month,year;
                String todaydate = ShowList.this.date;
                int first = todaydate.indexOf("/");
                int second = todaydate.indexOf("/",first+1);


                Log.d("showlist",""+first+" "+second+" ");
                date = Integer.parseInt(todaydate.substring(0,first));
                month = Integer.parseInt(todaydate.substring(first+1,second));
                year = Integer.parseInt(todaydate.substring(second+1));

                Intent goinginent3 = new Intent(this, Add_Todo_list.class);
                goinginent3.putExtra("customdate", todaydate);
                goinginent3.putExtra("l1", date);
                goinginent3.putExtra("l2", month);
                goinginent3.putExtra("l3", year);
                goinginent3.putExtra("oid",oid);

                Log.d("showlist ",""+date+" ,"+month+" ,"+year);
                startActivity(goinginent3);
                break;
        }
    }


    class  AsyncFill extends AsyncTask<Void,Void,Void>
    {

        String timetext;
        int notstatus,done;
        int ispresent= 1;
        timecorrection timecorrection;
        @Override
        protected Void doInBackground(Void... params) {

            if(prolist.size()>0)
            {
                prolist.clear();
            }
            String selection = " _id = "+oid;
            timecorrection = new timecorrection();
            int[] columnno = {1,2,3,4,5};
            cursor = todolist.select(db,0,columnno,selection , null,null,null,null);
            if(cursor.getCount()==0)
            {
                ispresent=0;
            }
            while (cursor.moveToNext())
            {
                titletext = cursor.getString(cursor.getColumnIndex("title"));
                notstatus = cursor.getInt(cursor.getColumnIndex("notification"));
                listno= cursor.getInt(cursor.getColumnIndex("listno"));
                done = cursor.getInt(cursor.getColumnIndex("done"));
                date = cursor.getString(cursor.getColumnIndex("date"));

                if(notstatus==1)
                {
                    //read from table 2 notificaton
                    int reqcolum[] = {2,3};
                    String selecttion = " tid = "+oid;
                    Cursor tempcursor = todolist.select(db,2,reqcolum,selecttion , null,null,null,null);
                    while (tempcursor.moveToNext())
                    {
                        int min = tempcursor.getInt(tempcursor.getColumnIndex("nmin"));
                        int hr = tempcursor.getInt(tempcursor.getColumnIndex("nhr"));

                        timetext =timecorrection.formatime(hr,min);

                    }
                }

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
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(ispresent==0)
            {hiddentext.setVisibility(View.VISIBLE);
                hiddentext.setText(getResources().getString(R.string.notodolist));
                title.setVisibility(View.GONE);
                notification.setVisibility(View.GONE);
                nottime.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                dateview.setVisibility(View.GONE);
                ldate.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            }
            else {
                if(titletext.equals(""))
                {
                    title.setText(getResources().getString(R.string.notitle));
                }
                else {
                    title.setText(titletext);
                }

                if(notstatus ==0)
                {
                    notification.setText(R.string.nonotificationset);
                    notification.setPadding(70, 0, 0, 0);
                    nottime.setText("");
                }
                else {
                    notification.setText(R.string.notification);
                    notification.setPadding(0,0,0,0);
                    nottime.setText(timetext);
                }

                dateview.setText(date);
                mArrayAdapter.notifyDataSetChanged();

                FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
                fab.attachToListView(listView);
                fab.setOnClickListener(ShowList.this);

               // setupshare();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_list, menu);
      //  MenuItem shar = menu.findItem(R.id.action_share);
      //  mshare = (ShareActionProvider)shar.getActionProvider();

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {

            String x = "2";
            x = x.concat(Integer.toString(oid));
            int pendingintentid = Integer.parseInt(x);

            NotificationManager notificationManager = (NotificationManager) ShowList.this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(pendingintentid);
            return true;
        }

        else if(id==R.id.action_share)
        {
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


        else if(id==R.id.action_delete)
        {
            DialogFragment alertDialog = MyDialogFragment.newInstance(getResources().getString(R.string.delete) + " "+titletext,6,getResources().getString(R.string.tdeletewarn));
            alertDialog.show(getFragmentManager(),"dialogs");
        }

        return super.onOptionsItemSelected(item);
    }




    public int deletelistitem()
    {
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
        callintent.putExtra("text", "" + getResources().getString(R.string.thereare)+" "+listno+"  "+getResources().getString(R.string.itemslist));
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

    public void doPositiveClick() {

        int x= deletelistitem();
        if(x>0)
        {
            maketext.makeText(getResources().getString(R.string.deletionsucess));
            SharedPreferences sharedPreferences = getSharedPreferences("sharedprefs",0);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putInt("ischanged",1);
            editor.apply();

            finish();
        }
        else {
            maketext.makeText(getResources().getString(R.string.deletionfailed));
        }
    }

    public void doNegativeClick() {
    }
}
