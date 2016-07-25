package droid.nir.testapp1.noveu.Tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import droid.nir.testapp1.noveu.Util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.wnafee.vector.MorphButton;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.constants.constants;

public class Add_Reminder extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.alarm)
    TextView alarm;
    @Bind(R.id.repeat)
    TextView repeat;

    int mode = 0, timehr, timemin;
    String dateselected;
    int datemode = -1, timemode = 0,repeatmode=0;
    Import anImport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__reminder);

        ButterKnife.bind(this);

        anImport = new Import(this);
        getArguments();
        setupviews();
        Log.d("ar", "" + mode + " " + datemode + " " + timemode + " " + timehr + " " + timemin);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add__reminder, menu);
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
        switch (v.getId()) {
            case R.id.date:
                setupdateoptions();
                break;

            case R.id.time:
                Log.d("addreminder", "time click");
                setuptimeoptions();
                break;
            case R.id.alarm:
                setupalarmoptions();
                break;
            case R.id.repeat:
                setuprepeatoptions();
                break;
            case R.id.fab:
                returnParent();
                break;
        }
    }

    private void returnParent() {

        Intent return_intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putInt("mode", mode);
        switch (mode) {
            case 7:
            case 15:
            case 11:
            case 3:
                bundle.putInt("timehr", timehr);
                bundle.putInt("timemin", timemin);
            case 1:
                bundle.putString("dateselected", dateselected);

        }
        return_intent.putExtras(bundle);
        setResult(constants.SUCCESS_CODE, return_intent);
        finish();
    }


    private void getArguments() {
        Bundle bundle_reminder = getIntent().getExtras();

        setStates(bundle_reminder);
    }

    private void setStates(Bundle bundle_reminder)
    {
        mode = bundle_reminder.getInt("remmode");
        switch (mode) {
            case 1:
                dateselected = bundle_reminder.getString("dateselected");
                DateText();
                break;
            case 3:
                dateselected = bundle_reminder.getString("dateselected");
                DateText();
                timehr = bundle_reminder.getInt("timehr");
                timemin = bundle_reminder.getInt("timemin");
                TimeText();
                break;
            case 7:
                dateselected = bundle_reminder.getString("dateselected");
                DateText();
                timehr = bundle_reminder.getInt("timehr");
                timemin = bundle_reminder.getInt("timemin");
                TimeText();
                setAlarmText(true);
                break;
            default:
                datemode = 0;

        }
    }



    private void setupviews() {

        anImport.settypefaces("Raleway-Light.ttf", date);
        anImport.settypefaces("Raleway-Light.ttf", repeat);
        anImport.settypefaces("Raleway-Light.ttf", time);
        anImport.settypefaces("Raleway-Light.ttf", alarm);


        date.setOnClickListener(this);
        findViewById(R.id.fab).setOnClickListener(this);


    }




    private void TimeText() {
        timemode = findTimeMode();
        setTimeText();

    }

    private int findTimeMode() {

        if (timehr == 9 && timemin == 0)
            return 0;
        else if (timehr == 12 && timemin == 0)
            return 1;
        else if (timehr == 18 && timemin == 0)
            return 2;
        else
            return 3;
    }

    private void setTimeText() {
        time.setText(Import.formatTime(timehr, timemin));
        anImport.changeColorState(time, (MorphButton) findViewById(R.id.timeimage), R.color.taskselected_list);

        time.setOnClickListener(this);

        anImport.changeColorState(alarm, (MorphButton) findViewById(R.id.alarmimage), R.color.taskpermit_list);
        anImport.changeColorState(repeat, (MorphButton) findViewById(R.id.repeatimage), R.color.taskpermit_list);
        alarm.setOnClickListener(Add_Reminder.this);
        repeat.setOnClickListener(Add_Reminder.this);

    }

    private void setuptimeoptions() {

        View subparent = findViewById(R.id.itime);
        final ImageView collapse = (ImageView) findViewById(R.id.hidetime);
        String[] options = getResources().getStringArray(R.array.time_array);
        final TextView[] sub = setupcommonoptions(subparent, options, collapse, timemode);
        sub[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTimeClick(sub[0], sub[1], sub[2], collapse);
            }
        });

        sub[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setUpTimeClick(sub[1], sub[0], sub[2], collapse);
            }
        });

        sub[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTimeClick(sub[2], sub[0], sub[1], collapse);
            }
        });


        collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibilty(sub[0], sub[1], sub[2], collapse, View.GONE);
            }
        });



    }

    private void setuprepeatoptions() {

        View subparent =  findViewById(R.id.irepeat);
        final ImageView collapse = (ImageView) findViewById(R.id.hiderepeat);
        String[] options = getResources().getStringArray(R.array.repeat_array);
        final TextView[] sub = setupcommonoptions(subparent, options, collapse, repeatmode);
        sub[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTimeClick(sub[0], sub[1], sub[2], collapse);
            }
        });

        sub[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //setUpRepeatClick(sub[1], sub[0], sub[2], collapse);
            }
        });

        sub[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTimeClick(sub[2], sub[0], sub[1], collapse);
            }
        });


        collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibilty(sub[0], sub[1], sub[2], collapse, View.GONE);
            }
        });


    }

    private void setUpRepeatClick()
    {

    }

    private void setUpTimeClick(TextView sub, TextView sub1, TextView sub2, ImageView collapse) {

        String timetext = sub.getText().toString();
        if (!timetext.equals(getResources().getString(R.string.custom))) {
            int index = timetext.indexOf(":");
            timehr = Integer.parseInt(timetext.substring(0, index));
            timemin = Integer.parseInt(timetext.substring(index + 1, timetext.length()));
            time.setText(timetext);

            timemode = findTimeMode();
        } else {
            timemode = 3;
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");

        }

            if (mode < 2) {
                anImport.changeColorState(time, (MorphButton) findViewById(R.id.timeimage), R.color.taskselected_list);
                anImport.changeColorState(alarm, (MorphButton) findViewById(R.id.alarmimage), R.color.taskpermit_list);
                anImport.changeColorState(repeat, (MorphButton) findViewById(R.id.repeatimage), R.color.taskpermit_list);
                alarm.setOnClickListener(Add_Reminder.this);
                repeat.setOnClickListener(Add_Reminder.this);

            }
        changeVisibilty(sub1, sub2, sub, collapse, View.GONE);

        setMode(2);
    }


    private void DateText() {
        datemode = findDateMode(dateselected);
        setDateText(dateselected);
    }

    private void setDateText(String dateselected) {

        String[] options = getResources().getStringArray(R.array.days_array);
        date.setText(options[datemode] + "  " + dateselected);
        MorphButton morphButton = (MorphButton) findViewById(R.id.reminderimage);
        anImport.changeColorState(date, morphButton, R.color.taskselected_list);
        anImport.changeColorState(time, (MorphButton) findViewById(R.id.timeimage), R.color.taskpermit_list);

        time.setOnClickListener(this);


    }


    private void setupdateoptions() {


        View subparent = findViewById(R.id.ireminder);
        final ImageView collapse = (ImageView) findViewById(R.id.hidedate);
        String[] options = getResources().getStringArray(R.array.days_array);
        final TextView[] sub = setupcommonoptions(subparent, options, collapse, datemode);


        sub[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setUpDateClick(sub[0], sub[1], sub[2], collapse);
            }
        });

        sub[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDateClick(sub[1], sub[0], sub[2], collapse);
            }
        });

        sub[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDateClick(sub[2], sub[0], sub[1], collapse);
            }
        });


        collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibilty(sub[0], sub[1], sub[2], collapse, View.GONE);
            }
        });




    }



    private void setUpDateClick(TextView sub, TextView sub1, TextView sub2, ImageView collapse) {

        datemode = findDateMode(sub);
        Calendar calendar = Calendar.getInstance();
        if (datemode == 3) {
            DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");

        } else {
            int ar[] = {0, 1, 7};
            calendar.add(Calendar.DATE, ar[datemode]);
            dateselected = getOptionDate(calendar);
            String dateText = sub.getText().toString() + "  " + dateselected;
            date.setText(dateText);
        }

        if (mode < 1)
        {
            anImport.changeColorState(date, (MorphButton) findViewById(R.id.reminderimage), R.color.taskselected_list);
            anImport.changeColorState(time, (MorphButton) findViewById(R.id.timeimage), R.color.taskpermit_list);
            time.setOnClickListener(Add_Reminder.this);
        }



        changeVisibilty(sub1, sub2, sub, collapse, View.GONE);


        setMode(1);
    }


    private void setupalarmoptions() {

        String alarmon = getApplicationContext().getResources().getString(R.string.alarmon);
        String selectedoption = alarm.getText().toString();

        if (!selectedoption.equals(alarmon)){
           setAlarmText(true);
            setMode(3);
        }
        else {
           setAlarmText(false);
            mode-=4;
        }

        Log.d("ar", "mode " + mode);
    }

    private void setAlarmText(boolean ison)
    {
        if(ison)
        {
            String alarmon = getApplicationContext().getResources().getString(R.string.alarmon);
            alarm.setText(alarmon);
        }
        else{
            String alarmof = getApplicationContext().getResources().getString(R.string.alarmoff);
            alarm.setText(alarmof);
        }

        anImport.changeColorState(alarm, (MorphButton) findViewById(R.id.alarmimage), (ison) ? R.color.taskselected_list : R.color.taskpermit_list);
    }




    private void changeVisibilty(TextView sub1, TextView sub2, TextView sub3, ImageView imageView, int visibility) {
        sub1.setVisibility(visibility);
        sub2.setVisibility(visibility);
        sub3.setVisibility(visibility);
        imageView.setVisibility(visibility);
    }


    private TextView[] setupcommonoptions(View subparent, String[] options, ImageView collapse, int mode) {
        TextView sub1 = (TextView) subparent.findViewById(R.id.sub1);
        TextView sub2 = (TextView) subparent.findViewById(R.id.sub2);
        TextView sub3 = (TextView) subparent.findViewById(R.id.sub3);


        collapse.setVisibility(View.VISIBLE);
        sub1.setVisibility(View.VISIBLE);
        sub2.setVisibility(View.VISIBLE);
        sub3.setVisibility(View.VISIBLE);

        anImport.settypefaces("Raleway-Light.ttf", sub1);
        anImport.settypefaces("Raleway-Light.ttf", sub2);
        anImport.settypefaces("Raleway-Light.ttf", sub3);

        int i = 0;

        i = preCheckText(sub1, i, mode, options);
        i = preCheckText(sub2, i, mode, options);
        i = preCheckText(sub3, i, mode, options);

        return new TextView[]{sub1, sub2, sub3};

    }


    private int preCheckText(TextView textView, int i, int mode, String[] options) {
        while (i == mode) i++;
        textView.setText(options[i++]);
        return i;
    }


    private int findDateMode(String dateselected) {

        Calendar calendar = Calendar.getInstance();
        int ar[] = {0, 1, 6};
        for (int i = 0; i < ar.length; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, ar[i]);
            if (getOptionDate(calendar).equals(dateselected))
                return i;
        }

        return 3;
    }

    private int findDateMode(TextView textView) {
        String[] options = getResources().getStringArray(R.array.days_array);
        String selectedOption = textView.getText().toString();

        int i = 0;
        while (i < 4 && !selectedOption.equals(options[i])) i++;

        return i;
    }


    private String getOptionDate(Calendar calendar) {
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return date + "/" + month + "/" + year;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d("ar","saving state");
        outState.putInt("remmode", mode);
        outState.putInt("datemode", datemode);
        outState.putInt("timemode", timemode);
        outState.putInt("timehr", timehr);
        outState.putInt("timemin", timemin);
        outState.putString("dateselected", dateselected);
        Log.d("ar", "" + mode + " " + datemode + " " + timemode + " " + timehr + " " + timemin);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        setStates(savedInstanceState);
    }

    private void restoreStates(Bundle savedInstanceState) {
    }

    private void setMode(int i) {

        if (i == 1) {
            if (mode < i) {
                mode = i;
            }
        } else if (i == 2) {
            if (mode < i)
                mode = 3;

        } else if (i == 3) {
            if (mode != 7 || mode != 15)
                mode += 4;
        } else if (i == 4) {
            if (mode < 8) {
                mode += 8;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnParent();
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int month, int dayOfMonth) {

        month = month + 1;
        dateselected = "  " + dayOfMonth + "/" + month + "/" + year;
        String datetext = getResources().getString(R.string.custom) + dateselected;
        date.setText(datetext);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        timehr = hourOfDay;
        timemin = minute;

        time.setText(Import.formatTime(timehr, timemin));
    }


}
