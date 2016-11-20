package droid.nir.testapp1;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;


import android.support.v7.app.AlertDialog;

import android.view.View;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import droid.nir.databaseHelper.Remainder;


/**
 * Created by user on 7/29/2015.
 */
public class MyDialogFragment extends DialogFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    int isalarm = 0;
    Calendar calendar;
    TextView notimte;
    toast maketext;
    static String passdate;
    static int oid;
    int choice;
    int ispresent = 1;

    public static MyDialogFragment newInstance(String title, int choice, String passdatee) {
        MyDialogFragment frag = new MyDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putInt("choice", choice);
        args.putString("passdate", passdatee);
        passdate = passdatee;
        frag.setArguments(args);
        return frag;
    }

    public static MyDialogFragment newInstance(String title, int choice, String passdatee, int oiid) {
        MyDialogFragment frag = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("choice", choice);
        args.putString("passdate", passdatee);
        passdate = passdatee;
        frag.setArguments(args);
        oid = oiid;
        return frag;
    }

    /*

    1-todo list item edits
    2-reminder creation from main activity
    3-reminder creatino from custom date
    4-delete confirmation from showdecision
    5-delte conf from event
    6-delet conf from showlist
    7-reminder creation from allreminder
    8-delet conf from showrem
    9-delet conf from alllists
    10- reminder creation from show reminder

     */

    @Override
    public void onStart() {
        super.onStart();

        if (choice == 2 || choice == 3 || choice == 7 || choice == 10) {
            calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            final Switch alarm = (Switch) getDialog().findViewById(R.id.ralarmswitch);
            alarm.setChecked(false);
            alarm.setOnCheckedChangeListener(this);

            ImageView more  = (ImageView) getDialog().findViewById(R.id.more);
            ImageView less  = (ImageView) getDialog().findViewById(R.id.less);
            notimte = (TextView) getDialog().findViewById(R.id.rnotifacationtime);
            update();
            notimte.setOnClickListener(this);
            more.setOnClickListener(this);
            less.setOnClickListener(this);

            if (choice == 10) {
                getvalues();
            }
        } else if (choice == 4 || choice == 5 || choice == 6 || choice == 8 || choice == 9) {
            TextView delete = (TextView) getDialog().findViewById(R.id.contexttext);
            delete.setText(passdate);
        }


    }

    private void getvalues() {

        AsyncFill asyncFill = new AsyncFill();
        asyncFill.execute();
    }

    public class AsyncFill extends AsyncTask<Void, Void, Void> {

        String title, timetext, desp = "";
        int isalarm;

        @Override
        protected Void doInBackground(Void... params) {

            String selection = " _id = " + oid;

            int[] columnno = {1, 2, 3, 4, 6, 7, 8};
            Remainder remainder = new Remainder(getActivity());
            SQLiteDatabase db = remainder.settingDatabase();

            Cursor cursor = remainder.select(db, 0, columnno, selection, null, null, null, null);
            ispresent = cursor.getCount();

            while (cursor.moveToNext()) {

                title = cursor.getString(cursor.getColumnIndex("title"));

                // date = cursor.getString(cursor.getColumnIndex("date"));
                int nothr = cursor.getInt(cursor.getColumnIndex("nothr"));
                int notmin = cursor.getInt(cursor.getColumnIndex("notmin"));
                isalarm = cursor.getInt(cursor.getColumnIndex("isalarm"));
                int isdesp = cursor.getInt(cursor.getColumnIndex("isdesp"));
                timetext = new timecorrection().formatime(nothr, notmin);


                if (isdesp == 1) {
                    desp = (cursor.getString(cursor.getColumnIndex("desp")));
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (ispresent == 1) {
                EditText rtitle = (EditText) getDialog().findViewById(R.id.rtitle);
                EditText rdesp = (EditText) getDialog().findViewById(R.id.rdesp);

                rtitle.setText(title);
                rdesp.setText(desp);
                notimte.setText(timetext);
                if (isalarm == 1) {

                    final Switch alarm = (Switch) getDialog().findViewById(R.id.ralarmswitch);
                    alarm.setChecked(true);
                    showall();
                }
                else if(!desp.equals(""))
                {
                    showall();
                }


            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString("title");
        final int choice = getArguments().getInt("choice");
        maketext = new toast(getActivity());
        this.choice = choice;
        if (choice == 1) {
            return new AlertDialog.Builder(getActivity())

                    .setTitle(title)
                    .setView(R.layout.mydialoglayout)
                    .setPositiveButton(getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    final EditText tv = (EditText) getDialog().findViewById(R.id.newlistitem);

                                    String text = tv.getText().toString();
                                    if (tv.equals(""))
                                        maketext.makeText(getResources().getString(R.string.emptytodolist));
                                    else {
                                        ((Add_Todo_list) getActivity()).doPositiveClick(text);

                                    }
                                }
                            }
                    )
                    .setNegativeButton(getResources().getText(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((Add_Todo_list) getActivity()).doNegativeClick();
                                }
                            }
                    )
                    .create();
        } else if (choice == 2 || choice == 7 )//remaindr creationn dialog from main activity and from allremainder activity
        {
            passdate = getArguments().getString("passdate");
            AlertDialog.Builder remaainderalert = new AlertDialog.Builder(getActivity());


            remaainderalert.setTitle(title)
                    .setView(R.layout.remainder_dialog)
                    .setPositiveButton(getResources().getText(R.string.create),
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    final EditText rtitle = (EditText) getDialog().findViewById(R.id.rtitle);
                                    final EditText desp = (EditText) getDialog().findViewById(R.id.rdesp);

                                    String title = rtitle.getText().toString();
                                    String descriptionn = desp.getText().toString();

                                    int[] passint = {calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), isalarm};
                                    String[] passvalues = {title, descriptionn, passdate};
                                    if (choice == 2)
                                        ((MainActivity) getActivity()).doPositiveClick(passvalues, passint);
                                    else if (choice == 7)
                                        ((AllRemainders) getActivity()).doPositiveClick(passvalues, passint);

                                }
                            }
                    )
                    .setNegativeButton(getResources().getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    if (choice == 2)
                                        ((MainActivity) getActivity()).doNegativeClick();
                                    else if (choice == 7)
                                        ((AllRemainders) getActivity()).doNegativeClick();

                                }
                            }
                    )
                    .create();

            return remaainderalert.create();

        } else if (choice == 10) {
            passdate = getArguments().getString("passdate");
            AlertDialog.Builder remaainderalert = new AlertDialog.Builder(getActivity());


            remaainderalert.setTitle(title)
                    .setView(R.layout.remainder_dialog)
                    .setPositiveButton(getResources().getText(R.string.update),
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    final EditText rtitle = (EditText) getDialog().findViewById(R.id.rtitle);
                                    final EditText desp = (EditText) getDialog().findViewById(R.id.rdesp);

                                    String title = rtitle.getText().toString();
                                    String descriptionn = desp.getText().toString();

                                    int[] passint = {calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), isalarm};
                                    String[] passvalues = {title, descriptionn, passdate};
                                    ((ShowRemainder) getActivity()).doPositiveClick(passvalues, passint);


                                }
                            }
                    )
                    .setNegativeButton(getResources().getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            }
                    )
                    .create();

            return remaainderalert.create();

        } else if (choice == 3)//remaindr creationn dialog from custom date

        {
            passdate = getArguments().getString("passdate");
            AlertDialog.Builder remaainderalert = new AlertDialog.Builder(getActivity());


            remaainderalert.setTitle(title)
                    .setView(R.layout.remainder_dialog)
                    .setPositiveButton(getResources().getString(R.string.create),
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    final EditText rtitle = (EditText) getDialog().findViewById(R.id.rtitle);
                                    final EditText desp = (EditText) getDialog().findViewById(R.id.rdesp);

                                    String title = rtitle.getText().toString();
                                    String descriptionn = desp.getText().toString();

                                    int[] passint = {calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), isalarm};
                                    String[] passvalues = {title, descriptionn, passdate};
                                    ((CustomDate) getActivity()).doPositiveClick(passvalues, passint);


                                }
                            }
                    )
                    .setNegativeButton(getResources().getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((CustomDate) getActivity()).doNegativeClick();
                                }
                            }
                    )
                    .create();

            return remaainderalert.create();

        } else if ((choice == 4) || (choice == 5) || (choice == 6) || (choice == 8 || choice == 9))//deletion confirmation dialog from todolist

        {
            String contexttext = getArguments().getString("passdate");


            AlertDialog.Builder deletionconfirmation = new AlertDialog.Builder(getActivity());

            deletionconfirmation.setTitle(title)
                    .setView(R.layout.delete_dialog_layout)
                    .setPositiveButton(getResources().getString(R.string.Delete), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            switch (choice) {
                                case 4:
                                    ((ShowDecision) getActivity()).doPositiveClick();
                                    break;
                                case 5:
                                    ((ShowEvent) getActivity()).doPositiveClick();
                                    break;
                                case 6:
                                    ((ShowList) getActivity()).doPositiveClick();
                                    break;

                                case 8:
                                    ((ShowRemainder) getActivity()).doPositiveClick();
                                    break;
                                case 9:
                                    ((AllLists) getActivity()).doPositiveClick();
                                    break;

                            }
                        }

                    })
                    .setNegativeButton(getResources().getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    switch (choice) {
                                        case 4:

                                            break;
                                        case 5:
                                            ((ShowEvent) getActivity()).doNegativeClick();
                                            break;
                                        case 6:
                                            ((ShowList) getActivity()).doNegativeClick();
                                            break;
                                        case 8:
                                            ((ShowRemainder) getActivity()).doNegativeClick();
                                            break;
                                        case 9:
                                            ((AllLists) getActivity()).doNegativeClick();
                                            break;
                                    }

                                }

                            });


            return deletionconfirmation.create();
        }

        return null;

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            isalarm = 1;
        } else {
            isalarm = 0;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rnotifacationtime:
            case R.id.efromdate:

                TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                break;

            case R.id.more:
                showall();
                break;

            case R.id.less:
                hideall();
                break;
        }
    }

    private void hideall() {

        getDialog().findViewById(R.id.ralarm).setVisibility(View.GONE);
        getDialog().findViewById(R.id.ralarmswitch).setVisibility(View.GONE);

        getDialog().findViewById(R.id.rdesp).setVisibility(View.GONE);
        getDialog().findViewById(R.id.less).setVisibility(View.GONE);
        getDialog().findViewById(R.id.more).setVisibility(View.VISIBLE);
    }

    private void showall() {

        getDialog().findViewById(R.id.ralarm).setVisibility(View.VISIBLE);
        getDialog().findViewById(R.id.ralarmswitch).setVisibility(View.VISIBLE);

        getDialog().findViewById(R.id.rdesp).setVisibility(View.VISIBLE);
        getDialog().findViewById(R.id.less).setVisibility(View.VISIBLE);
        getDialog().findViewById(R.id.more).setVisibility(View.GONE);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {

    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update();

    }

    private void update() {

        String time = "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        notimte.setText(time);
    }
}