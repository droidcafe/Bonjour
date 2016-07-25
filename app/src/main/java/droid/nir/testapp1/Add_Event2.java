package droid.nir.testapp1;


import android.app.AlarmManager;
import android.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import droid.nir.testapp1.noveu.Util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import droid.nir.databaseHelper.Events;



public class Add_Event2 extends ActionBarActivity implements View.OnClickListener,DialogueCreator.DialogListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, CompoundButton.OnCheckedChangeListener {


    TextView fromdate,fromtime,todate,totime,from,too,notifytime,setalaram;
    EditText etitle,locedit,notes;
    private static int ff = 0;
    Switch eWholeSwitch,ealarmSwitch;
    private static final String TIME_PATTERN = "HH:mm";
    String notify_time_array[];
    private Calendar calendar,tempcalendar,tempcalendar2,tempcalendar3;
    private DateFormat dateFormat;
    toast maketext;
    private SimpleDateFormat timeFormat;

    SQLiteDatabase db;
    Events  events;
    private static Add_Event2 inst;
    Context context;
    private int dayswitchValue =0, setalarmswitchvalue =1,fromtmehr,frmtimemin,totimehr,totimemin,notifiacationhr,notifiacationmin;
    AlarmService alarmService;
    AlarmManager alarmManager;
    inputManager use;
    String dateofevent = "";
    int date,year,month;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__event2);


        getSupportActionBar().setHomeButtonEnabled(true);
        Intent passintent = getIntent();
        if(passintent!=null)
        {
            dateofevent =   passintent.getExtras().getString("customdate");
            date = passintent.getExtras().getInt("e1");
            month = passintent.getExtras().getInt("e2");
            year = passintent.getExtras().getInt("e3");

        }

        events = new Events(this);
        use = new inputManager(this);

        context = this;
        maketext = new toast(this);
        calendar = Calendar.getInstance();
        Log.d("addevent", "passing date is " + date + " " + month + " " + year);

        tempcalendar = Calendar.getInstance();
        tempcalendar2 = Calendar.getInstance();
        tempcalendar3 = Calendar.getInstance();

         calendar.set(Calendar.DAY_OF_MONTH,date);
        calendar.set(Calendar.MONTH,month-1);
        calendar.set(Calendar.YEAR,year);

        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        eWholeSwitch = (Switch) findViewById(R.id.ewholeswitch);
        ealarmSwitch= (Switch)findViewById(R.id.ealarmswitch);

        fromdate = (TextView) findViewById(R.id.efromdate);
        fromtime = (TextView) findViewById(R.id.efromtime);
        todate = (TextView) findViewById(R.id.etodate);
        totime = (TextView) findViewById(R.id.etotime);
        from = (TextView) findViewById(R.id.etimefrom);
        too= (TextView) findViewById(R.id.etimeto);
        notifytime = (TextView) findViewById(R.id.enotifytime);
        setalaram  = (TextView) findViewById(R.id.ealarmset);


        etitle = (EditText) findViewById(R.id.etitle);
        locedit = (EditText) findViewById(R.id.elocedit);
        notes = (EditText) findViewById(R.id.enotes);


        notify_time_array = getResources().getStringArray(R.array.notify_time_array1);

       // fromdate.setOnClickListener(this);
        fromtime.setOnClickListener(this);
        todate.setOnClickListener(this);
        totime.setOnClickListener(this);
        notifytime.setOnClickListener(this);




        eWholeSwitch.setChecked(false);
        ealarmSwitch.setChecked(true);
        eWholeSwitch.setOnCheckedChangeListener(this);
        ealarmSwitch.setOnCheckedChangeListener(this);
        for(int i=0;i<3;i++)
            update(i);

         tempcalendar.add(Calendar.HOUR_OF_DAY, 1);
        totime.setText(timeFormat.format(tempcalendar.getTime()));
        totimehr = tempcalendar.get(Calendar.HOUR_OF_DAY);
        totimemin = tempcalendar.get(Calendar.MINUTE);


        alarmManager =  (AlarmManager)getSystemService(ALARM_SERVICE);

        findViewById(R.id.fab).setOnClickListener(this);
    }

    public static Add_Event2 instance() {
        return inst;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add__event2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cancel_item) {
            finish();
            return true;
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


        switch (v.getId())
        {
            case R.id.efromdate:
                ff=0;
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");

                break;
            case R.id.efromtime:
                ff=1;
                TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                break;
            case R.id.etodate:
                ff=2;
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;
            case R.id.etotime:
                ff=3;
                TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                break;

            case R.id.enotifytime:
                DialogFragment dialogFragment = DialogueCreator.newInstance(R.string.dialogtitle,"",R.array.notify_time_array);

                dialogFragment.show(getFragmentManager(), "dialog");

                break;

            case R.id.fab:

                int success =0;
                success =  getThings();
                if(success==1)
                {
                    // startActivity(new Intent(this, MainActivity.class));
                    SharedPreferences sharedPreferences = getSharedPreferences("sharedprefs",0);
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putInt("ischanged",1);
                    editor.apply();
                    finish();
                }
                break;

        }
    }

    private  int getThings()
    {
        String title,location,frmdate="",tdate="",notificationchoice,note;
        int isloc,notifyvalue=1,flg=0,i,isnotes =0,pendingintentid=1234,noofdaysleft=0;
        title = use.getStrings(etitle);
        if(title.equals(""))
        {
            maketext.makeText(getResources().getString(R.string.notitle));
            etitle.requestFocus();
            use.key(etitle);
        }
        else
        {
            location = use.getStrings(locedit);
            if(location.equals(""))
            {
                isloc =0;
            }
            else {
                isloc =1;
            }

           if(dayswitchValue ==0)
           {
               frmdate = use.gettextStrings(fromdate);
            //   frmtime = use.gettextStrings(fromtime);
               tdate = use.gettextStrings(todate);
            //   ttime = use.gettextStrings(totime);

           }

            notificationchoice = use.gettextStrings(notifytime);
            i=0;
            while(i<3&&flg!=1)
            {
                if(notificationchoice.equals(notify_time_array[i]))
                {
                    notifyvalue =i;
                    flg =1;
                }
                i++;
            }
            if(flg==0)
                notifyvalue=3;



            if(notifyvalue==1)
            {
                tempcalendar3.set(tempcalendar2.get(Calendar.YEAR), tempcalendar2.get(Calendar.MONTH), tempcalendar2.get(Calendar.DATE), tempcalendar2.get(Calendar.HOUR_OF_DAY), tempcalendar2.get(Calendar.MINUTE));
                tempcalendar3.add(Calendar.MINUTE, -10);
                //maketext.akeText(""+tempcalendar3.get(Calendar.MINUTE)+ " hh "+tempcalendar3.get(Calendar.HOUR_OF_DAY));
            }
            else if(notifyvalue==2)
            {
                tempcalendar3.set(tempcalendar2.get(Calendar.YEAR), tempcalendar2.get(Calendar.MONTH), tempcalendar2.get(Calendar.DATE), tempcalendar2.get(Calendar.HOUR_OF_DAY), tempcalendar2.get(Calendar.MINUTE));

                tempcalendar3.add(Calendar.MINUTE, -30);
               // maketext.akeText(""+tempcalendar3.get(Calendar.MINUTE)+ " hh "+tempcalendar3.get(Calendar.HOUR_OF_DAY));
            }
            else if(notifyvalue==3)
            {
               // maketext.akeText(""+tempcalendar3.get(Calendar.MINUTE)+ " hh "+tempcalendar3.get(Calendar.HOUR_OF_DAY));
            }

            if(notifyvalue!=0)
            {
                notifiacationhr = tempcalendar3.get(Calendar.HOUR_OF_DAY);
                notifiacationmin = tempcalendar3.get(Calendar.MINUTE);
            }
            //notificationchoice = tempcalendar3.get(Calendar.HOUR_OF_DAY)+":"+tempcalendar3.get(Calendar.MINUTE);



            note = use.getStrings(notes);
            if(note.equals(""))
            {
                isnotes =0;
            }
            else {
                isnotes=1;
            }

            int firstdate[] = {tempcalendar.get(Calendar.DAY_OF_MONTH),tempcalendar.get(Calendar.MONTH),tempcalendar.get(Calendar.YEAR)};
            int secondate[] = {tempcalendar2.get(Calendar.DAY_OF_MONTH),tempcalendar2.get(Calendar.MONTH),tempcalendar2.get(Calendar.YEAR)};

            if((firstdate[0]==secondate[0])&&(firstdate[1]==secondate[1])&&(firstdate[2]==secondate[2]))
            {
                noofdaysleft =0;
            }
            else{

                noofdaysleft = (firstdate[0]-secondate[0]) + ((firstdate[1]-secondate[1])*30) + ((firstdate[2]-secondate[2])*365);
            }
            if(noofdaysleft<0){
                maketext.makeText(getResources().getString(R.string.invalidate));
                fromdate.requestFocus();

            }
            else
            {

                Log.d("","the date is "+dateofevent);
                    String[]  passstring = {title,location,dateofevent,frmdate,tdate ,note};
                int[] intpass = {dayswitchValue,notifyvalue,isnotes,0,noofdaysleft,fromtmehr,frmtimemin,totimehr,totimemin,notifiacationhr,notifiacationmin,setalarmswitchvalue};

                db = events.settingDatabase();
                if(db!=null)
                    events.insert(passstring, intpass, db,tempcalendar2,tempcalendar3);

                return  1;
            }


        }

        return 0;
    }


    public void setAlarmText(String alarmText) {
        fromdate.setText(alarmText);
    }

    private void update(int f) {

        switch (f)
        {
            case 0:fromdate.setText(dateFormat.format(calendar.getTime()));

                tempcalendar2.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                break;
            case 1:fromtime.setText(timeFormat.format(calendar.getTime()));
                fromtmehr = calendar.get(Calendar.HOUR_OF_DAY);
                frmtimemin = calendar.get(Calendar.MINUTE);
                tempcalendar2.set(tempcalendar2.get(Calendar.YEAR), tempcalendar2.get(Calendar.MONTH), tempcalendar2.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                break;
            case 2:todate.setText(dateFormat.format(calendar.getTime()));

                tempcalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

                break;
            case 3:
                totime.setText(timeFormat.format(calendar.getTime()));
                totimehr = calendar.get(Calendar.HOUR_OF_DAY);
                totimemin = calendar.get(Calendar.MINUTE);
                tempcalendar.set(tempcalendar.get(Calendar.YEAR), tempcalendar.get(Calendar.MONTH), tempcalendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                break;
            case 4:
                notifytime.setText(timeFormat.format(calendar.getTime()));
                tempcalendar3.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE),calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
                break;
        }


    }
    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }



    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {

        calendar.set(year, monthOfYear, dayOfMonth);
        update(ff);
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update(ff);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.ewholeswitch:
                if(isChecked)
                {
                    fromdate.setVisibility(View.GONE);
                    fromtime.setVisibility(View.GONE);
                    todate.setVisibility(View.GONE);
                    totime.setVisibility(View.GONE);
                    from.setVisibility(View.GONE);
                    too.setVisibility(View.GONE);
                    dayswitchValue =1;
                }
                else
                {
                    fromdate.setVisibility(View.VISIBLE);
                    fromtime.setVisibility(View.VISIBLE);
                    todate.setVisibility(View.VISIBLE);
                    totime.setVisibility(View.VISIBLE);
                    from.setVisibility(View.VISIBLE);
                    too.setVisibility(View.VISIBLE);
                    dayswitchValue =0;
                }
                break;
            case R.id.ealarmswitch:
                if(isChecked)
                {
                    setalarmswitchvalue =1;
                }
                else
                {
                    setalarmswitchvalue = 0;
                }
                break;
        }
    }

    @Override
    public void userClicked(DialogFragment dialogFragment, int m) {

        String currentinlang =(getResources().getStringArray(R.array.notify_time_array1))[m];
       String current = notify_time_array[m];

        if(current.equals("Custom"))
        {
            ff=4;
                TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");

        }
        else
        {
            notifytime.setText(currentinlang);
            if(current.equals("No Notification"))
            {
                setalaram.setVisibility(View.GONE);
                ealarmSwitch.setVisibility(View.GONE);
            }
            else
            {
                setalaram.setVisibility(View.VISIBLE);
                ealarmSwitch.setVisibility(View.VISIBLE);
            }
        }
    }
}
