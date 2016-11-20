package droid.nir.testapp1;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

import droid.nir.databaseHelper.Events;
import droid.nir.databaseHelper.Pending;
import droid.nir.databaseHelper.Remainder;
import droid.nir.databaseHelper.SelectAll;
import droid.nir.databaseHelper.Todolist;


public class CustomDate extends ActionBarActivity implements View.OnClickListener, View.OnLongClickListener {

    String passdate;
    toast maketext;
    int date,month,year;
    FloatingActionMenu leftCenterMenu;
    SelectAll selectAll;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    Pending_Recycler_Data pending_recycler_data;
    TextView hiddentext;
    int ispresent =0;
    Events events;
    Pending pending;
    Todolist todolist;
    Remainder remainder;
    Context context;
    List<pending_data> arrayList;
    timecorrection timecorrection;
    int ischanged;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_date);

        Intent dateintent = getIntent();
        passdate = dateintent.getExtras().getString("datevalue");
        date = dateintent.getExtras().getInt("s1");
        month= dateintent.getExtras().getInt("s2");
        year= dateintent.getExtras().getInt("s3");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("" + passdate);

        maketext = new toast(this);
        timecorrection = new timecorrection();
        selectAll = new SelectAll(passdate,this);
        setupFloatingButton();
        hiddentext = (TextView) findViewById(R.id.hiddentext);
        recyclerView = (RecyclerView) findViewById(R.id.custom_date_recycler);
        mLayoutManager = new LinearLayoutManager(this);

        events = new Events(this);
        pending = new Pending(this);
        remainder = new Remainder(this);
        todolist = new Todolist(this);

        arrayList  = new ArrayList<>();
        context = this;
        setuprecycler();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(leftCenterMenu.isOpen())
            leftCenterMenu.close(true);



        readfromshared();
        if (ischanged==1) {
            rundelayedrefresh();
        }


    }

    private void readfromshared() {

        sharedPreferences = getSharedPreferences("sharedprefs", 0);

        ischanged = sharedPreferences.getInt("ischanged", 0);


    }


    private void rundelayedrefresh() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                setuprecycler();
                //maketext.makeText(getResources().getString(R.string.refreshing));
                Log.d("mainactivity", "Refreshing");
            }
        }, 3000);

        ischanged=0;
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putInt("ischanged",0);
        editor.apply();
    }
    private void setuprecycler() {
        Asyncsetuprecycler asyncsetuprecycler = new Asyncsetuprecycler();
        asyncsetuprecycler.execute();
    }

    class  Asyncsetuprecycler extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params) {

            arrayList.clear();

            Cursor cursor2 = selectAll.selectCursor(0);
            Cursor cursor1 = selectAll.selectCursor(1);
            Cursor cursor = selectAll.selectCursor(2);
            Cursor cursor3 = selectAll.selectCursor(3);

            if(cursor2.getCount()!=0)
            {
                ispresent++;
               setuppending(cursor2);
            }
            if(cursor1.getCount()!=0)
            {
                ispresent++;
               setupevents(cursor1);
            }
            if(cursor.getCount()!=0)
            {
                ispresent++;
                setuplists(cursor);
            }
            if(cursor3.getCount()!=0)
            {
                ispresent++;
                setupremainders(cursor3);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(ispresent==0)
            {
                recyclerView.setVisibility(View.GONE);
                hiddentext.setText(getResources().getString(R.string.nocustom));
            }
            else {
                hiddentext.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                pending_recycler_data = new Pending_Recycler_Data(context ,arrayList,CustomDate.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(pending_recycler_data);
            }
        }
    }

    private void setupremainders(Cursor cursor2) {


        Log.d("checkfortoday", " inserting remainder items " + cursor2.getCount());

        while(cursor2.moveToNext()) {
            pending_data customData = new pending_data();

            customData.title = cursor2.getString(cursor2.getColumnIndex("title"));
            customData.oid = cursor2.getInt(cursor2.getColumnIndex("_id"));
            customData.id = cursor2.getInt(cursor2.getColumnIndex("_id"));
            customData.type =3;
            int isdesp = cursor2.getInt(cursor2.getColumnIndex("isdesp"));

            if(isdesp==1)
            {
                customData.customstatement1  = cursor2.getString(cursor2.getColumnIndex("desp"));
            }
            else {
                customData.customstatement1 = getResources().getString(R.string.nodesp) ;

            }

            int hr = cursor2.getInt(cursor2.getColumnIndex("nothr"));
            int min = cursor2.getInt(cursor2.getColumnIndex("notmin"));


            customData.time =timecorrection.formatime(hr,min);
            customData.paddingleft=(int) context.getResources().getDimension(R.dimen.circlepaddingleft);
            arrayList.add(arrayList.size(),customData);
        }
    }

    private void setuppending(Cursor cursor1) {

        Log.d("checkfortoday", " inserting pending items " + cursor1.getCount());
        SQLiteDatabase db;
        db= pending.settingDatabase();
        while (cursor1.moveToNext())
        {
            pending_data customData = new pending_data();
            String statement = "";
            customData.title = cursor1.getString(cursor1.getColumnIndex("title"));
            customData.id= cursor1.getInt(cursor1.getColumnIndex("_id"));
            customData.oid= cursor1.getInt(cursor1.getColumnIndex("_id"));
            customData.type =0;

            int  notmin =cursor1.getInt(cursor1.getColumnIndex("timemin"));
            int nothr = cursor1.getInt(cursor1.getColumnIndex("timehr")) ;

            customData.time = timecorrection.formatime(nothr,notmin);

            customData.paddingleft =(int) context.getResources().getDimension(R.dimen.circlepaddingleft);
            // customData.done = cursor1.getInt(cursor1.getColumnIndex("done"));

            String decisiontype = cursor1.getString(cursor1.getColumnIndex("decisiontype"));
            if(decisiontype.equals("Custom"))
            {
                String tempselection = " pid = ?";
                String tempargs[] = {Integer.toString(customData.id)};
                int customvalue = cursor1.getInt(cursor1.getColumnIndex("custom"));
                if(customvalue==1)
                {
                    int tempcolumn[] = {2};
                    Cursor tempcursor = pending.select(db,4,tempcolumn,tempselection,tempargs,null,null,null);

                    while (tempcursor.moveToNext())
                    {
                        String custom = tempcursor.getString(tempcursor.getColumnIndex("custom"));
                        statement = getResources().getString(R.string.shouldi)+" "+custom + " ?";

                    }
                }
                else
                {
                    int tempcolumn[] = {2,5,6};
                    String teselection = " _id = ?";
                    Cursor tempcursor = pending.select(db,0,tempcolumn,teselection,tempargs,null,null,null);

                    while (tempcursor.moveToNext())
                    {
                        int desp = tempcursor.getInt(tempcursor.getColumnIndex("desp"));
                        if(desp!=0)
                        {
                            int tempcolumnn[] = {2};
                            Cursor tempcursorr = pending.select(db,1,tempcolumnn,tempselection,tempargs,null,null,null);

                            while (tempcursorr.moveToNext())
                            {
                                String custom = tempcursorr.getString(tempcursorr.getColumnIndex("description"));
                                statement = custom;

                            }
                        }
                        else
                        {
                            int nocons = tempcursor.getInt(tempcursor.getColumnIndex("nocons"));
                            int nopros =tempcursor.getInt(tempcursor.getColumnIndex("nopros"));

                            if((nopros==0)&&(nocons==0))
                            {
                                statement = getResources().getString(R.string.pnoprocon);

                            }
                            else if(nopros>nocons)
                            {
                                statement = getResources().getString(R.string.pprobig);

                            }
                            else if(nopros<nocons)
                            {
                                statement = getResources().getString(R.string.pconbig);
                            }
                            else {
                                statement = getResources().getString(R.string.pprosame);
                            }
                        }
                    }
                }
            }
            else {

                String[] tempar = getResources().getStringArray(R.array.decision_arrays);
                String[] tempar1 = getResources().getStringArray(R.array.decision_arrays1);

                int yy=0;
                while((yy<tempar1.length)&&!(decisiontype.equals(tempar1[yy])))
                {
                    yy++;
                }
                if (yy==tempar1.length)
                    statement = getResources().getString(R.string.shouldi) + " "+decisiontype +" ?";
                else {
                    statement = getResources().getString(R.string.shouldi) + " "+tempar[yy] +" ?";
                }

            }

            customData.customstatement1 = statement;

            arrayList.add(arrayList.size(),customData);
        }
    }

    private void setupevents(Cursor cursor) {

        int iswholeday=0;
        SQLiteDatabase db;
        db =events.settingDatabase();
        Log.d("checkfort1oday", " inserting event items " + cursor.getCount());
        while(cursor.moveToNext())
        {

            pending_data customData = new pending_data();


            customData.id = cursor.getInt(cursor.getColumnIndex("_id"));
            customData.oid = cursor.getInt(cursor.getColumnIndex("_id"));
            customData.title = cursor.getString(cursor.getColumnIndex("title"));
            customData.type=1;

            int notify = cursor.getInt(cursor.getColumnIndex("notify"));
            iswholeday = cursor.getInt(cursor.getColumnIndex("wholeday"));


            String tempselection = " eid = ?";
            String tempargs[] = {Integer.toString(customData.id)};
            if(iswholeday!=1)
            {
                customData.temp1 =0;

                int tempcolumno[] = {2,3,4,5,6,7};
                Cursor tempcursor = events.select(db,1,tempcolumno,tempselection,tempargs,null,null,null);
                while(tempcursor.moveToNext())
                {
                    customData.customstatement1 =tempcursor.getString(tempcursor.getColumnIndex("fromdate"));
                    customData.customstatement2 =tempcursor.getString(tempcursor.getColumnIndex("todate"));
                    int frommin = tempcursor.getInt(tempcursor.getColumnIndex("fromtimemin"));
                    int  fromhr = tempcursor.getInt(tempcursor.getColumnIndex("fromtimehr")) ;

                    customData.customstatement3 = timecorrection.formatime(fromhr,frommin);


                    int tomin = tempcursor.getInt(tempcursor.getColumnIndex("totimemin"));
                    int tohr  = tempcursor.getInt(tempcursor.getColumnIndex("totimehr")) ;

                    customData.customstatement4 = timecorrection.formatime(tohr,tomin);



                }

            }
            else {
                customData.customstatement1 = getResources().getString(R.string.ewholeday);
                customData.temp1 =1;

            }

            if(notify>0)
            {
                notify =1;

                int tempcolumno[] = {2,3,4};
                Cursor tempcursor = events.select(db, 2, tempcolumno, tempselection, tempargs, null, null, null);

                while(tempcursor.moveToNext())
                {


                   int notifymin =tempcursor.getInt(tempcursor.getColumnIndex("timemin"));

                    int notifhr = tempcursor.getInt(tempcursor.getColumnIndex("timehr")) ;
                    customData.time = timecorrection.formatime(notifhr,notifymin);

                }
                customData.paddingleft =(int) context.getResources().getDimension(R.dimen.circlepaddingleft);
            }
            else {
                customData.paddingleft = (int) context.getResources().getDimension(R.dimen.circlepaddingleft2);
                if(!customData.title.equals(""))
                {
                    char ch = customData.title.charAt(0);
                    ch = Character.toUpperCase(ch);
                    customData.time = Character.toString(ch);
                }
                else {
                    customData.time = Character.toString('E');
                }
            }


            arrayList.add(arrayList.size(),customData);
        }

    }


    public void setuplists(Cursor cursor2)
    {

        Log.d("checkfortoday", " inserting pending items " + cursor2.getCount());
        SQLiteDatabase db;
        db = todolist.settingDatabase();
        while(cursor2.moveToNext())
        {
            pending_data customData = new pending_data();

            customData.title = cursor2.getString(cursor2.getColumnIndex("title"));
            int notif = cursor2.getInt(cursor2.getColumnIndex("notification"));
            customData.id = cursor2.getInt(cursor2.getColumnIndex("_id"));
            customData.oid = cursor2.getInt(cursor2.getColumnIndex("_id"));
            customData.type = 2;

            int listno = cursor2.getInt(cursor2.getColumnIndex("listno"));
            customData.customstatement1 = getResources().getString(R.string.youhave)+" "+listno +" " + getResources().getString(R.string.noof);
            if(notif==1)
            {
                String tempselection = " tid = ?";
                String tempargs[] = {Integer.toString(customData.id)};
                int tempcolumn[] = {2,3};
                Cursor tempcursor = todolist.select(db,2,tempcolumn,tempselection,tempargs,null,null,null);

                while(tempcursor.moveToNext())
                {
                    int notifmin = tempcursor.getInt(tempcursor.getColumnIndex("nmin"));
                    int notifhr ;

                    notifhr = tempcursor.getInt(tempcursor.getColumnIndex("nhr"));

                    customData.time = timecorrection.formatime(notifhr,notifmin);


                    customData.paddingleft =(int) context.getResources().getDimension(R.dimen.circlepaddingleft);
                }
            }

            else {

                customData.paddingleft = (int) context.getResources().getDimension(R.dimen.circlepaddingleft2);
                if(!customData.title.equals(""))
                {
                    char ch = customData.title.charAt(0);
                    ch = Character.toUpperCase(ch);
                    customData.time = Character.toString(ch);
                }
                else {
                    customData.time = Character.toString('L');
                }

            }

            arrayList.add(arrayList.size(),customData);
        }



    }



    public  void setupFloatingButton()
    {
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

        final FloatingActionButton leftCenterButton = new FloatingActionButton.Builder(this)
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



        lcIcon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_bell_white_24dp));
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_custom_date, menu);
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

        if(id==R.id.refresh)
        {
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case 0xb:
                Intent goinginent1 = new Intent(this,Add_Pending.class);
                goinginent1.putExtra("customdate", passdate);
                goinginent1.putExtra("p1",date);
                goinginent1.putExtra("p2",month);
                goinginent1.putExtra("p3", year);
                startActivity(goinginent1);

                break;
            case 0xc:
                Intent goinginent2 = new Intent(this,Add_Event2.class);
                goinginent2.putExtra("customdate",passdate);
                goinginent2.putExtra("e1",date);
                goinginent2.putExtra("e2",month);
                goinginent2.putExtra("e3",year);
                startActivity(goinginent2);
                break;
            case 0xd:
                Intent goinginent3 = new Intent(this,Add_Todo_list.class);
                goinginent3.putExtra("customdate",passdate);
                goinginent3.putExtra("l1",date);
                goinginent3.putExtra("l2",month);
                goinginent3.putExtra("l3",year);
                goinginent3.putExtra("oid",-3);
                startActivity(goinginent3);
                break;
            case 0xe:
                //   startActivity(new Intent(this,floating_trials.class));
                setupremainderdialog();
                break;
        }
        leftCenterMenu.close(true);

    }

    private void setupremainderdialog() {

        DialogFragment alertDialog = MyDialogFragment.newInstance(getResources().getString(R.string.newreminder),3,passdate);
        alertDialog.show(getFragmentManager(), "dialogs");
    }

    @Override
    public boolean onLongClick(View v) {

        switch (v.getId())
        {
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

    public void doPositiveClick(String[] passvalues, int[] passint) {
        if(passvalues[0].equals(""))
            maketext.makeText(getResources().getString(R.string.remtitle));
        else {

            SQLiteDatabase db = remainder.settingDatabase();

            remainder.insert(passvalues,passint,db);
            rundelayedrefresh();
        }
    }

    public void doNegativeClick() {

    }

    public void refresh() {
        setuprecycler();
        //maketext.makeText(getResources().getString(R.string.refreshing));
        Log.d("mainactivity", "Refreshing");
    }
}
