package droid.nir.testapp1;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

import droid.nir.databaseHelper.Today;

/**
 * Created by user on 9/7/2015.
 */
public class BootReset {

    Calendar calendar;
    Today today;
    Context context;
    int hr,min;
    SQLiteDatabase db;

    BootReset(Context context)
    {
        this.context = context;
        calendar = Calendar.getInstance();
        hr= calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        today = new Today(context);


    }

    public void resetalarms() {

        db = today.settingDatabase();
        String selection = " notifyhr >= "+hr +" and notifymin > = "+min;

        int columnno[] = {0,1,2,4,5};
       // Cursor cursor =


    }



}
