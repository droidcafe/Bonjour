package droid.nir.testapp1;


import android.app.DialogFragment;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;



import droid.nir.testapp1.noveu.Util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;



import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;


import org.w3c.dom.Text;

import java.util.ArrayList;

import java.util.Calendar;

import java.util.List;

import droid.nir.databaseHelper.DatabaseCreater;
import droid.nir.databaseHelper.Pending;


public class Add_Pending extends ActionBarActivity implements DialogueCreator.DialogListener, View.OnClickListener, TimePicker.OnTimeChangedListener, TimePickerDialog.OnTimeSetListener {


    LinearLayout protextid,contextid, addmin,phonelayout,weblayout,maillayout,pricelayout,addresslayout,faxlayout,shopnamelayout,selecttimelayout;

    Switch aSwitch;
    TextView pwhy ,importancetext,timetext,remaindertext,decisiontext,extratext,selecttime;
    EditText pother,pcon,ppro,ptitle,pdesp,ptimemin;
    Button addpro,addcon;
    List<String> prolist,conlist,extralist;
    int switchValue,timeSelected,decisionType,customUsed,extrasUsed,isextraUsed,importance,despUsed;
    inputManager use;
    toast toast;
    String decisionchoice;
    int extrasArray[],xx=0,notificationtype = 0 ;
    Calendar calendar,tempcalendar;
    Pending pending;
    String dateofevent="",todothing="";
    SQLiteDatabase db;
    int f =0,year,date,month;

    DatabaseCreater databaseCreater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pending);

      //  getSupportActionBar().setIcon(R.drawable.ic_action_accept);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent passintent = getIntent();
        if(passintent!=null)
        {
            dateofevent =   passintent.getExtras().getString("customdate");
            date = passintent.getExtras().getInt("e1");
            month = passintent.getExtras().getInt("e2");
            year = passintent.getExtras().getInt("e3");

        }


        toast = new toast(this);
        use = new inputManager(this);
        calendar = Calendar.getInstance();
        tempcalendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH,date);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.YEAR,year);

        tempcalendar.set(Calendar.DAY_OF_MONTH,date);
        tempcalendar.set(Calendar.MONTH,month);
        tempcalendar.set(Calendar.YEAR,year);

         databaseCreater = new DatabaseCreater(this);

        importancetext = (TextView) findViewById(R.id.pimportancespinner);
        timetext = (TextView) findViewById(R.id.ptimespinner);
        remaindertext = (TextView) findViewById(R.id.preminderspinner);
        decisiontext = (TextView) findViewById(R.id.pdecisionspinner);
        extratext = (TextView) findViewById(R.id.pextrasspinner);
       selecttime = (TextView) findViewById(R.id.pselcttime);

        ImageView more = (ImageView)findViewById(R.id.more);
        ImageView less = (ImageView) findViewById(R.id.less);
        importancetext.setOnClickListener(this);
        timetext.setOnClickListener(this);
        remaindertext.setOnClickListener(this);
        decisiontext.setOnClickListener(this);
        extratext.setOnClickListener(this);
        more.setOnClickListener(this);
        less.setOnClickListener(this);

        ImageView more2 = (ImageView)findViewById(R.id.more2);
        ImageView less2 = (ImageView) findViewById(R.id.less2);
        more2.setOnClickListener(this);
        less2.setOnClickListener(this);


        ImageView more3 = (ImageView)findViewById(R.id.more3);
        ImageView less3 = (ImageView) findViewById(R.id.less3);
        more3.setOnClickListener(this);
        less3.setOnClickListener(this);

        ImageView more4 = (ImageView)findViewById(R.id.more4);
        ImageView less4 = (ImageView) findViewById(R.id.less4);
        more4.setOnClickListener(this);
        less4.setOnClickListener(this);


        addmin = (LinearLayout) findViewById(R.id.addmin);
        selecttimelayout = (LinearLayout) findViewById(R.id.pselectedtime);
        contextid = (LinearLayout) findViewById(R.id.constextid);
        protextid = (LinearLayout) findViewById(R.id.protextid);
        phonelayout = (LinearLayout) findViewById(R.id.pphonelayout);
        weblayout = (LinearLayout) findViewById(R.id.pweblayout);
        maillayout = (LinearLayout) findViewById(R.id.pmailidlayout);
        pricelayout = (LinearLayout) findViewById(R.id.ppricelayout);
        addresslayout = (LinearLayout) findViewById(R.id.paddresslayout);
        faxlayout = (LinearLayout) findViewById(R.id.pfaxlayout);
        shopnamelayout = (LinearLayout) findViewById(R.id.pshopnamelayout);

        addpro = (Button) findViewById(R.id.paddpro);
        addcon = (Button) findViewById(R.id.paddcon);

        pother = (EditText) findViewById(R.id.pother);
        ptimemin =(EditText)findViewById(R.id.ptimemin);






        pcon = (EditText) findViewById(R.id.pcon);
        ppro = (EditText) findViewById(R.id.ppro);
        ptitle = (EditText) findViewById(R.id.ptitle);
        pdesp = (EditText) findViewById(R.id.pdescription);

        aSwitch = (Switch) findViewById(R.id.switch1);
        aSwitch.setChecked(true);
        switchValue =1;
        timeSelected = 1;
        decisionType =0;
        customUsed = 0;
        decisionchoice = "Buy";
        extrasUsed =0;
        isextraUsed = 0;
        extrasArray = new int[7];
        importance =1;
        despUsed=0;

        for(int i=0;i<extrasArray.length;i++)
            extrasArray[i] = 0;
        pwhy = (TextView) findViewById(R.id.pwhy);

        pending = new Pending(this);





        prolist = new ArrayList<>();
        conlist = new ArrayList<>();
        extralist = new ArrayList<>();


        addpro.setOnClickListener(this);
        addcon.setOnClickListener(this);


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pwhy.setText(R.string.pwhy);
                    switchValue =1;
                } else{
                    pwhy.setText(R.string.pwhynot);
                    switchValue =0;
                }
            }
        });
        String arr[] = {"Phone" ,"WebSite" ,"Mail Id"};
        String arr2[] = {"Add an Extra ","Phone" ,"WebSite" ,"Mail Id"};

    findViewById(R.id.fab).setOnClickListener(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add__pending, menu);
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



    void dialogshow(int title,int array,String tag)
    {
        DialogFragment dialogFragment = DialogueCreator.newInstance(title, "", array);

        dialogFragment.show(getFragmentManager(), tag);
    }

    void dialogcreation(int f)
    {
        switch (f)
        {
            case 0:dialogshow(R.string.dialogtitle,R.array.importance_arrays,"importance");
                break;
            case 1:
                dialogshow(R.string.dialogtitle,R.array.time_arrays,"time");
                break;
            case 2:
                dialogshow(R.string.dialogtitle,R.array.remainder_arrays,"remainder");
                break;
            case 3:
                dialogshow(R.string.dialogtitle,R.array.decision_arrays,"decision");
                break;
            case 4:
                dialogshow(R.string.dialogtitle,R.array.extras_arrays,"extra");
                break;
        }
    }




    public void onitemSelected(int f,int position,String choice) {


        switch (f)
        {
            case 1:
                // Toast.makeText(this,view + "         " + id ,Toast.LENGTH_LONG).show()settingtext(text,choice);// ;

                switch (choice)
                {

                    case "In a Few minutes":
                        settingtext(timetext, getResources().getString(R.string.afew));
                        addmin.setVisibility(View.VISIBLE);
                        selecttimelayout.setVisibility(View.GONE);
                        timeSelected = 1;
                        Log.d("add pending","in a few min"+timeSelected);
                        break;
                    case "Select a custom Time":
                        settingtext(timetext, getResources().getString(R.string.acustomtime));
                        addmin.setVisibility(View.GONE);

                        TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                        f = 0;
                        timeSelected = 0;
                        Log.d("add pending","custom"+timeSelected);
                        break;
                }
                break;

            case 2:

                switch (choice)
                {
                    case "Notification Only":
                        remaindertext.setText(getResources().getString(R.string.anotonly));
                        notificationtype =0;
                        break;
                    case "Alarm Only":
                        remaindertext.setText(getResources().getString(R.string.aalaonly));
                        notificationtype =1;
                        break;

                    case "Alarm+ Notification":
                        remaindertext.setText(getResources().getString(R.string.aalarmnot));
                        notificationtype =2;
                        break;



                }

                break;
            case 3:
                settingtext(decisiontext,todothing);
                decisionchoice = choice;
                if(choice.equals("Custom"))
                {

                    pother.setVisibility(View.VISIBLE);
                    decisionType =0;
                }
                else
                {
                    pother.setVisibility(View.GONE);
                    decisionType=1;
                }

                break;
            case 4:


                switch (choice)
                {
                    case "Phone":
                        phonelayout.setVisibility(View.VISIBLE);
                        extrasArray[0]=1;
                        break;
                    case "Website":
                        weblayout.setVisibility(View.VISIBLE);
                        extrasArray[1]=1;
                        break;
                    case "Mail Id":
                        maillayout.setVisibility(View.VISIBLE);
                        extrasArray[2]=1;
                        break;
                    case "Price":
                        pricelayout.setVisibility(View.VISIBLE);
                        extrasArray[3]=1;
                        break;
                    case "Address":
                        addresslayout.setVisibility(View.VISIBLE);
                        extrasArray[4]=1;
                        break;
                    case "Fax":
                        faxlayout.setVisibility(View.VISIBLE);
                        extrasArray[5]=1;
                        break;
                    case "Shop Name":
                        shopnamelayout.setVisibility(View.VISIBLE);
                        extrasArray[6]=1;
                        break;


                }
                break;
            case 0:

                settingtext(importancetext, choice);
                switch(choice)
                {
                    case "Important":
                        importance=1;

                        break;
                    case "Normal":
                        importance =0;
                        break;
                }
                break;

        }

    }

    void settingtext(TextView textView,String text)
    {
        textView.setText(text);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.paddpro:

                String current;
                current = ppro.getText().toString();
                ppro.setText("");
                if(current.equals(""))
                {
                    Toast.makeText(this,"Add a pro in the textbox", Toast.LENGTH_LONG).show();
                }
                else
                {
                   // Toast.makeText(this,"Add a pro in the textbox"+current, Toast.LENGTH_LONG).show();


                    TextView textView = new TextView(getApplicationContext());
                    textView.setText(current);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    textView.setLayoutParams(params);
                    textView.setTextAppearance(getApplicationContext(), R.style.inner_text_large);
                    textView.setTextSize(20);
                    textView.setPadding(10, 5, 10, 3);
                    protextid.addView(textView);
                    prolist.add(prolist.size(), current);
                   // toast.makeText(" " +prolist.size());
                }
                break;
            case R.id.paddcon:

                String current1;
                current1 = pcon.getText().toString();
                pcon.setText("");
                if(current1.equals(""))
                {
                    Toast.makeText(this,"Add a con in the textbox", Toast.LENGTH_LONG).show();
                }
                else
                {
                    // Toast.makeText(this,"Add a pro in the textbox"+current, Toast.LENGTH_LONG).show();


                    TextView textView = new TextView(getApplicationContext());
                    textView.setText(current1);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    textView.setLayoutParams(params);
                    textView.setTextAppearance(getApplicationContext(), R.style.inner_text_large);
                    textView.setTextSize(20);
                    textView.setPadding(10, 5, 10, 3);
                    contextid.addView(textView);
                    conlist.add(conlist.size(),current1);
                }
                break;

            case R.id.pimportancespinner:
                dialogcreation(0);
                break;
            case R.id.ptimespinner:
                dialogcreation(1);
                break;
            case R.id.preminderspinner:
                dialogcreation(2);
                break;
            case R.id.pdecisionspinner:
                dialogcreation(3);
                break;
            case R.id.pextrasspinner:
                dialogcreation(4);
                break;

            case R.id.more:
                aSwitch.setVisibility(View.VISIBLE);
                pdesp.setVisibility(View.VISIBLE);
                ImageView more = (ImageView) findViewById(R.id.more);
                ImageView less = (ImageView) findViewById(R.id.less);
                more.setVisibility(View.GONE);
                less.setVisibility(View.VISIBLE);
                break;
            case R.id.less:
                aSwitch.setVisibility(View.GONE);
                pdesp.setVisibility(View.GONE);
                ImageView more1 = (ImageView) findViewById(R.id.more);
                ImageView less1 = (ImageView) findViewById(R.id.less);
                less1.setVisibility(View.GONE);
                more1.setVisibility(View.VISIBLE);

               // aSwitch.setChecked(true);
               // pdesp.setText("");
                break;


            case R.id.more2:
                findViewById(R.id.ptimemode).setVisibility(View.VISIBLE);
                (findViewById(R.id.more2)).setVisibility(View.GONE);
                ( findViewById(R.id.less2)).setVisibility(View.VISIBLE);
                break;

            case R.id.less2:
                findViewById(R.id.ptimemode).setVisibility(View.GONE);
                (findViewById(R.id.more2)).setVisibility(View.VISIBLE);
                ( findViewById(R.id.less2)).setVisibility(View.GONE);
                break;
            case R.id.more3:

               findViewById(R.id.pwhyline).setVisibility(View.VISIBLE);
                ( findViewById(R.id.pros)).setVisibility(View.VISIBLE);
                ( findViewById(R.id.cons)).setVisibility(View.VISIBLE);
                (findViewById(R.id.more3)).setVisibility(View.GONE);
                ( findViewById(R.id.less3)).setVisibility(View.VISIBLE);

                ppro.setVisibility(View.VISIBLE);
                pcon.setVisibility(View.VISIBLE);
                addcon.setVisibility(View.VISIBLE);
                addpro.setVisibility(View.VISIBLE);
                protextid.setVisibility(View.VISIBLE);
                contextid.setVisibility(View.VISIBLE);
                break;
            case R.id.less3:

                findViewById(R.id.pwhyline).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.pros)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.cons)).setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.more3)).setVisibility(View.VISIBLE);
                ((ImageView) findViewById(R.id.less3)).setVisibility(View.GONE);

                ppro.setVisibility(View.GONE);
                pcon.setVisibility(View.GONE);
                addcon.setVisibility(View.GONE);
                addpro.setVisibility(View.GONE);
                protextid.setVisibility(View.GONE);
                contextid.setVisibility(View.GONE);
                break;

            case R.id.more4:

                findViewById(R.id.howline).setVisibility(View.VISIBLE);
                findViewById(R.id.premtype).setVisibility(View.VISIBLE);
                remaindertext.setVisibility(View.VISIBLE);
                findViewById(R.id.pdectype).setVisibility(View.VISIBLE);
                decisiontext.setVisibility(View.VISIBLE);
                extratext.setVisibility(View.VISIBLE);
                findViewById(R.id.less4).setVisibility(View.VISIBLE);
                findViewById(R.id.more4).setVisibility(View.GONE);

                break;

            case R.id.less4:
                findViewById(R.id.howline).setVisibility(View.GONE);
                findViewById(R.id.premtype).setVisibility(View.GONE);
                remaindertext.setVisibility(View.GONE);
                findViewById(R.id.pdectype).setVisibility(View.GONE);
                decisiontext.setVisibility(View.GONE);
                extratext.setVisibility(View.GONE);
                findViewById(R.id.less4).setVisibility(View.GONE);
                findViewById(R.id.more4).setVisibility(View.VISIBLE);
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

    private int getThings() {
        Log.d("add pending","starting collecting");

        String title,desp,timmin,decision,customdecision = null;
        int timemin =0,needhr=0,needmin=0;
        title =  use.getStrings(ptitle);



       // toast.makeText("long "+time+" date string : "+date.toString()+" date"+date);
        if(title.equals(""))
        {
            Toast.makeText(this,"No title provided" ,Toast.LENGTH_LONG).show();
            ptitle.requestFocus();
            use.key(ptitle);
            //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.showSoftInput(ptitle, InputMethodManager.SHOW_IMPLICIT);

        }
        else
        {
            desp = use.getStrings(pdesp);
            if(desp.equals(""))
            {
                desp = null;
                despUsed =0;
            }
            else
            {
                despUsed=1;
            }
            Log.d("add pending","title success");

            //toast.makeText("Time is df " +timepickerlayout.getCurrentHour() + " "+timepickerlayout.getCurrentMinute());
            if(timeSelected==1)
            {// add time
                xx =0;
                Log.d("add pending","timeslected "+timeSelected);
                timmin=use.getStrings(ptimemin);
                if(timmin.equals(""))
                {
                    toast.makeText("Invalid Time");
                    ptimemin.requestFocus();
                    use.key(ptimemin);
                   xx=0;
                }
                else
                {

                    Log.d("add pending","time else"+xx);
                    timemin = Integer.parseInt(timmin);
                    if(timemin<=0) {

                        toast.makeText("Invalid Time");
                        ptimemin.requestFocus();
                        xx =0;
                        use.key(ptimemin);
                    }
                    else
                    {
                        xx=1;
                        Log.d("add pending","time else else"+xx);
                        Calendar tcalendar = Calendar.getInstance();
                        tcalendar.add(Calendar.MINUTE,timemin);
                        needhr = tcalendar.get(Calendar.HOUR_OF_DAY);
                        needmin= tcalendar.get(Calendar.MINUTE);
                    }
                  //  toast.makeText("" + timemin);
                }


            }
            else if(timeSelected==0)
            {

                //custom time
                needhr = tempcalendar.get(Calendar.HOUR_OF_DAY);
                needmin = tempcalendar.get(Calendar.MINUTE);
                xx = 1;


            }
            else
            {
                toast.makeText("Invalid Time Option");
                timetext.requestFocus();
            }
            Log.d("add pending","time "+xx);
            if(xx==1)
            {
                if(decisionType==1)
                {

                }
                else if(decisionType==0)
                {
                    customdecision = use.getStrings(pother);
                    if(customdecision.equals(""))
                    {
                        customUsed =0;
                    }
                    else
                    {
                        customUsed = 1;
                    }
                }
                else
                {
                    toast.makeText("Invalid decision Type");
                    decisiontext.requestFocus();
                }
                extralist.clear();
                for(int k=0;k<extrasArray.length;k++) {
                    if (extrasArray[k] == 1) {
                        String tempid = "pextra" + k;
                        int extraid = use.getid(tempid);
                        // EditText temptext = (EditText) findViewById(extraid);
                        String m = use.getStrings((EditText) findViewById(extraid));
                        if (m.equals("")) {
                            extrasArray[k] =0;
                        } else {
                            extralist.add(extralist.size(), m);
                            extrasArray[k] =1;

                        }
                    }
                }
                if(extralist.size()>0)
                    isextraUsed =extralist.size();
                else
                    isextraUsed=0;






               // toast.makeText(""+needhr+":"+needmin);
                Log.d("addpending", "pass date is  " + dateofevent);
                Log.d("addpending","notif and alarm val"+notificationtype);
                String passStrings[] = {title,decisionchoice,dateofevent,customdecision,desp};
                int passInt[] = {despUsed,switchValue,timeSelected,prolist.size(),conlist.size(),customUsed,isextraUsed,importance,needhr,needmin,notificationtype,0};

                db = pending.settingDatabase();
               pending.insert(passStrings,passInt,prolist,conlist,extralist,db,extrasArray,tempcalendar);
                toast.makeText("Successully Inserted");
                return 1;

            }

        }

        toast.makeText("Insertion Failed!Please try again");
        return  0;
    }



    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        toast.makeText("time is " + hourOfDay + " " + minute);
    }

    @Override
    public void userClicked(DialogFragment dialogFragment, int m) {
        String tag= dialogFragment.getTag();

        String choice ;
        String notify_time_array[];
        switch (tag)
        {
            case "importance":

                notify_time_array = getResources().getStringArray(R.array.importance_arrays1);
                choice = notify_time_array[m];
                onitemSelected(0, m, choice);
                break;
            case "time":
                notify_time_array = getResources().getStringArray(R.array.time_arrays1);
                choice = notify_time_array[m];
                onitemSelected(1, m, choice);
                break;
            case "remainder":
                notify_time_array = getResources().getStringArray(R.array.remainder_arrays1);
                choice = notify_time_array[m];
                onitemSelected(2, m, choice);
                break;
            case "decision":
                notify_time_array = getResources().getStringArray(R.array.decision_arrays1);
                choice = notify_time_array[m];
                String notify_time_array2[] = getResources().getStringArray(R.array.decision_arrays);
                todothing = notify_time_array2[m];
                onitemSelected(3, m, choice);
                break;
            case "extra":
                notify_time_array = getResources().getStringArray(R.array.extras_arrays1);
                choice = notify_time_array[m];
            onitemSelected(4, m, choice);
            break;


        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update(f);
    }

    private  void update(int f)
    {
        switch(f)
        {
            case 0:
                tempcalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

            selecttimelayout.setVisibility(View.VISIBLE);
                String time = ""+tempcalendar.get(Calendar.HOUR_OF_DAY) +":" +tempcalendar.get(Calendar.MINUTE);

                selecttime.setText("");
                selecttime.setText(time);
                break;
        }
    }
}
