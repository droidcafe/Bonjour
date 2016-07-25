package droid.nir.testapp1;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import droid.nir.testapp1.noveu.Util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import droid.nir.databaseHelper.Remainder;
import droid.nir.databaseHelper.Today;

public class ShowRemainder extends ActionBarActivity implements View.OnClickListener {

    toast maketext;
    int oid;
    SQLiteDatabase db;

    String titletext,date,desptext,timetext;
    Cursor cursor;
    TextView title,notification,nottime,dateview,hiddentext,ldate,desp,alarm;
    View view;
    int isdesp;
    Remainder remainder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.remainder);
        setContentView(R.layout.activity_show_remainder);

        maketext = new toast(this);
        remainder = new Remainder(this);

        db = remainder.settingDatabase();
        Intent showintent = getIntent();
        if(showintent!=null)
        {

            oid = showintent.getExtras().getInt("oid");
            Log.d("oid", "oid is "+oid);

            // maketext.makeText("The passed id and oid is  "+oid);
        }
        else {
            maketext.makeText("Sorry!Please Try Again!Failed Pipe");
        }
        title = (TextView) findViewById(R.id.listtitle);
        notification= (TextView) findViewById(R.id.rnotificationn);
        nottime = (TextView) findViewById(R.id.rnottime);
        dateview = (TextView) findViewById(R.id.ldateview);
        alarm = (TextView) findViewById(R.id.ralarm);
        hiddentext = (TextView) findViewById(R.id.hiddentext);
        ldate = (TextView) findViewById(R.id.rdate);
        desp= (TextView) findViewById(R.id.desp);
        view =findViewById(R.id.secondline);



    }

    @Override
    protected void onResume() {
        super.onResume();

        setupremainder();
    }

    private void setupremainder() {

        AsyncFill asyncFill = new AsyncFill();
        asyncFill.execute();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.fab:
                int date,month,year;
                String todaydate = ShowRemainder.this.date;
                int first = todaydate.indexOf("/");
                int second = todaydate.indexOf("/",first+1);


                Log.d("showlist",""+first+" "+second+" ");
                date = Integer.parseInt(todaydate.substring(0,first));
                month = Integer.parseInt(todaydate.substring(first+1,second));
                year = Integer.parseInt(todaydate.substring(second+1));

                setupremainderdialog();

                break;
        }
    }

    private void setupremainderdialog() {

        DialogFragment alertDialog = MyDialogFragment.newInstance(getResources().getString(R.string.newreminder),10,date,oid);
        alertDialog.show(getFragmentManager(), "dialogs");
    }

    public void doPositiveClick(String[] passvalues, int[] passint) {

        if(passvalues[0].equals(""))
            maketext.makeText(getResources().getString(R.string.remtitle));
        else {

            //SQLiteDatabase db = remainder.settingDatabase();

            remainder.update(passvalues, passint, db,oid);
            rundelayedrefresh();
        }
    }

    private void rundelayedrefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setupremainder();
            }
        },2000);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedprefs",0);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putInt("ischanged", 1);
        editor.apply();
    }

    class  AsyncFill extends AsyncTask<Void,Void,Void> {


        int nothr,notmin, done;
        int ispresent = 1,isalarm;
        timecorrection timecorrection;

        @Override
        protected Void doInBackground(Void... params) {

            String selection = " _id = "+oid;
            timecorrection = new timecorrection();
            int[] columnno = {1,2,3,4,6,7,8};
            cursor = remainder.select(db,0,columnno,selection , null,null,null,null);
            if(cursor.getCount()==0)
            {
                ispresent=0;
            }
            while (cursor.moveToNext()) {

                titletext = cursor.getString(cursor.getColumnIndex("title"));
                date = cursor.getString(cursor.getColumnIndex("date"));
                nothr = cursor.getInt(cursor.getColumnIndex("nothr"));
                notmin = cursor.getInt(cursor.getColumnIndex("notmin"));
                isalarm = cursor.getInt(cursor.getColumnIndex("isalarm"));
                isdesp = cursor.getInt(cursor.getColumnIndex("isdesp"));
                timetext = timecorrection.formatime(nothr,notmin);

                if(isdesp==1)
                {
                    desptext = cursor.getString(cursor.getColumnIndex("desp"));
                }

            }

        return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if(ispresent==0)
            {hiddentext.setVisibility(View.VISIBLE);
                hiddentext.setText(R.string.noreminder);
                title.setVisibility(View.GONE);
                notification.setVisibility(View.GONE);
                nottime.setVisibility(View.GONE);
                dateview.setVisibility(View.GONE);
                ldate.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                alarm.setVisibility(View.GONE);
            }
            else{
                if(isdesp==1)
                {
                    desp.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                    desp.setText(desptext);

                }
                else
                {
                    desp.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                }

                if (isalarm == 1){
                    alarm.setText(R.string.alarmset);
                } else {
                    alarm.setText(R.string.noalarmset);
                }

                title.setText(titletext);
                nottime.setText(timetext);
                dateview.setText(date);

                FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
               // fab.attachToListView(listView);
                fab.setOnClickListener(ShowRemainder.this);
            }

        }
    }

            @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_remainder, menu);
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

       else if (id == R.id.action_share) {
            String list =""+titletext+"\n";
            if (isdesp != 0) {
                list = ""+desptext +"\n";
            }
            list = list.concat(getResources().getString(R.string.rdecidedby)+" " +timetext);
            Intent shar = new Intent();
            shar.setAction(Intent.ACTION_SEND);
            shar.setType("text/plain");
            shar.putExtra(Intent.EXTRA_TEXT, list);
            shar.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            shar.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.remainder) );

            startActivity(Intent.createChooser(shar, getResources().getString(R.string.shareusing)));
        } else if (id == R.id.action_delete) {
            DialogFragment alertDialog = MyDialogFragment.newInstance(getResources().getString(R.string.delete) +" " + titletext, 8, getResources().getString(R.string.rdeletewarn));
            alertDialog.show(getFragmentManager(), "dialogs");
        }


        return super.onOptionsItemSelected(item);
    }

    public void doPositiveClick() {

        int x= deleteremainderitem();
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

    private int deleteremainderitem() {

        String selection=" _id = "+oid;
       int xx =  remainder.delete(db,0,selection,null);

        selection ="type = 3 and oid = "+oid;
        Today today = new Today(ShowRemainder.this);
        today.settingDatabase();
        today.deleterow(selection, 0, null, db);

        return xx;

    }

    public void doNegativeClick() {
    }
}
