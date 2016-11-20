package droid.nir.testapp1;



import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
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

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import droid.nir.databaseHelper.Todolist;


public class Add_Todo_list extends ActionBarActivity implements DialogueCreator.DialogListener, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, CompoundButton.OnCheckedChangeListener, TimePickerDialog.OnTimeSetListener {


    TextView tadd;
    TextView ppro;
    LinearLayout protextid;
    List<String> prolist;
    TodolistAdapter mAdapter;
    ListView listView;
    toast maketext;
    Switch notifyswitch;
    ArrayAdapter<String> mArrayAdapter;
    int xx,notifyswitchvalue =0,ff;
    Calendar calendar,tempcalendar;
    EditText ttitle,taddedit;
    inputManager inputManager;
    Todolist todolist;
    SQLiteDatabase db;
    FragmentTransaction ft;
    Fragment prev;
    int date,month,year;
    int oid;
    String dateofevent = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo_list2);

        getSupportActionBar().setHomeButtonEnabled(true);

        Intent passintent = getIntent();
        if(passintent!=null)
        {
            dateofevent =   passintent.getExtras().getString("customdate");
            date = passintent.getExtras().getInt("l1");
            month = passintent.getExtras().getInt("l2");
            year = passintent.getExtras().getInt("l3");
            oid = passintent.getExtras().getInt("oid");
        }

        ttitle = (EditText) findViewById(R.id.ttitle);
        taddedit = (EditText) findViewById(R.id.taddedit);

        inputManager = new inputManager(this);
        todolist = new Todolist(this);

        ppro = (TextView) findViewById(R.id.taddedit);
        tadd = (TextView) findViewById(R.id.taddbutton);
        notifyswitch = (Switch) findViewById(R.id.talarmswitch);
        protextid = (LinearLayout) findViewById(R.id.tlistlayout);
        prolist = new ArrayList<>();
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,date);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.YEAR,year);
        tempcalendar = Calendar.getInstance();
        tempcalendar.set(Calendar.DAY_OF_MONTH,date);
        tempcalendar.set(Calendar.MONTH,month);
        tempcalendar.set(Calendar.YEAR,year);
        maketext= new toast(this);

        listView = (ListView) findViewById(R.id.todolist);
        mArrayAdapter = new ArrayAdapter<String>(this,R.layout.todo_list_item, R.id.todolistitem,prolist);
        listView.setAdapter(mArrayAdapter);
        mArrayAdapter.setNotifyOnChange(true);


        notifyswitch.setChecked(false);
        notifyswitch.setOnCheckedChangeListener(this);

        tadd.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        if(oid>0)
        {
            setuplist();
        }
    }

    private void setuplist() {

        AsyncFill asyncFill = new AsyncFill();
        asyncFill.execute();
    }

    class  AsyncFill extends AsyncTask<Void,Void,Void> {

        String timetext, date, titletext;
        Cursor cursor;
        int notstatus, done, listno;
        int ispresent = 1;
        timecorrection timecorrection;
        SQLiteDatabase db;

        @Override
        protected Void doInBackground(Void... params) {

            String selection = " _id = " + oid;
            timecorrection = new timecorrection();
            int[] columnno = {1, 2, 3, 4, 5};
            db = todolist.settingDatabase();
            cursor = todolist.select(db, 0, columnno, selection, null, null, null, null);
            if (cursor.getCount() == 0) {
                ispresent = 0;
            }
            while (cursor.moveToNext()) {
                titletext = cursor.getString(cursor.getColumnIndex("title"));
                notstatus = cursor.getInt(cursor.getColumnIndex("notification"));
                listno = cursor.getInt(cursor.getColumnIndex("listno"));
                done = cursor.getInt(cursor.getColumnIndex("done"));
                date = cursor.getString(cursor.getColumnIndex("date"));

                if (notstatus == 1) {
                    //read from table 2 notificaton
                    int reqcolum[] = {2, 3};
                    String selecttion = " tid = " + oid;
                    Cursor tempcursor = todolist.select(db, 2, reqcolum, selecttion, null, null, null, null);
                    while (tempcursor.moveToNext()) {
                        int min = tempcursor.getInt(tempcursor.getColumnIndex("nmin"));
                        int hr = tempcursor.getInt(tempcursor.getColumnIndex("nhr"));

                        timetext = timecorrection.formatime(hr, min);

                    }
                }

                if (listno > 0) {
                    int reqcolum[] = {2};
                    String selecttion = " tid = " + oid;
                    Cursor tempcursor = todolist.select(db, 1, reqcolum, selecttion, null, null, null, null);
                    while (tempcursor.moveToNext()) {
                        String current = tempcursor.getString(tempcursor.getColumnIndex("listitem"));
                        prolist.add(current);
                    }

                } else {
                    prolist.add(prolist.size(), "Sorry!No list items");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(ispresent>0)
            {
                ttitle.setText(titletext);
                mArrayAdapter.notifyDataSetChanged();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add__todo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.tdone_item)
        {

           int success = getthings();
            if(success==1)
            {
               // startActivity(new Intent(this, MainActivity.class));
                SharedPreferences sharedPreferences = getSharedPreferences("sharedprefs",0);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putInt("ischanged",1);
                editor.apply();
                finish();
            }


            return  true;
        }

        if(id==R.id.home)
        // app icon in action bar clicked; goto parent activity.
        {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.taddbutton:
                String current;
                current = ppro.getText().toString();

               ppro.setText("");
                if (current.equals("")) {
                  //  Toast.makeText(this, "Add an item in the textbox", Toast.LENGTH_LONG).show();
                } else {

                    prolist.add(prolist.size(), current);
                    mArrayAdapter.notifyDataSetChanged();

                    break;
                }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        DialogFragment dialogFragment = DialogueCreator.newInstance(R.string.dialogtitle,"",R.array.todo_listitem_array);
        dialogFragment.show(getFragmentManager(), "dialog");
        xx = position;
        return true;
    }

    @Override
    public void userClicked(DialogFragment dialogFragment, int m) {

       if(m==1)
       {

           prolist.remove(xx);
           mArrayAdapter.notifyDataSetChanged();
       }

        if(m==0)
        {

           //  maketext.makeText("first one");
            showdialog();
        }
    }

    private void showdialog() {

         ft =  getFragmentManager().beginTransaction();
        prev  = getFragmentManager().findFragmentByTag("dialog");
        if(prev!=null)
        {
            ft.remove(prev);
        }
       // ft.addToBackStack(null);

        DialogFragment alertDialog = MyDialogFragment.newInstance("Enter new String",1,"uuuuuu");
        alertDialog.show(getFragmentManager(),"dialogs");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch(buttonView.getId())
        {
            case R.id.talarmswitch:
                if(isChecked)
                {
                    notifyswitchvalue = 1;
                    ff = 0;
                    TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");

                }
                else {
                    notifyswitchvalue=0;
                }

        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update(ff);
    }

    private  void update(int f)
    {
        switch(f)
        {
            case 0:
                tempcalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

                maketext.makeText("" + tempcalendar.get(Calendar.HOUR_OF_DAY) +":" + tempcalendar.get(Calendar.MINUTE));

                break;
        }
    }

    public  int getthings()
    {
        if(prolist.size()>0)
        {
            String title;
            int nooflist,notifyhr=0,notifymin=0;



                title = inputManager.getStrings(ttitle);
            if(title.equals(""))
            {
                title = prolist.get(0);
            }
            nooflist = prolist.size();

            db = todolist.settingDatabase();



                Log.d("addtodolist","pass date is custom string in getthings "+dateofevent);


            if(notifyswitchvalue==1)
            {
                notifyhr = tempcalendar.get(Calendar.HOUR_OF_DAY);
                notifymin = tempcalendar.get(Calendar.MINUTE);

            }
            int done = 0;
            String[] stringvalues = {title,dateofevent};
            int[] intvalues = {notifyswitchvalue,nooflist ,done, notifyhr,notifymin};

            Log.d("add to do list",""+notifyswitchvalue +" "+stringvalues[1]+" the date is  "+dateofevent);
            Log.d("","");
            if(oid<0)
            {
                todolist.insert(stringvalues, intvalues, db, prolist, tempcalendar);
                maketext.makeText("Insertion Successful");
                return 1;
            }

            else {

                todolist.update(db,oid,stringvalues,intvalues,prolist,tempcalendar);
                maketext.makeText("List Updated");
                return 1;
            }

        }
        else {
            maketext.makeText("Insertion Failed! Empty ToDoList");
            return 0;
        }

    }

    public void doNegativeClick() {
    }

    public void doPositiveClick(String newtext) {
      //  maketext.makeText("" + newtext);
        prolist.set(xx, newtext);
        mArrayAdapter.notifyDataSetChanged();


    }

    public void getstring(String newtext) {

       // maketext.makeText("" + newtext);
        prolist.set(xx, newtext);
        mArrayAdapter.notifyDataSetChanged();




    }
}
