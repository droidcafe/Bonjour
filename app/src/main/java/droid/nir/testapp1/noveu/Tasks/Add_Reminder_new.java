package droid.nir.testapp1.noveu.Tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.Util.TimeUtil;
import droid.nir.testapp1.noveu.constants.constants;

/**
 * mode values
 * 1 date
 * 2 reminder
 * 4 alarm
 * 8 repeat
 * 3 date+reminder
 * 7 date +alarm+reminder
 * 11 date + reminder + repeat
 * 15 all
 */
public class Add_Reminder_new extends AppCompatActivity implements
        View.OnClickListener, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.alarm)
    TextView alarm;
    @Bind(R.id.repeat)
    TextView repeat;

    int mode, timehr, timemin;
    String dateselected;

    int dateSelected = -1, timeSelected = -1, repeatSelected = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__reminder_new);

        ButterKnife.bind(this);
//        date = (TextView) findViewById(R.id.date);
//        time = (TextView) findViewById(R.id.time);
//        alarm = (TextView) findViewById(R.id.alarm);
//        repeat = (TextView) findViewById(R.id.repeat);
        Log.d("arn","arn ");
        getArguments();
        setupviews();
    }

    /**
     * function for getting arguments from parent intent
     */
    private void getArguments() {
        Bundle bundle_reminder = getIntent().getExtras();
        setStates(bundle_reminder);
    }

    /**
     * set up the textviews and their fonts and onclicks
     */
    private void setupviews() {

        Import.settypefaces(this, "Raleway-Light.ttf", date);
        Import.settypefaces(this, "Raleway-Light.ttf", repeat);
        Import.settypefaces(this, "Raleway-Light.ttf", time);
        Import.settypefaces(this, "Raleway-Light.ttf", alarm);

        date.setOnClickListener(this);
        findViewById(R.id.fab).setOnClickListener(this);


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

    private void setupdateoptions() {

        View subparent = findViewById(R.id.ireminder);
        ImageView hidedate = (ImageView) findViewById(R.id.hidedate);
        setSubOptions(subparent, hidedate,
                Import.getStringArray(this, R.array.days_array), dateSelected, 1);

    }


    private void setuptimeoptions() {
        View subparent = findViewById(R.id.itime);
        ImageView hidedate = (ImageView) findViewById(R.id.hidetime);
        setSubOptions(subparent, hidedate,
                Import.getStringArray(this, R.array.time_array), timeSelected, 2);
    }

    private void setupalarmoptions() {
        String alarmon = getApplicationContext().getResources().getString(R.string.alarmon);
        String selectedoption = alarm.getText().toString();
        if (!selectedoption.equals(alarmon)) {
            alarm.setText(alarmon);
            Import.changeColorState(this, alarm,
                    (MorphButton) findViewById(R.id.alarmimage), R.color.taskselected_list);
            setMode(3);
        } else {
            alarm.setText(getResources().getString(R.string.alarmoff));
            Import.changeColorState(this, alarm,
                    (MorphButton) findViewById(R.id.alarmimage), R.color.taskpermit_list);
            mode -= constants.modeAdd[3];
        }
    }

    private void setuprepeatoptions() {
        View subparent = findViewById(R.id.irepeat);
        ImageView hiderepeat = (ImageView) findViewById(R.id.hiderepeat);
        setSubOptions(subparent, hiderepeat,
                Import.getStringArray(this, R.array.repeat_array), repeatSelected, 4);
    }

    /**
     * set the sub options for date,time repeat features
     *
     * @param subparent      for which view subparent is to be created
     * @param collapse       the  particular collapse button
     * @param optionArray    sub option array
     * @param selectedOption currently selected option in that feature
     * @param choice         pointer to identify which feature it is
     */

    private void setSubOptions(View subparent, final ImageView collapse,
                               String[] optionArray, int selectedOption, final int choice) {


        final TextView sub[] = new TextView[4];
        sub[0] = (TextView) subparent.findViewById(R.id.sub1);
        sub[1] = (TextView) subparent.findViewById(R.id.sub2);
        sub[2] = (TextView) subparent.findViewById(R.id.sub3);
        sub[3] = (TextView) subparent.findViewById(R.id.sub4);

        changeVisibilty(sub[0], sub[1], sub[2], sub[3], collapse, View.VISIBLE);
        changeTypeFaces(sub[0], sub[1], sub[2], sub[3]);

        for (int i = 0; i < 4; i++) {
            preCheckText(sub[i], i, selectedOption, optionArray[i]);
            setOnClick(sub[i], sub, collapse, i, choice);
        }
        setOnClick(collapse, sub, collapse, 4, choice);


    }

    private void setOnClick(final View view, final TextView sub[], final ImageView collapse,
                            final int option, final int choice) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (option < 4) setSubClick((TextView) view, option, choice);
                changeVisibilty(sub[0], sub[1], sub[2], sub[3], collapse, View.GONE);
            }
        });
    }


    private void setSubClick(TextView sub, int subClickedOption, int choice) {
        switch (choice) {
            case 1:
                setDateClick(sub, subClickedOption);
                break;
            case 2:
                setTimeClicks(sub, subClickedOption);
                break;
            case 4:
                setRepeatClicks(sub, subClickedOption);
        }
    }


    private void setDateClick(TextView sub, int dateClickedOption) {
        dateSelected = dateClickedOption;
        Calendar calendar = Calendar.getInstance();

        if (dateClickedOption == 3) {
            DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");

        } else {
            int ar[] = {0, 1, 7};
            calendar.add(Calendar.DATE, ar[dateClickedOption]);
            dateselected = TimeUtil.getDate(calendar);
            String dateText = sub.getText().toString() + "  " + dateselected;
            date.setText(dateText);
        }

        dateClickFollower(mode);
        setMode(1);
    }

    private void dateClickFollower(int mode) {
        if (mode < constants.permitMode[1]) {
            Import.changeColorState(this, date,
                    (MorphButton) findViewById(R.id.reminderimage), R.color.taskselected_list);
            Import.changeColorState(this, time,
                    (MorphButton) findViewById(R.id.timeimage), R.color.taskpermit_list);
            time.setOnClickListener(this);
        }
    }


    private void setTimeClicks(TextView sub, int subClickedOption) {
        timeSelected = subClickedOption;
        if (subClickedOption == 3) {
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");

        } else {
            String timetext = sub.getText().toString();
            int index = timetext.indexOf(":");
            timehr = Integer.parseInt(timetext.substring(0, index));
            timemin = Integer.parseInt(timetext.substring(index + 1, timetext.length()));
            time.setText(timetext);

        }
        timeClickFollower(mode);
        setMode(2);
    }

    private void timeClickFollower(int mode) {
        if (mode < constants.permitMode[2]) {
            Import.changeColorState(this, time,
                    (MorphButton) findViewById(R.id.timeimage), R.color.taskselected_list);
            Import.changeColorState(this, alarm,
                    (MorphButton) findViewById(R.id.alarmimage), R.color.taskpermit_list);
            Import.changeColorState(this, repeat,
                    (MorphButton) findViewById(R.id.repeatimage), R.color.taskpermit_list);

            alarm.setOnClickListener(Add_Reminder_new.this);
            repeat.setOnClickListener(Add_Reminder_new.this);

        }
    }

    private void setRepeatClicks(TextView sub, int repatClickedOption) {
        repeatSelected = repatClickedOption;
        setMode(4);
        repeat.setText(sub.getText().toString());
        Import.changeColorState(this, repeat,
                (MorphButton) findViewById(R.id.repeatimage), R.color.taskselected_list);
    }

    private void returnParent() {
        Log.d("adn", "mode is " + mode);
        Intent return_intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("mode", mode);
//        if (mode >= constants.permitMode[1]) {
//            bundle.putString("dateselected", dateselected);
//            if (mode >= constants.permitMode[2]) {
//                bundle.putInt("timehr", timehr);
//                bundle.putInt("timemin", timemin);
//            }
//            if (mode == constants.permitMode[4] || mode == constants.permitMode[5]) {
//                bundle.putInt("repeatselected", repeatSelected);
//            }
//
//        }


        switch (mode) {
            case 15:
            case 11:
                bundle.putInt("repeatselected", repeatSelected);
            case 7:
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



    /**
     * helper function for setting the various textviews, suboptions and color modes of
     * the different features of reminder using bundle passed from parent
     * when the view first loads
     * @param bundle_reminder the bundle passed from parent view
     */
    private void setStates(Bundle bundle_reminder) {
        mode = bundle_reminder.getInt("remmode");
        Log.d("arn","mode "+mode);
        //   Toast.makeText(this,mode,Toast.LENGTH_LONG)'
        if (mode >= constants.permitMode[1]) {
            dateselected = bundle_reminder.getString("dateselected");
            dateSelected = getDate();
        }
        if (mode >= constants.permitMode[2]) {
            timehr = bundle_reminder.getInt("timehr");
            timemin = bundle_reminder.getInt("timemin");
            timeSelected = getTime();
        }
        if (mode == constants.permitMode[3] || mode == constants.permitMode[5]) {
            getAlarm();
        }
        if (mode == constants.permitMode[4] || mode == constants.permitMode[5]) {
            repeatSelected = bundle_reminder.getInt("repeatselected");
            Log.d("arn","repeatselected "+repeatSelected);
            getRepeat();
        }

    }

    /**
     * for finding out which mode a selected date is which has been
     * passsed through the intent
     * <p/>
     * checks if date is today,tommorow or custom
     * also set the date text view appropriately, calls the date follower for
     * setting the color and clickability of rest features
     *
     * @return the mode of date to be used internally
     */
    private int getDate() {
        int add[] = {0, 1, 7};
        int i;
        for (i = 0; i < 3; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, add[i]);
            if (dateselected.equals(TimeUtil.getDate(calendar)))
                break;
        }
        date.setText(Import.getStringArray(this, R.array.days_array)[i] + " " + dateselected);
        dateClickFollower(0);
        return i;
    }

    /**
     * for finding out which mode a selected time is which has been
     * passsed through the intent
     * <p/>
     * checks if time is predefined one or custom one
     * also set the time text view appropriately and call time click follower
     * for setting rest of the views
     *
     * @return the mode of time to be used internally
     */
    private int getTime() {
        int i;
        String timee = TimeUtil.formatTime(timehr, timemin);
        time.setText(timee);
        String options[] = Import.getStringArray(this, R.array.time_array);
        for (i = 0; i < 3; i++) {
            if (timee.equals(options[i]))
                break;
        }
        time.setText(timee);
        timeClickFollower(constants.permitMode[1]);
        return i;
    }

    /**
     * for setting the alarm text currently  if alarm is on
     */

    private void getAlarm() {
        String alarmon = getApplicationContext().getResources().getString(R.string.alarmon);
        alarm.setText(alarmon);
        Import.changeColorState(this, alarm,
                (MorphButton) findViewById(R.id.alarmimage), R.color.taskselected_list);
    }

    /**
     * for setting the repeat textview according to the repeat
     * option selected and also for changing the color state of textview
     */
    private void getRepeat() {
        String[] repeatArray = Import.getStringArray(this, R.array.repeat_array);
        repeat.setText(repeatArray[repeatSelected]);
        Import.changeColorState(this, repeat,
                (MorphButton) findViewById(R.id.repeatimage), R.color.taskselected_list);
    }

    /**
     * set correct mode value on changing an option of reminder
     * @param i which function has been changed
     *          1 - date
     *          2- reminder
     *          3 - alarm
     *          4 - repeat
     */
    private void setMode(int i) {

        if (i == 1) {
            if (mode < constants.permitMode[1]) {
                mode = constants.permitMode[1];
            }
        } else if (i == 2) {
            if (mode < constants.permitMode[2])
                mode = constants.permitMode[2];

        } else if (i == 3) {
            if (mode != constants.permitMode[3] || mode != constants.permitMode[5])
                mode += constants.modeAdd[3];
        } else if (i == 4) {
            if (mode < constants.permitMode[4]) {
                mode += constants.modeAdd[4];
            }
        }
    }


    /**
     * checks if the particular textview is one selected ,set the correct color of the text view
     * and also the text of the view
     *
     * @param sub            the particular text view in question
     * @param iterator       the index of the text view in the list
     * @param selectedOption the option currently selected
     * @param option         the text to be set for the view
     */
    void preCheckText(TextView sub, int iterator, int selectedOption, String option) {
        if (iterator == selectedOption) {
            Import.changeColorState(this, sub, null, R.color.taskselected_list);
        } else {
            Import.changeColorState(this, sub, null, R.color.taskpermit);
        }
        sub.setText(option);
    }

    /**
     * change the visibility of the params views to the given visibility
     *
     * @param sub1       the first text option
     * @param sub2       the second text option
     * @param sub3       the third text option
     * @param sub4       the fourth text option
     * @param imageView  the collape button
     * @param visibility visiblity mode
     */
    private void changeVisibilty(TextView sub1, TextView sub2, TextView sub3, TextView sub4,
                                 ImageView imageView, int visibility) {
        sub1.setVisibility(visibility);
        sub2.setVisibility(visibility);
        sub3.setVisibility(visibility);
        sub4.setVisibility(visibility);
        imageView.setVisibility(visibility);
    }

    /**
     * change the fonts of the params views to the given visibility
     *
     * @param sub1 the first text option
     * @param sub2 the second text option
     * @param sub3 the third text option
     * @param sub4 the fourth text option
     */

    private void changeTypeFaces(TextView sub1, TextView sub2, TextView sub3, TextView sub4) {
        Import.settypefaces(this, "Raleway-Light.ttf", sub1);
        Import.settypefaces(this, "Raleway-Light.ttf", sub2);
        Import.settypefaces(this, "Raleway-Light.ttf", sub3);
        Import.settypefaces(this, "Raleway-Light.ttf", sub4);

    }


    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int month, int day) {
        month = month + 1;
        dateselected = "  " + day + "/" + month + "/" + year;
        String datetext = Import.getString(this, R.string.custom) + dateselected;
        date.setText(datetext);
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        timehr = hourOfDay;
        timemin = minute;

        time.setText(Import.formatTime(timehr, timemin));
    }




}
