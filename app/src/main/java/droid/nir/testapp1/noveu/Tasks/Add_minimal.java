package droid.nir.testapp1.noveu.Tasks;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import droid.nir.testapp1.noveu.Util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Dialogue.DialogueSelectorTasks;
import droid.nir.testapp1.noveu.Tasks.data.SharedData;
import droid.nir.testapp1.noveu.Util.AutoRefresh;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.dB.Project;
import droid.nir.testapp1.noveu.dB.Tasks;

public class Add_minimal extends AppCompatActivity implements View.OnClickListener, TextWatcher, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Import anImport;
    @Bind(R.id.new_task)EditText newtask;
    @Bind(R.id.date) View date;
    @Bind(R.id.time) View time;

   static String dateselected;
   static int timehr,timemin,mode,projectid;
    static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_minimal);

        context = this;
        setHeight();
        ButterKnife.bind(this);

        anImport = new Import(this);
        getArguments();


        newtask.addTextChangedListener(this);
        setOnClicks();
        mode=0;
    }

    private void getArguments() {

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            projectid = bundle.getInt("proid");
        }
        else{
            projectid = 2;
        }

        TextView projectName = (TextView) findViewById(R.id.proname);
        projectName.setText(Project.getProjectName(context,projectid));
        anImport.settypefaces("Raleway-SemiBold.ttf", projectName);
    }


    private void setHeight() {

        final RelativeLayout view = (RelativeLayout) findViewById(R.id.scrolllayout);
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int height = findViewById(R.id.scroll2).getHeight();
                Log.d("addmin", "height is " + height);
                //   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,height);
                //since relative view is inside scroll view
                ScrollView.LayoutParams layoutParams = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, height);
                view.setLayoutParams(layoutParams);
                ViewTreeObserver obs = view.getViewTreeObserver();

                obs.removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    private void setOnClicks() {

        date.findViewById(R.id.today_pic).setOnClickListener(this);
        date.findViewById(R.id.tommrw_pic).setOnClickListener(this);
        date.findViewById(R.id.week_pic).setOnClickListener(this);
        date.findViewById(R.id.custom_pic).setOnClickListener(this);

        time.findViewById(R.id.t9_pic).setOnClickListener(this);
        time.findViewById(R.id.t12_pic).setOnClickListener(this);
        time.findViewById(R.id.t18_pic).setOnClickListener(this);
        time.findViewById(R.id.tcustom_pic).setOnClickListener(this);

        findViewById(R.id.proname).setOnClickListener(this);

        findViewById(R.id.add_task).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_minimal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_expand) {
            gotoExpandView();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void gotoExpandView() {

        Intent intent_expand= new Intent(this, Add_Expand.class);

        Bundle bundle_expand = new Bundle();

        bundle_expand.putInt("choice",0);
        bundle_expand.putInt("projectid", projectid);
        bundle_expand.putString("task",""+((TextView)findViewById(R.id.new_task)).getText().toString());
        bundle_expand.putInt("mode",mode);

        if(mode==2)
            bundle_expand.putString("date",dateselected);
        else if(mode==3)
        {
            bundle_expand.putString("date",dateselected);
            bundle_expand.putInt("timehr",timehr);
            bundle_expand.putInt("timemin",timemin);

        }
        intent_expand.putExtras(bundle_expand);
        startActivity(intent_expand);

        finish();
    }

    private void savetask() {
        TextView taskfield = (TextView) findViewById(R.id.new_task);
        String task = taskfield.getText().toString();

        Log.d("am", "" + mode);
        if(!TaskUtil.isSaveableTask(task,mode))
        {
            finish();
            return;
        }
        if(taskfield.equals(" "))
            task=getResources().getString(R.string.randomtask);
        new AsyncSave().execute(task);
       finish();
           
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.proname:
                showProjectDialogue();
                break;

            case R.id.add_task:
                savetask();
                break;
            case R.id.today_pic:
                dateClick(0);
                break;
            case R.id.tommrw_pic:
                dateClick(1);
                break;
            case R.id.week_pic:
                dateClick(2);
                break;

            case R.id.custom_pic:
                dateClick(3);
                break;
            case R.id.t9_pic:
                timeClick(0);
                break;
            case R.id.t12_pic:
                timeClick(1);
                break;
            case R.id.t18_pic:
                timeClick(2);
                break;
            case R.id.tcustom_pic:
                timeClick(3);
                break;

        }
    }

    private void showProjectDialogue() {

        DialogFragment dialogFragment =  DialogueSelectorTasks.newInstance(1);
        dialogFragment.show(getFragmentManager(), "dialogs");
    }

    private void timeClick(int i) {

        setfontTime();
            switch (i)
            {
                case 0:
                    timehr=9;
                    timemin=0;
                    break;
                case 1:
                    timehr=12;
                    timemin=0;
                    break;
                case 2:
                    timehr=18;
                    timemin=0;
                    break;
                case 3:
                    Calendar calendar = Calendar.getInstance();
                    TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                    return;

            }
        mode=3;
        savetask();

    }



    private void dateClick(int option)
    {
        Log.d("minimal", "before font");

        date.setVisibility(View.GONE);
        time.setVisibility(View.VISIBLE);
        mode=2;
        setfontTime();
        Calendar  calendar = Calendar.getInstance();

        switch (option)
        {
            case 1:
                calendar.add(Calendar.DATE,1);
                 break;
            case 2:
                calendar.add(Calendar.DATE,7);
                break;
            case 3:
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;
        }
        int day =  calendar.get(Calendar.DAY_OF_MONTH);
        int month =calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        dateselected = day+ "/" + month+ "/" + year;
        Log.d("minimal", "" + dateselected);
    }

    private void setfontDate()
    {
        Log.d("minimal", "font");
        anImport.settypefaces("Raleway-Light.ttf", (TextView) date.findViewById(R.id.today_text));
        anImport.settypefaces("Raleway-Light.ttf", (TextView) date.findViewById(R.id.tmrw_text));
        anImport.settypefaces("Raleway-Light.ttf", (TextView) date.findViewById(R.id.week_text));
        anImport.settypefaces("Raleway-Light.ttf", (TextView) date.findViewById(R.id.custom_text));
    }

    private void setfontTime()
    {
        Log.d("minimal", "font");
        anImport.settypefaces("Raleway-Light.ttf", (TextView) time.findViewById(R.id.today_text));
        anImport.settypefaces("Raleway-Light.ttf", (TextView) time.findViewById(R.id.tmrw_text));
        anImport.settypefaces("Raleway-Light.ttf", (TextView) time.findViewById(R.id.week_text));
        anImport.settypefaces("Raleway-Light.ttf", (TextView) time.findViewById(R.id.custom_text));

        anImport.settypefaces("Raleway-Light.ttf", (TextView) time.findViewById(R.id.t9_pic));
        anImport.settypefaces("Raleway-Light.ttf", (TextView) time.findViewById(R.id.t18_pic));
        anImport.settypefaces("Raleway-Light.ttf", (TextView) time.findViewById(R.id.t12_pic));

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d("addmin", "before");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

      //  Log.d("addmin","textchange"+s+" "+start+" "+before+" "+count);
    }

    @Override
    public void afterTextChanged(Editable s) {

        int visibility = s.toString().equals("")?View.GONE:View.VISIBLE;
        if(visibility==View.VISIBLE){

            if(mode==0)
            {
              //  Log.d("addmin","shownng date");
                findViewById(R.id.date_card).setVisibility(visibility);
                date.setVisibility(visibility);
                setfontDate();
                mode=1;
            }
        }
        else {
            Log.d("addmin","to hide "+mode);
            if(mode==1)
            {
                Log.d("addmin","hiding date");
                findViewById(R.id.date_card).setVisibility(visibility);
                date.setVisibility(visibility);
            }
            else if(mode==2||mode==3)
            {
                Log.d("addmin","hiding time");
                time.setVisibility(visibility);
                findViewById(R.id.date_card).setVisibility(visibility);

            }
            mode=0;
        }
    }

    public void renameProject(String project,int id)
    {
        ((TextView)findViewById(R.id.proname)).setText(project);
        projectid = id;
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear = monthOfYear+1;
        dateselected = dayOfMonth+"/"+monthOfYear+"/"+year;
        Log.d("addmin",""+dateselected);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        timehr = hourOfDay;
        timemin = minute;

        mode =3;
        savetask();

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d("addmin", "save instancestate "+mode);
        outState.putString("proname", ((TextView) findViewById(R.id.proname)).getText().toString());
        outState.putInt("mode", mode);
        outState.putInt("proid",projectid);
        //SavedInstanceValues savedInstanceValues = new SavedInstanceValues();
        //outState.putParcelable("savedInstance",savedInstanceValues);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        //SavedInstanceValues savedInstanceValues = inState.getParcelable("savedInstance");

        restoreStates(inState);
    }

    private void    restoreStates(Bundle savedInstanceState) {
        ((TextView)findViewById(R.id.proname)).setText(savedInstanceState.getString("proname"));
        projectid = savedInstanceState.getInt("proid");
        mode = savedInstanceState.getInt("mode");
        Log.d("addmin", "restore instancestate "+mode);

        if(mode==1)
        {
           date.setVisibility(View.VISIBLE);
            ((TextView)date.findViewById(R.id.reminder)).setText(getResources().getText(R.string.dateselect));
            setfontDate();
        }
        else if(mode==3||mode==2)
        {
            date.setVisibility(View.GONE);
            time.setVisibility(View.VISIBLE);
            ((TextView)time.findViewById(R.id.reminder)).setText(getResources().getText(R.string.timeselect));
            setfontTime();
        }
    }

    private static class AsyncSave extends AsyncTask<String,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AutoRefresh.setRefreshSharedPref(context);
        }

        @Override
        protected Void doInBackground(String... params) {

            Log.d("addmin"," save "+params[0] +" "+mode+" "+dateselected+" "+timehr+" "+timemin+" ");
        //    Tasks tasks = new Tasks(context);
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH) +1;
            String date= calendar.get(Calendar.DAY_OF_MONTH) +"/"+month+"/"+calendar.get(Calendar.YEAR);

            String[] passData;
            int[] passInt;
            if(mode<2)
            {
                passData = new String[]{params[0],date};
                passInt=new int[]{projectid,0,0,0,0};

            }
            else {
                passData = new String[]{params[0],dateselected,dateselected};
                if(mode==2)
                    passInt = new int[]{projectid,1,0,0,0,0};
                else
                    passInt = new int[]{projectid,1,0,0,0,1,timehr,timemin,0,0};
            }

            Tasks.insert(passData,passInt,context, null,null);
            return null;
        }
    }

   /* public class SavedInstanceValues implements Parcelable{

        public   final Parcelable.Creator<SavedInstanceValues> CREATOR = new Parcelable.Creator() {

            @Override
            public SavedInstanceValues createFromParcel(Parcel source) {
                Log.d("addmin","create from parcel");


                return new SavedInstanceValues(source);
            }

            @Override
            public SavedInstanceValues[] newArray(int size) {
                Log.d("addmin","new array");
                return new SavedInstanceValues[size];
            }
        };
        private SavedInstanceValues()
        {
            Log.d("addmin","empty constructr");
        }

        private SavedInstanceValues(Parcel in)
        {
            Log.d("addmin","demasking parcel");
            ((TextView)findViewById(R.id.proname)).setText(in.readString());
            projectid = in.readInt();
            mode = in.readInt();
        }
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            Log.d("addmin","writing to parcel");
            dest.writeString( ((TextView)findViewById(R.id.proname)).getText().toString());
            dest.writeInt(projectid);
            dest.writeInt(mode);

        }


    }
    */


}
