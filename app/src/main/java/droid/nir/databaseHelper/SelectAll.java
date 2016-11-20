package droid.nir.databaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import droid.nir.testapp1.noveu.Util.Log;

/**
 * Created by user on 8/22/2015.
 */
public class SelectAll {

    String passdate;
    String selection, orderby;
    Context context;

    Events events;
    Pending pending;
    Remainder remainder;
    Todolist todolist;
    String[] selectionArgs;
    public SelectAll(String passdate, Context context)
    {
        this.passdate = passdate;
        this.context = context;
        this.selection = " date =  ? ";
        orderby = " _id desc";
        events = new Events(context);
        pending = new Pending(context);
        todolist = new Todolist(context);
        remainder = new Remainder(context);
        selectionArgs = new String[1];
        selectionArgs[0] = passdate;

    }

    public Cursor selectCursor(int choice)
    {
        switch (choice)
        {
            case  0:
                Cursor cursor0 = selectPending();
                return cursor0;

            case  1:
                Cursor cursor1 = selectEvents();
                return cursor1;

            case  2:
                Cursor cursor2 = selectToDoList();
                return cursor2;
            case  3:
                Cursor cursor3 = selectremainder();
                return  cursor3;

        }
        return  null;
    }

    private Cursor selectremainder() {

        SQLiteDatabase db;
        db = remainder.settingDatabase();
        int columnno[] = {0,1,2,3,5,6,7,8};

        Log.d("selectall","selection strign is "+selection);
        Cursor cursor = remainder.select(db,0,columnno,selection,selectionArgs,null,null,orderby);
        Log.d("Selectall", " count of remainder " + cursor.getCount());

        return cursor;
    }

    private Cursor selectToDoList() {
        SQLiteDatabase db;
        db = todolist.settingDatabase();
        int columnno[] = {0,1,2,3,4};

        Log.d("selectall","selection strign is "+selection);
        Cursor cursor = todolist.select(db,0,columnno,selection,selectionArgs,null,null,orderby);
        Log.d("Selectall", " count of todolist " + cursor.getCount());

        return cursor;
    }

    private Cursor  selectEvents() {
        SQLiteDatabase db;
        db = events.settingDatabase();
        String selection   = " fromdate =  ? ";;
        int[] columno = {0,1,3,4,6};

        Cursor cursor1 = events.select(db, 0, columno, selection, selectionArgs, null, null, orderby);

        Log.d("Selectall", " count of events " + cursor1.getCount());
        return cursor1;
    }

    private Cursor selectPending() {
        SQLiteDatabase db;
        db = pending.settingDatabase();

        int[] columnno2 = {0,1,14,12,13,7,8,11};

        String orderby = " _id desc";
        Cursor cursor1 = pending.select(db, 0, columnno2, selection, selectionArgs, null, null, orderby);

         Log.d("Selectall", " count of pending " + cursor1.getCount());
        return cursor1;
    }
}
