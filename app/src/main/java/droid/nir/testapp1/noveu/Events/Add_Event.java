package droid.nir.testapp1.noveu.Events;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import droid.nir.databaseHelper.Events;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Dialogue.DialogueCreator;
import droid.nir.testapp1.noveu.Util.InputManager;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.Util.TimeUtil;
import droid.nir.testapp1.noveu.constants.constants;

public class Add_Event extends AppCompatActivity implements View.OnClickListener,
        TimeUtil.TimePickerFragment.OnTimeSelectedListener,
        TimeUtil.DatePickerFragment.OnDateSelectedListener, CompoundButton.OnCheckedChangeListener, DialogueCreator.OnDialogueClickListener {

    private int f_date, f_month, f_year, f_hr, f_min;
    private int t_date, t_month, t_year, t_hr, t_min;
    private int n_hr, n_min;
    Context context;
    /**
     * mode for which type of notification.
     * {@link constants#event_notification_modes}
     * 0 - no notification
     * 1 - 10 min before
     * 2 - half an hour before
     * 3 - custom
     */
    private int notification_mode;

    /**
     * mode for selecting which field is being
     * focused now
     * 0 - fromDate
     * 1 - fromTime
     * 2 - toDate
     * 3 - toTime
     * 4 - notificationTime
     * 5 - date for whole day event
     */
    static int clickmode;

    @Bind(R.id.ewholeswitch)
    Switch wholeSwitch;
    @Bind(R.id.efromdate)
    TextView fromDate;
    @Bind(R.id.efromtime)
    TextView fromTime;
    @Bind(R.id.etodate)
    TextView toDate;
    @Bind(R.id.etotime)
    TextView toTime;
    @Bind(R.id.enotifytime)
    TextView notifyTime;
    @Bind(R.id.efromdate1)
    TextView fromDate1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__event);

        getSupportActionBar().setHomeButtonEnabled(true);

        ButterKnife.bind(this);
        setupInitial();
        getArguments();
        setupViews();

        context = this;
    }


    private void setupInitial() {
        n_hr = 0;
        n_min = 0;
        notification_mode = 0;
    }

    private void getArguments() {
        Intent passintent = getIntent();
        Calendar calendar = Calendar.getInstance();

        if (passintent != null) {
            f_date = passintent.getExtras().getInt("e1");
            f_month = passintent.getExtras().getInt("e2");
            f_year = passintent.getExtras().getInt("e3");
        } else {
            setDate(0, calendar);
        }
        setTime(1, calendar);
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        setDate(2, calendar);
        setTime(3, calendar);

        calendar.add(Calendar.HOUR_OF_DAY,-1);
        calendar.add(Calendar.MINUTE,-10);
        setTime(4,calendar);


    }

    private void setupViews() {
        findViewById(R.id.fab).setOnClickListener(this);
        fromDate.setOnClickListener(this);
        fromTime.setOnClickListener(this);
        toDate.setOnClickListener(this);
        toTime.setOnClickListener(this);
        fromDate1.setOnClickListener(this);
        notifyTime.setOnClickListener(this);

        wholeSwitch.setOnCheckedChangeListener(this);
        setDateText(0);
        setDateText(2);
        setTimeText(1);
        setTimeText(3);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                save();
                break;
            case R.id.efromdate:
                clickmode = 0;
                DialogFragment dateFragment = TimeUtil.DatePickerFragment.newInstance(f_year,f_month,f_date,this);
                dateFragment.show(getFragmentManager(), "datPicker");
                break;

            case R.id.efromtime:
                clickmode = 1;
                DialogFragment timeFragment = TimeUtil.TimePickerFragment.newInstance(f_hr,f_min,this);
                timeFragment.show(getFragmentManager(), "timePicker");
                break;
            case R.id.etodate:
                clickmode = 2;
                DialogFragment dateFragment1 = TimeUtil.DatePickerFragment.newInstance(t_year,t_month,t_date,this);
                dateFragment1.show(getFragmentManager(), "datPicker");
                break;

            case R.id.etotime:
                clickmode = 3;
                DialogFragment timeFragment1 = TimeUtil.TimePickerFragment.newInstance(t_hr,t_min,this);
                timeFragment1.show(getFragmentManager(), "timePicker");
                break;
            case R.id.enotifytime:
                DialogFragment dialogFragment = DialogueCreator.newInstance(this,
                        R.string.dialogtitle, "", R.array.notify_time_array);

                dialogFragment.show(getFragmentManager(), "dialog");

                break;
            case R.id.efromdate1:
                clickmode = 5;
                DialogFragment dateFragment2 = TimeUtil.DatePickerFragment.newInstance(f_year,f_month,f_date,this);
                dateFragment2.show(getFragmentManager(), "datPicker");
                break;

        }
    }

    private void save() {
        new AsyncSave().execute();
        finish();
    }


    private void setDateText(int choice) {
        if (choice == 0) {
            fromDate.setText(TimeUtil.getDate(f_date, f_month + 1, f_year));
        } else if (choice == 2) {
            toDate.setText(TimeUtil.getDate(t_date, t_month + 1, t_year));
        } else if (choice == 5) {
            fromDate1.setText(TimeUtil.getDate(f_date,f_month+1,f_year));
        }
    }

    private void setTimeText(int choice) {
        if (choice == 1) {
            fromTime.setText(TimeUtil.formatTime(f_hr, f_min));
        } else if (choice == 3) {
            toTime.setText(TimeUtil.formatTime(t_hr, t_min));
        } else if (choice == 4) {
            notifyTime.setText(TimeUtil.formatTime(n_hr, n_min));
        }
    }

    private void setDate(int choice, Calendar c) {
        setDate(choice, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    private void setDate(int choice, int year, int month, int day) {
        if (choice == 0 ) {
            f_year = year;
            f_month = month;
            f_date = day;
        } else if (choice == 2) {
            t_year = year;
            t_month = month;
            t_date = day;
        } else if (choice == 5) {
            f_year = year;
            f_month = month;
            f_date = day;
            t_year = year;
            t_month = month;
            t_date = day;

        }
    }

    private void setTime(int choice, Calendar c) {
        setTime(choice, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    }

    private void setTime(int choice, int hr, int min) {
        if (choice == 1) {
            f_hr = hr;
            f_min = min;
        } else if (choice == 3) {
            t_hr = hr;
            t_min = min;
        } else if (choice == 4) {
            n_hr = hr;
            n_min = min;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.ewholeswitch:
                if (isChecked) {
                    fromDate.setVisibility(View.GONE);
                    fromTime.setVisibility(View.GONE);
                    toDate.setVisibility(View.GONE);
                    toTime.setVisibility(View.GONE);
                    findViewById(R.id.etimefrom).setVisibility(View.GONE);
                    findViewById(R.id.etimeto).setVisibility(View.GONE);
                    findViewById(R.id.dateline).setVisibility(View.GONE);

                    findViewById(R.id.etimefrom1).setVisibility(View.VISIBLE);
                    findViewById(R.id.dateline1).setVisibility(View.VISIBLE);
                    fromDate1.setVisibility(View.VISIBLE);
                    setDateText(5);
                } else {
                    fromDate.setVisibility(View.VISIBLE);
                    fromTime.setVisibility(View.VISIBLE);
                    toDate.setVisibility(View.VISIBLE);
                    toTime.setVisibility(View.VISIBLE);
                    findViewById(R.id.etimefrom).setVisibility(View.VISIBLE);
                    findViewById(R.id.etimeto).setVisibility(View.VISIBLE);
                    findViewById(R.id.dateline).setVisibility(View.VISIBLE);

                    findViewById(R.id.etimefrom1).setVisibility(View.GONE);
                    findViewById(R.id.dateline1).setVisibility(View.GONE);
                    fromDate1.setVisibility(View.GONE);

                    setDateText(0);
                    setDateText(2);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add__event2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cancel_item) {
            finish();
            return true;
        }

        if (id == R.id.home) {
            this.finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void timeChanged(int hour, int min) {
        Log.d("ae", "time changed ae " + hour + " " + min);
        setTime(clickmode, hour, min);
        setTimeText(clickmode);
    }


    @Override
    public void dataChanged(int year, int month, int day) {
        Log.d("ae", "date changed ae " + year + " " + month + day);
        setDate(clickmode, year, month, day);
        setDateText(clickmode);
    }

    @Override
    public void onClick(DialogFragment dialogFragment, int m) {
        String currentinlang = (getResources().getStringArray(R.array.notify_time_array))[m];
        String current = constants.event_notification_modes[m];

        if (current.equals("Custom")) {
            DialogFragment dateFragment = TimeUtil.TimePickerFragment.newInstance(n_hr,n_min,this);
            dateFragment.show(getFragmentManager(), "datPicker");
            clickmode = 4;
            notification_mode = 3;
        } else {
            notifyTime.setText(currentinlang);
            if (current.equals("No Notification")) {
                notification_mode = 0;
                findViewById(R.id.ealarmset).setVisibility(View.GONE);
                findViewById(R.id.ealarmswitch).setVisibility(View.GONE);
            } else {
                findViewById(R.id.ealarmset).setVisibility(View.VISIBLE);
                findViewById(R.id.ealarmswitch).setVisibility(View.VISIBLE);
                if (current.equals("10 minutes before"))
                    notification_mode = 1;
                else if (current.equals("Half an hour before"))
                    notification_mode = 2;
            }
        }
    }


     class AsyncSave extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            getThings();
            return null;
        }

        /**
         * passString -  title  location  dateofevent fromdate todate  note
         *                  0       1         2           3       4     5
         * passInt - isWholeday  isnotify  isnotes done  noofdays from_timehr from_timemin to_timehr to_timemin notification_hr notification_min isalarm
         *              0           1         2      3      4          5           6           7           8         9             10              11
         * @return
         */
        private int getThings() {

            String passString[] = new String[6];
            int[] passInt = new int[12];

            passString[0] = InputManager.getStrings((EditText) findViewById(R.id.etitle));
            if (passString[0].equals("")) {
                passString[0] = getResources().getString(R.string.randomevent);
            }

            passString[2] = InputManager.getStrings((EditText) findViewById(R.id.elocedit));

            passString[3] = TimeUtil.getDate(f_date, f_month, f_year); ///fromdate
            if ( wholeSwitch.isChecked()) {
                passInt[0] =1; //iswhole day
                passString[4] = passString[3]; // todate

                f_hr = 0;
                f_min =0;
                t_hr = 23;
                t_min = 59;
            }else{
                passInt[0] = 0; //iswholeday
                passString[4] = TimeUtil.getDate(t_date, t_month, t_year); //todate
            }

            passInt[5] = f_hr;
            passInt[6] = f_min;
            passInt[7] = t_hr;
            passInt[8] = t_min;

            /** date of event = from date if no notification is present .Else it is = notification date */
            if (notification_mode != 0) {
                passInt[1] = 1;
                Calendar notification_calendar = Calendar.getInstance();
                notification_calendar = TimeUtil.setCalendar(notification_calendar,f_year,f_month,f_date);
                notification_calendar = TimeUtil.setCalendar(notification_calendar, f_hr, f_min);

                Log.d("ae","not cal "+notification_calendar.get(Calendar.DAY_OF_MONTH) +" "+notification_calendar.get(Calendar.MINUTE));
                if (notification_mode == 1) {
                    notification_calendar.add(Calendar.MINUTE,-10);
                } else if (notification_mode == 2) {
                    notification_calendar.add(Calendar.MINUTE,-30);
                } else if (notification_mode == 3) {
                    notification_calendar.set(Calendar.HOUR_OF_DAY,n_hr);
                    notification_calendar.set(Calendar.MINUTE,n_min);
                }

                passString[2] = TimeUtil.getDate(notification_calendar);
                passInt[9] = n_hr ;
                passInt[10] = n_min ;
            }else{
                passInt[1] = 0;
                passString[2] = passString[3]; /** date of event = from date*/
            }

            passString[5] = InputManager.getStrings((EditText) findViewById(R.id.enotes)); /** notes */
            if (passString[5].equals("")) {
                passInt[2] = 0; /** isnotes = 0*/
            } else {
                passInt[2] = 1; /** isnotes = 1*/
            }

            passInt[3] = 0; /** done */


            if ((f_date == t_date) && (f_month == t_month) && (f_year == t_year)) {
                passInt[4] = 0;
            } else {
                passInt[4] = (f_date - t_date) + ((f_month - t_month) * 30) + ((f_year - t_year) * 365);
            }
            if (passInt[4] < 0) {
                passInt[4] = 0;
                passString[4] = passString[3]; /** todate = fromdate  */
            }
            Log.d("ae",""+passString.toString());
            Log.d("ae",""+passInt.toString());
            Events events = new Events(context);
            SQLiteDatabase db = events.settingDatabase();
            if (db != null)
                events.insert(passString, passInt, db);

            return 1;
        }
    }


}
