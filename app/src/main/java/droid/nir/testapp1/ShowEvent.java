package droid.nir.testapp1;


import android.app.DialogFragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import droid.nir.databaseHelper.Events;
import droid.nir.databaseHelper.Today;
import droid.nir.testapp1.noveu.Util.FirebaseUtil;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.social.share.Master;

public class ShowEvent extends ActionBarActivity {

    TextView title, location, date, done, fromdate, fromtime, from, to, from1, to1, fromdate1, todate1, totime, todate, notes, notify, notifytime, alarm, wholeday, hiddentext;
    ImageView notesimage;
    int oid;

    toast maketext;
    SQLiteDatabase db;
    Events events;
    String titletext, fromdatetext, fromtimetext, todatetext, totimetext;
    int iswholeday;
    timecorrection timecorrection;
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.event));
        setContentView(R.layout.activity_show_event);

        maketext = new toast(this);
        events = new Events(this);
        timecorrection = new timecorrection();
        Intent showintent = getIntent();
        if (showintent != null) {

            oid = showintent.getExtras().getInt("oid");

            // maketext.makeText("The passed id and oid is  "+oid);


            title = (TextView) findViewById(R.id.etitle);
            location = (TextView) findViewById(R.id.elocation);
            date = (TextView) findViewById(R.id.edate);
            done = (TextView) findViewById(R.id.edone);
            fromdate = (TextView) findViewById(R.id.efromdate);
            fromdate1 = (TextView) findViewById(R.id.efromdate1);
            hiddentext = (TextView) findViewById(R.id.hiddentext);
            fromtime = (TextView) findViewById(R.id.efromtime);
            totime = (TextView) findViewById(R.id.etotime);
            from = (TextView) findViewById(R.id.etimefrom);
            to = (TextView) findViewById(R.id.etimeto);
            from1 = (TextView) findViewById(R.id.etimefrom1);
            to1 = (TextView) findViewById(R.id.etimeto1);
            todate = (TextView) findViewById(R.id.etodate);
            todate1 = (TextView) findViewById(R.id.etodate1);
            wholeday = (TextView) findViewById(R.id.eltimeedit);
            notify = (TextView) findViewById(R.id.enotificationn);
            notifytime = (TextView) findViewById(R.id.enotifytime);
            alarm = (TextView) findViewById(R.id.ealarmset);
            notes = (TextView) findViewById(R.id.enotes);

            notesimage = (ImageView) findViewById(R.id.enotesimage);
            setinvisibility();

            db = events.settingDatabase();
            setupEvent();

            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            FirebaseUtil.recordScreenView(this,"show event",mFirebaseAnalytics);

        } else {
            maketext.makeText("Sorry!Please Try Again!Failed Pipe");
        }
    }

    private void setinvisibility() {

        wholeday.setVisibility(View.GONE);
        from.setVisibility(View.GONE);
        from1.setVisibility(View.GONE);
        to.setVisibility(View.GONE);
        to1.setVisibility(View.GONE);
        todate.setVisibility(View.GONE);
        todate1.setVisibility(View.GONE);
        fromdate.setVisibility(View.GONE);
        fromdate1.setVisibility(View.GONE);
        fromtime.setVisibility(View.GONE);
        totime.setVisibility(View.GONE);

        notesimage.setVisibility(View.GONE);
        notes.setVisibility(View.GONE);
    }

    private void setupEvent() {
        AsyncFill asyncFill = new AsyncFill();
        asyncFill.execute();


    }

    class AsyncFill extends AsyncTask<Void, Void, Void> {

        String locationtext, notestext, nottimetext, alarmtext;
        int donee, isnotify, not, noofdays, issameday, isalarm, ispresent = 0;

        @Override
        protected Void doInBackground(Void... params) {
            String selection = " _id = " + oid;
            String selectionn = " eid = " + oid;
            int[] columnno = {1, 2, 3, 4, 5, 6, 7, 8};
            Cursor cursor;
            cursor = events.select(db, 0, columnno, selection, null, null, null, null);

            if (cursor.getCount() == 0) {
                ispresent = 0;
            } else {
                ispresent = 1;
            }
            while (cursor.moveToNext()) {
                titletext = cursor.getString(cursor.getColumnIndex("title"));
                locationtext = cursor.getString(cursor.getColumnIndex("location"));

                iswholeday = cursor.getInt(cursor.getColumnIndex("wholeday"));
                donee = cursor.getInt(cursor.getColumnIndex("done"));
                noofdays = cursor.getInt(cursor.getColumnIndex("noofdays"));
                isnotify = cursor.getInt(cursor.getColumnIndex("notify"));
                not = cursor.getInt(cursor.getColumnIndex("notes"));

                if (iswholeday == 0) {

                    int columnno1[] = {2, 3, 4, 5, 6, 7};


                    Cursor tempcursor = events.select(db, 1, columnno1, selectionn, null, null, null, null);
                    while (tempcursor.moveToNext()) {
                        fromdatetext = tempcursor.getString(tempcursor.getColumnIndex("fromdate"));
                        todatetext = tempcursor.getString(tempcursor.getColumnIndex("todate"));

                        if (fromdatetext.equals(todatetext)) {
                            issameday = 1;
                        }
                        int frommin = tempcursor.getInt(tempcursor.getColumnIndex("fromtimemin"));

                        int fromhr = tempcursor.getInt(tempcursor.getColumnIndex("fromtimehr"));
                        int tomin = tempcursor.getInt(tempcursor.getColumnIndex("totimemin"));
                        int tohr = tempcursor.getInt(tempcursor.getColumnIndex("totimehr"));

                        fromtimetext = timecorrection.formatime(fromhr, frommin);


                        totimetext = timecorrection.formatime(tohr, tomin);


                    }
                } else {
                    fromdatetext = cursor.getString(cursor.getColumnIndex("date"));
                }

                Log.d("showevent", "notifiaction " + isnotify);
                if (isnotify > 0) {
                    int columnno1[] = {2, 3, 4};

                    Cursor tempcursor = events.select(db, 2, columnno1, selectionn, null, null, null, null);
                    while (tempcursor.moveToNext()) {

                        int min = tempcursor.getInt(tempcursor.getColumnIndex("timemin"));
                        int hr = tempcursor.getInt(tempcursor.getColumnIndex("timehr"));

                        nottimetext = timecorrection.formatime(hr, min);


                        Log.d("showevent", "notification is " + nottimetext);
                        isalarm = tempcursor.getInt(tempcursor.getColumnIndex("alarm"));
                        if (isalarm == 1)
                            alarmtext = getResources().getString(R.string.alarmset);
                        else
                            alarmtext = getResources().getString(R.string.noalarmset);

                    }
                }

                if (not == 1) {
                    Log.d("showevent ", "Note is present ");
                    int columnno1[] = {2};
                    Cursor tempcursor = events.select(db, 3, columnno1, selectionn, null, null, null, null);
                    while (tempcursor.moveToNext()) {
                        notestext = tempcursor.getString(tempcursor.getColumnIndex("note"));
                        Log.d("showevent ", "Note is " + notestext);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (ispresent == 0) {
              /*  title.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
                location.setVisibility(View.GONE);
                notify.setVisibility(View.GONE);
*/
                RelativeLayout fist, second;
                fist = (RelativeLayout) findViewById(R.id.firstlayout);
                second = (RelativeLayout) findViewById(R.id.secondlayout);

                fist.setVisibility(View.GONE);
                second.setVisibility(View.GONE);

                View v = findViewById(R.id.lastline);
                v.setVisibility(View.GONE);
                hiddentext.setVisibility(View.VISIBLE);
                hiddentext.setText(R.string.noevent);
            } else {
                title.setText(titletext);
                date.setText(fromdatetext);
                if (locationtext.equals("")) {
                    location.setText(R.string.enolocation);
                } else {
                    location.setText(locationtext);
                }

                if (donee == 1) {
                    done.setText("Event is scheduled for today");
                } else {
                    done.setText("Event is not scheduled for today");
                }
                if (isnotify == 0) {
                    //no notification
                    notify.setText(R.string.nonotificationset);
                    notifytime.setVisibility(View.GONE);
                    alarm.setVisibility(View.GONE);
                } else {
                    notifytime.setText(nottimetext);
                    alarm.setText(alarmtext);
                }

                if (iswholeday == 1) {
                    //whole day event
                    wholeday.setVisibility(View.VISIBLE);
                } else {
                    if (issameday == 1) {
                        fromdate1.setVisibility(View.VISIBLE);
                        todate1.setVisibility(View.VISIBLE);
                        from1.setVisibility(View.VISIBLE);
                        to1.setVisibility(View.VISIBLE);

                        fromdate1.setText(fromtimetext);
                        todate1.setText(totimetext);
                    } else {
                        from.setVisibility(View.VISIBLE);
                        to.setVisibility(View.VISIBLE);
                        fromtime.setVisibility(View.VISIBLE);
                        totime.setVisibility(View.VISIBLE);
                        fromdate.setVisibility(View.VISIBLE);
                        todate.setVisibility(View.VISIBLE);

                        totime.setText(totimetext);
                        fromtime.setText(fromtimetext);
                        todate.setText(todatetext);
                        fromdate.setText(fromdatetext);
                    }
                }

                if (not == 1) {
                    notes.setVisibility(View.VISIBLE);
                    notesimage.setVisibility(View.VISIBLE);

                    notes.setText(notestext);
                }

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_clear) {

            String x = "1";
            x = x.concat(Integer.toString(oid));
            int pendingintentid = Integer.parseInt(x);

            NotificationManager notificationManager = (NotificationManager) ShowEvent.this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(pendingintentid);


            return true;
        } else if (id == R.id.action_share) {
            String list = titletext + "\n";
            String temp;
            if (iswholeday != 1) {
                temp = getResources().getString(R.string.eeventform)+" " + fromdatetext + " " + fromtimetext + " " + getResources().getString(R.string.eto)+" " + todatetext + " " + totimetext;
            } else {
                temp = getResources().getString(R.string.ewhole) + " " + fromdatetext;
            }
            list = list.concat(temp);
            Intent shareintent = Master.share(titletext, list);

            Bundle fire_share = new Bundle();
            fire_share.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"event");
            fire_share.putString(FirebaseAnalytics.Param.ITEM_ID, titletext);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,fire_share);
            startActivity(Intent.createChooser(shareintent, getResources().getString(R.string.shareusing)));
        } else if (id == R.id.action_delete) {
            DialogFragment alertDialog = MyDialogFragment.newInstance(getResources().getString(R.string.delete) + " " + titletext, 5, getResources().getString(R.string.edeletewarn));
            alertDialog.show(getFragmentManager(), "dialogs");
        }


        return super.onOptionsItemSelected(item);
    }

    public void doPositiveClick() {

        int x = deletelistitem();
        if (x > 0) {
            maketext.makeText(getResources().getString(R.string.deletionsucess));
            Bundle fireBundle = new Bundle();
            fireBundle.putString("name",titletext);
            fireBundle.putString("type","expand");
            mFirebaseAnalytics.logEvent(FirebaseUtil.event_delete,fireBundle);

            SharedPreferences sharedPreferences = getSharedPreferences("sharedprefs", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("ischanged", 1);
            editor.apply();
            finish();
        } else {
            maketext.makeText(getResources().getString(R.string.deletionfailed));
        }
    }

    private int deletelistitem() {

        String selection = " _id = " + oid;
        int xx = events.delete(db, 0, selection, null);

        selection = " eid = " + oid;
        events.delete(db, 1, selection, null);
        events.delete(db, 2, selection, null);
        events.delete(db, 3, selection, null);

        selection = "type = 1 and oid = " + oid;

        Today today = new Today(ShowEvent.this);
        today.settingDatabase();
        today.deleterow(selection, 0, null, db);

       /* String x = "1";
        x = x.concat(Integer.toString(oid));
        int pendingintentid = Integer.parseInt(x);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(pendingintentid);

        Intent callintent = new Intent(this, NotificationCreater.class);
        callintent.putExtra("title", "" + title);
        String oiid = Integer.toString(oid);
        callintent.putExtra("oid",""+oiid);
        callintent.putExtra("text", "" + "There are "+listno+" items in the list");
        String type = Integer.toString(2);
        callintent.putExtra("type",type);
        callintent.setAction("nir.droid.NEWNOTIFICATION");


        notificationManager.cancel(pendingintentid);

        String pent = Integer.toString(oid);
        pent = "333".concat(pent);
        int iid = Integer.parseInt(pent);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, iid, callintent, PendingIntent.FLAG_NO_CREATE);
        AlarmManager alarmManager= (AlarmManager) this.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);*/


        return xx;


    }

    public void doNegativeClick() {
    }
}
