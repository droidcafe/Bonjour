package droid.nir.testapp1;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;

import android.app.Dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.database.sqlite.SQLiteDatabase;

import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.AttributeSet;
import droid.nir.testapp1.noveu.Util.Log;

import android.view.Menu;

import android.view.MenuItem;
import android.view.View;

import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateChangedListener;


import java.util.Calendar;


import droid.nir.alarmManager.*;

import droid.nir.databaseHelper.Events;
import droid.nir.databaseHelper.MainDatabase;

import droid.nir.databaseHelper.Remainder;
import droid.nir.defcon3.FirstScreen;
import droid.nir.testapp1.noveu.Home.Home;
import droid.nir.testapp1.noveu.NavDrw.HomeNav;
import droid.nir.testapp1.noveu.welcome.about.*;


public class MainActivity extends ActionBarActivity implements UndoBarController.UndoListener, View.OnClickListener, View.OnLongClickListener, OnDateChangedListener {

    Toolbar toolbar;

    public static String filename = "sharedprefs";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    private CheckForToday checkForToday;

    TextView hiddentext, calendarbuton, calendarcancelbutton;
    MainDatabase mainDatabase;
    Events events;
    Remainder remainder;

    toast maketext;
    FloatingActionMenu leftCenterMenu;
    Dialog dialog;
    Calendar calendar;
    String dateselected = "", todaydate;
    int date, month, year, selectedday, selectedmonth, selectedyr;
    MaterialCalendarView materialCalendarView;
    SharedPreferences sharedPreferences;
    String lastnot, lastdate;
    int ischanged = 0, isalarmset,isfirst=0;
     FloatingActionButton leftCenterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("mainactivity", "on create");
        setContentView(R.layout.activity_main_appbar_above);
        calendar = Calendar.getInstance();
        date = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        todaydate = date + "/" + month + "/" + year;
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(todaydate);
        // getSupportActionBar().setTitle("Create New Decision");
        //getSupportActionBar().setIcon(Drawable.createFromPath("@drawable/ic_laucnher"));
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        maketext = new toast(this);
        readfromshared();
        if(isfirst==1)
        {
            showhelpdialogue();
        }
        setupFloatingButton();
        checkForToday = new CheckForToday();

        mainDatabase = new MainDatabase(this,this);
        hiddentext = (TextView) findViewById(R.id.hiddentext);
       // pending = new Pending(this);
        events = new Events(this);
        remainder = new Remainder(this);
        Navigation_Fragment2 navfrag = (Navigation_Fragment2)
                getSupportFragmentManager().findFragmentById(R.id.nav_fragment);
        navfrag.setup((DrawerLayout) findViewById(R.id.maindrawerlayout), toolbar, leftCenterMenu);

        mRecyclerView = (RecyclerView) findViewById(R.id.pending_recycler);
        mLayoutManager = new LinearLayoutManager(this);

     //   context = this;



            recyclersetup();
            Log.d("main", "alarm already set " + todaydate + " last inserted is " + lastdate);


        //maketext.makeText("Notification details are " + lastnot);


    }



  /*  @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_mode,menu);
    }
*/

    @Override
    public boolean onContextItemSelected(MenuItem item) {

      //  maketext.makeText("You have selected "+item.getItemId());
        return super.onContextItemSelected(item);


    }

    private void readfromshared() {

        sharedPreferences = getSharedPreferences(filename, 0);
        lastnot = sharedPreferences.getString("lastnoti", "xxxxxx");
        isalarmset = sharedPreferences.getInt("isalarm", 0);
        lastdate = sharedPreferences.getString("lastdate", "xxxxxx");
        isfirst = sharedPreferences.getInt("firsttime",1);

    }

    private void rundelayedrefresh() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                recyclersetup();
                //maketext.makeText(getResources().getString(R.string.refreshing));
                Log.d("mainactivity", "Refreshing");
            }
        }, 3000);

        ischanged=0;
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putInt("ischanged", 0);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mainactivity", "on resume");
        if (leftCenterMenu.isOpen())
            leftCenterMenu.close(true);

        if (!(lastdate.equals(todaydate))) {

            new MainDatabase(this,this).settingDatabase(false);
            setUpAlarm(0);
           rundelayedrefresh();
            Log.d("main", "alarm not  set " + todaydate + " last inserted is " + lastdate);
        }
        if(isalarmset!=8999)
        {
            setUpAlarm(1);
        }


        readspecialprefs();
        if (ischanged == 1) {
            rundelayedrefresh();
        }

        //cardsetup();
    }

    private void readspecialprefs() {

        ischanged = sharedPreferences.getInt("ischanged", 0);

    }

    private void translatefab(float slideoffset)
    {
        if(leftCenterMenu!=null)
        {
            if(leftCenterMenu.isOpen())
            {
                leftCenterMenu.close(true);
            }

            leftCenterButton.setTranslationX(slideoffset*300);
        }
    }

    public void ondrawerslide(float slideoffset)
    {
        translatefab(slideoffset);
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void setUpAlarm(int choice) {

        CheckForToday.Asyncschedule asyncschedule = checkForToday.new Asyncschedule(choice);
        asyncschedule.execute(this);

        /*ComponentName receiver = new ComponentName(this, BootReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
                */
    }



    private void recyclersetup() {
        mainDatabase.setUp(mRecyclerView, mLayoutManager, hiddentext);


        Log.d("mainactivity", "calling from recycler setup");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {


            case R.id.calendarselect:

                showcalendialogue();
                return true;

            case R.id.action_settings:

            break;
            case R.id.refresh:

                refresh();
            break;
            case R.id.action_help:

                startActivity(new Intent(this, droid.nir.testapp1.noveu.welcome.about.About.class));

                break;
            case R.id.action_share:

                String sharetext = getResources().getString(R.string.sharetext);
                Intent shar = new Intent();
                shar.setAction(Intent.ACTION_SEND);
                shar.setType("text/plain");
                shar.putExtra(Intent.EXTRA_TEXT, sharetext);
                startActivity(Intent.createChooser(shar,getResources().getString(R.string.shareusing)));
                break;
            case R.id.action_feedback:
                String[] mailid = {"wiizzapps@gmail.com"};
                composeEmail(mailid,"FeedBack",null);
                break;
            case R.id.action_rate:

                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=droid.nir.testapp1"));
                startActivity(intent);
                break;
            case R.id.action_about:

                startActivity(new Intent(this, FirstScreen.class));


                break;


        }


        return super.onOptionsItemSelected(item);
    }


    private void showhelpdialogue()
    {
        startActivity(new Intent(this, FirstScreen.class));

        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putInt("firsttime", 0);
        editor.commit();
      //  MainActivity.this.finish();


    }



    private void showcalendialogue() {

        dialog = new Dialog(this);

        dialog.setTitle(getResources().getString(R.string.choosedate));
        dialog.setContentView(R.layout.activity_trial_calendar2);

        dialog.show();

        materialCalendarView = (MaterialCalendarView) dialog.findViewById(R.id.calendarView);
        calendarbuton = (TextView) dialog.findViewById(R.id.calendarbutton);
        calendarcancelbutton = (TextView) dialog.findViewById(R.id.calendarcancelbutton);

        materialCalendarView.setOnDateChangedListener(this);
        calendarbuton.setOnClickListener(this);
        calendarcancelbutton.setOnClickListener(this);
    }

    @Override
    public void onUndo(Parcelable token) {
        // Perform the undo
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        mUndoBarController.onSaveInstanceState(outState);
    }

    //error here change it

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //    mUndoBarController.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case 0xb:
                Intent goinginent1 = new Intent(this, Add_Pending.class);
                goinginent1.putExtra("customdate", todaydate);
                goinginent1.putExtra("p1", date);
                goinginent1.putExtra("p2", month);
                goinginent1.putExtra("p3", year);
                startActivity(goinginent1);

                break;
            case 0xc:
                Intent goinginent2 = new Intent(this, Add_Event2.class);
                goinginent2.putExtra("customdate", todaydate);
                goinginent2.putExtra("e1", date);
                goinginent2.putExtra("e2", month);
                goinginent2.putExtra("e3", year);
                startActivity(goinginent2);
                break;
            case 0xd:
              /*  Intent goinginent3 = new Intent(this, Add_Todo_list.class);
                goinginent3.putExtra("customdate", todaydate);
                goinginent3.putExtra("l1", date);
                goinginent3.putExtra("l2", month);
                goinginent3.putExtra("l3", year);
                goinginent3.putExtra("oid",-3);
                startActivity(goinginent3);*/

               startActivity(new Intent(this,Home.class));
                break;

            case 0xe:
               // setupremainderdialog();
                startActivity(new Intent(this, HomeNav.class));
                break;

            case R.id.calendarbutton:
                if (dateselected.equals("")) {
                    maketext.makeText(getResources().getString(R.string.nodate));
                } else {
                    Intent gotodate = new Intent(this, CustomDate.class);
                    gotodate.putExtra("datevalue", "" + dateselected);
                    gotodate.putExtra("s1", selectedday);
                    gotodate.putExtra("s2", selectedmonth);
                    gotodate.putExtra("s3", selectedyr);
                    startActivity(gotodate);
                    dialog.dismiss();
                }

                break;
            case R.id.calendarcancelbutton:
                dialog.dismiss();

        }
        leftCenterMenu.close(true);

    }

    private void setupremainderdialog() {

        DialogFragment alertDialog = MyDialogFragment.newInstance(getResources().getString(R.string.newreminder), 2, todaydate);
        alertDialog.show(getFragmentManager(), "dialogs");
    }

    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()) {
            case 0xb:
                maketext.makeText(getResources().getString(R.string.newdecision));
                break;
            case 0xc:
                maketext.makeText(getResources().getString(R.string.newevent));
                break;
            case 0xd:
                maketext.makeText(getResources().getString(R.string.newtodolist));
                break;
            case 0xe:
                maketext.makeText(getResources().getString(R.string.newreminder));
                break;
        }
        return true;
    }

    public void setupFloatingButton() {
        int redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
        int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.action_button_marginn);
        int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        int redActionMenuRadius = getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
        int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

        final ImageView fabIconStar = new ImageView(this);
        fabIconStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_plus_white_36dp));

        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
        starParams.setMargins(redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin);
        fabIconStar.setLayoutParams(starParams);

        FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconStarParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);

         leftCenterButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconStar, fabIconStarParams)
                .setBackgroundDrawable(R.drawable.button_action_blue_selector)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
                .setLayoutParams(starParams)
                .build();

        // Set up customized SubActionButtons for the right center menu
        SubActionButton.Builder lCSubBuilder = new SubActionButton.Builder(this);
        lCSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_red_selector));

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);
        lCSubBuilder.setLayoutParams(blueContentParams);
        // Set custom layout params
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        lCSubBuilder.setLayoutParams(blueParams);

        ImageView lcIcon1 = new ImageView(this);

        ImageView lcIcon2 = new ImageView(this);
        ImageView lcIcon3 = new ImageView(this);
        ImageView lcIcon4 = new ImageView(this);


        lcIcon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_hourglass_empty_white_24dp));
        lcIcon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_event_white_24dp));
        lcIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_format_list_bulleted_white_24dp));
        lcIcon4.setImageDrawable(getResources().getDrawable(R.drawable.ic_alarm_add_white_24dp));

        SubActionButton sb1 = lCSubBuilder.setContentView(lcIcon1, blueContentParams).build();
        SubActionButton sb2 = lCSubBuilder.setContentView(lcIcon2, blueContentParams).build();
        SubActionButton sb3 = lCSubBuilder.setContentView(lcIcon3, blueContentParams).build();
        SubActionButton sb4 = lCSubBuilder.setContentView(lcIcon4, blueContentParams).build();


        // Build another menu with custom options
        leftCenterMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(sb1)
                .addSubActionView(sb2)
                .addSubActionView(sb3)
                .addSubActionView(sb4)

                .setRadius(redActionMenuRadius)
                .attachTo(leftCenterButton)
                .build();

        leftCenterMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                fabIconStar.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconStar, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                fabIconStar.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconStar, pvhR);
                animation.start();
            }
        });


//        sb1.setId(0xb);
//        sb2.setId(0xc);
//        sb3.setId(0xd);
//        sb4.setId(0xe);
        sb1.setOnClickListener(this);
        sb2.setOnClickListener(this);
        sb3.setOnClickListener(this);
        sb4.setOnClickListener(this);

        sb1.setOnLongClickListener(this);
        sb2.setOnLongClickListener(this);
        sb3.setOnLongClickListener(this);
        sb4.setOnLongClickListener(this);
    }

    public void refresh() {
        recyclersetup();
        ischanged=0;
        //maketext.makeText(getResources().getString(R.string.refreshing));
        Log.d("mainactivity", "Refreshing");
    }

    @Override
    public void onDateChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {

        selectedday = calendarDay.getDay();
        selectedmonth = calendarDay.getMonth() + 1;
        selectedyr = calendarDay.getYear();
        dateselected = selectedday + "/" + selectedmonth + "/" + selectedyr;
        //maketext.makeText("" + dateselected);


    }


    public void doPositiveClick(String[] passvalues, int[] passint) {
        if (passvalues[0].equals(""))
            maketext.makeText(getResources().getString(R.string.remtitle));
        else {
            SQLiteDatabase db = remainder.settingDatabase();

            remainder.insert(passvalues, passint, db);
            rundelayedrefresh();
        }
    }

    public void doNegativeClick() {

    }

    public void composeEmail(String[] addresses, String subject, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
     //   intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
