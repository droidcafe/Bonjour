package droid.nir.databaseHelper;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;

import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import droid.nir.testapp1.Pending_Recycler_Data;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.dB.metaValues.dBmetaData;
import droid.nir.testapp1.noveu.welcome.Initial;
import droid.nir.testapp1.pending_data;

import droid.nir.testapp1.timecorrection;

/**
 * Created by user on 7/20/2015.
 */
public class MainDatabase extends SQLiteOpenHelper {

    String[][] columnName, columnTypes;
    int[] columnNo;
    ;
    Context context;
    //  toast maketoast;
    String[] CreateSQL, TABLE_NAME, DropSQL;
    SQLiteDatabase db;
    Pending pending;
    Events events;
    Today today;
    RecyclerView mRecyclerView;
    Activity activity;
    int isset =0;
    TextView timetext;
    timecorrection timecorrection;

    private static final String DATABASE_SCHEMA = "MyDataBase";
    private static int DATABASE_VERSION = dBmetaData.DATABASE_VERSION_LEGACY;

    public MainDatabase(Context context, Activity activity) {
        super(context, DATABASE_SCHEMA, null, DATABASE_VERSION);
        this.context = context;
        this.activity = activity;
        pending = new Pending(context);
        events = new Events(context);
        today = new Today(context);
        timecorrection = new timecorrection();

    }


    public void setUp(RecyclerView mRecyclerView, RecyclerView.LayoutManager mLayoutManager, TextView textView) {


        AsyncSetUp asyncSetUp = new AsyncSetUp(mRecyclerView, textView, mLayoutManager);

        asyncSetUp.execute();
    }


    class AsyncSetUp extends AsyncTask<Void, String, List<pending_data>> {

        TextView textView, temptext;
        int visibilty;
        Pending_Recycler_Data mRecyclerAdapter;
        RecyclerView.LayoutManager mLayoutManager;


        AsyncSetUp(RecyclerView mRecyclerVieww, TextView textView, RecyclerView.LayoutManager mLayoutManager) {
            mRecyclerView = mRecyclerVieww;
            this.textView = textView;
            visibilty = 0;

            this.mLayoutManager = mLayoutManager;
        }

        @Override
        protected List<pending_data> doInBackground(Void... params) {


            List<pending_data> arrayList = new ArrayList<>();
            SQLiteDatabase db = settingDatabase(true);

            pending.settingDatabase();
            events.settingDatabase();
            today.settingDatabase();
            int[] columnos = {11, 1};
            int[] columnnos = {0, 1};
            int[] columnoss = {0, 1, 2, 3, 5, 6, 7};
            String orderby = " notifyhr desc, notifymin desc";

            //Cursor cursor = pending.select(db,0,columnos,null,null,null,null,null);
            //Cursor tempcursor  = events.select(db, 0, columnnos, null, null, null, null, null);
            Cursor tempcursor2 = today.select(db, 0, columnoss, null, null, null, null, orderby);

            if (tempcursor2.getCount() == 0) {
                visibilty = 0;
                publishProgress("null");
            } else {
                visibilty = 1;
                Log.d("maindatabase", "The no of items scheduled for today is " + tempcursor2.getCount());
                arrayList = settingupcards(tempcursor2);

            }

            return arrayList;


        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            // Toast.makeText(context,""+values[0],Toast.LENGTH_LONG).show();
            if (values[0].equals("null")) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(R.string.main_description);
                mRecyclerView.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onPostExecute(List<pending_data> pendinglist) {
            super.onPostExecute(pendinglist);
            if (visibilty == 1) {
                textView.setVisibility(View.GONE);
                textView.setText("");
                mRecyclerView.setVisibility(View.VISIBLE);
//               mCardRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerAdapter = new Pending_Recycler_Data(context, pendinglist, activity);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mRecyclerAdapter);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                /*final SwipeToDismissTouchListener<RecyclerViewAdapter> touchListener =
                        new SwipeToDismissTouchListener<>(
                                new RecyclerViewAdapter(mRecyclerView),
                                new SwipeToDismissTouchListener.DismissCallbacks<RecyclerViewAdapter>() {
                                    @Override
                                    public boolean canDismiss(int position) {
                                        return true;
                                    }

                                    @Override
                                    public void onDismiss(RecyclerViewAdapter view, int position) {
                                        mRecyclerAdapter.remove(position);
                                    }
                                });

                mRecyclerView.setOnTouchListener(touchListener);
                mRecyclerView.setOnScrollListener((RecyclerView.OnScrollListener) touchListener.makeScrollListener());
                mRecyclerView.addOnItemTouchListener(new SwipeableItemClickListener(context,
                        new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (view.getId() == R.id.txt_delete) {
                                    touchListener.processPendingDismisses();
                                } else if (view.getId() == R.id.txt_undo) {
                                    touchListener.undoPendingDismiss();
                                } else { // R.id.txt_data
                                    Toast.makeText(context, "Position " + position, Toast.LENGTH_LONG).show();
                                }
                            }
                        }));*/
            }


        }
    }

    public List<pending_data> settingupcards(Cursor tempcursor2) {

        int i = 0;
        List<pending_data> arrayList = new ArrayList<>();

        while (tempcursor2.moveToNext()) {


            pending_data pendingData = new pending_data();
            int isnotify = tempcursor2.getInt(tempcursor2.getColumnIndex("isnotify"));
            pendingData.title = tempcursor2.getString(tempcursor2.getColumnIndex("title"));
            if (isnotify == 0) {
                char ch = pendingData.title.charAt(0);
                ch = Character.toUpperCase(ch);
                pendingData.time = Character.toString(ch);


                pendingData.paddingleft = (int) context.getResources().getDimension(R.dimen.circlepaddingleft2);

            } else {
                int hr = tempcursor2.getInt(tempcursor2.getColumnIndex("notifyhr"));
                int min = tempcursor2.getInt(tempcursor2.getColumnIndex("notifymin"));

                pendingData.time = timecorrection.formatime(hr, min);
                pendingData.paddingleft = (int) context.getResources().getDimension(R.dimen.circlepaddingleft);
            }


            pendingData.type = tempcursor2.getInt(tempcursor2.getColumnIndex("type"));
            pendingData.id = tempcursor2.getInt(tempcursor2.getColumnIndex("_id"));
            pendingData.oid = tempcursor2.getInt(tempcursor2.getColumnIndex("oid"));

            String tempselection = " tid = " + pendingData.id;

            switch (pendingData.type) {
                case 0:
                    int tempcolumno[] = {2};
                    Cursor tempcursor = today.select(db, 1, tempcolumno, tempselection, null, null, null, null);
                    while (tempcursor.moveToNext()) {
                        pendingData.customstatement1 = tempcursor.getString(tempcursor.getColumnIndex("statement"));
                    }
                    break;
                case 1:
                    int tempcolumno1[] = {2};
                    Cursor tempcursor1 = today.select(db, 2, tempcolumno1, tempselection, null, null, null, null);
                    while (tempcursor1.moveToNext()) {
                        pendingData.temp1 = tempcursor1.getInt(tempcursor1.getColumnIndex("iswholeday"));
                        if (pendingData.temp1 == 1) {
                            pendingData.customstatement1 = context.getResources().getString(R.string.ewholeday);
                        } else {

                            int tempcolumno2[] = {2, 3, 4, 5, 6, 7};
                            Cursor tempcursorr = today.select(db, 3, tempcolumno2, tempselection, null, null, null, null);

                            while (tempcursorr.moveToNext()) {
                                pendingData.customstatement1 = tempcursorr.getString(tempcursorr.getColumnIndex("fromdate"));
                                pendingData.customstatement2 = tempcursorr.getString(tempcursorr.getColumnIndex("todate"));
                                int fromhr = tempcursorr.getInt(tempcursorr.getColumnIndex("fromtimehr"));
                                int frommin = tempcursorr.getInt(tempcursorr.getColumnIndex("fromtimemin"));
                                int tohr = tempcursorr.getInt(tempcursorr.getColumnIndex("totimehr"));
                                int tomin = tempcursorr.getInt(tempcursorr.getColumnIndex("totimemin"));
                                pendingData.customstatement3 = timecorrection.formatime(fromhr, frommin);
                                pendingData.customstatement4 = timecorrection.formatime(tohr, tomin);
                            }
                        }

                    }

                    break;
                case 2:
                    int tempcolumno3[] = {2};

                    Cursor tempcursorr = today.select(db, 4, tempcolumno3, tempselection, null, null, null, null);
                    while (tempcursorr.moveToNext()) {
                        //read whethter there is notification and then

                        int listno = tempcursorr.getInt(tempcursorr.getColumnIndex("noofitems"));
                        pendingData.customstatement1 = context.getResources().getString(R.string.youhave) + " " + listno + " " + context.getResources().getString(R.string.noof);
                    }
                    break;

                case 3:
                    int tempcolumno4[] = {2, 3};

                    Cursor tempcursorrr = today.select(db, 5, tempcolumno4, tempselection, null, null, null, null);
                    while (tempcursorrr.moveToNext()) {

                        int isdesp = tempcursorrr.getInt(tempcursorrr.getColumnIndex("isdesp"));
                        if (isdesp == 1) {
                            pendingData.customstatement1 = tempcursorrr.getString(tempcursorrr.getColumnIndex("desp"));
                        } else {
                            pendingData.customstatement1 = context.getResources().getString(R.string.nodesp);
                        }
                    }

                    Log.d("maindatabase", "remainder found of " + pendingData.customstatement1 + " " + tempcursorrr.getCount());
                    break;
            }

            Log.d("maindatabse", "id is");
            arrayList.add(i++, pendingData);
        }

        return arrayList;

    }


    public void intialisecreatesql() {
        CreateSQL = new String[TABLE_NAME.length];

        for (int i = 0; i < TABLE_NAME.length; i++) {
            CreateSQL[i] = "Create table " + TABLE_NAME[i] + " ( " + columnName[i][0] + " INTEGER PRIMARY KEY AUTOINCREMENT , ";

            for (int j = 1; j < columnNo[i]; j++) {
                String temp = columnName[i][j] + " " + columnTypes[i][j] + " ";
                if ((j + 1) != columnNo[i])
                    temp = temp.concat(" , ");

                CreateSQL[i] = CreateSQL[i].concat(temp);
            }
            CreateSQL[i] = CreateSQL[i].concat(" );");

        }
    }

    public void intialisedrop() {
        DropSQL = new String[TABLE_NAME.length];

        for (int i = 0; i < TABLE_NAME.length; i++) {

            DropSQL[i] = "Drop table if exists " + TABLE_NAME[i] + " ;";
        }

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Import.setSharedPref(context,SharedKeys.db_mydatabase, constants.LOCK_CODE);
        Import.setSharedPref(context, SharedKeys.legacyDbVersion_new, dBmetaData.DATABASE_VERSION_LEGACY);
        Import.setSharedPref(context, SharedKeys.legacyDbVersion_old, dBmetaData.DATABASE_VERSION_LEGACY);

        if(isset == 0)
            setVariables();
        intialisecreatesql();
        for (int i = 0; i < CreateSQL.length; i++) {

            try {
                db.execSQL(CreateSQL[i]);
                Log.i("drop ", CreateSQL[i]);


            } catch (SQLException e) {

                Log.d("oncreate ", "exception " + e);
            }


        }
        Import.setSharedPref(context,SharedKeys.db_mydatabase, constants.SUCCESS_CODE);
        int db_ops_status_modes = Import.getSharedPref(context,SharedKeys.db_ops_status);
        Log.d("md","db_ops_mode "+db_ops_status_modes);
        if(db_ops_status_modes == constants.db_ops_status_modes[4]){
            Log.d("md","finished md . db not done because of all ");
            Import.updateSharedPref(context,SharedKeys.db_ops_status, -constants.db_ops_status_modes[3]);
        }else if(db_ops_status_modes == constants.db_ops_status_modes[3]){
            Log.d("md","calling db_ops after main db");
            Import.setSharedPref(context,SharedKeys.db_ops_status, -constants.db_ops_status_modes[3]);
            Initial.startDBops(context);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Import.setSharedPref(context, SharedKeys.legacyDbVersion_new, newVersion);
        Import.setSharedPref(context, SharedKeys.legacyDbVersion_old, oldVersion);

        if(isset == 0)
            setVariables();
        Import.setSharedPref(context, SharedKeys.update, 1);

//        intialisedrop();
//        for (int i = 0; i < DropSQL.length; i++) {
//            try {
//
//              //  db.execSQL(DropSQL[i]);
//                //Log.d("drop ", DropSQL[i]);
//                //  Toast.makeText(context, "Upgrade" + DropSQL[i], Toast.LENGTH_LONG).show();
//
//            } catch (SQLException e) {
//                // Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show();
//                Log.i("onUpgrade", "exception " + e);
//            }
//        }

        //  onCreate(db);

    }

    public SQLiteDatabase settingDatabase(boolean setVariable) {

        Log.d("", "calling setting database");
        if (setVariable) {
            setVariables();
        }

        db = getWritableDatabase();
        Log.d("", "");
        return db;
    }

    private void setVariables() {
        String columnName[][] = {
         /*1*/  {"_id", "title", "desp", "leaning", "decide", "nopros", "nocons", "decisiontype", "custom", "extas", "implevel", "date", "timehr", "timemin", "notificationtype", "done"},
                {"_id", "pid", "description"},
                {"_id", "pid", "pro"},
                {"_id", "pid", "con"},
                {"_id", "pid", "custom"},
                {"_id", "pid", "extratype", "extra"},
         /*2*/  {"_id", "title", "location", "wholeday", "notify", "notes", "date", "done", "noofdays", "fromdate"},
                {"_id", "eid", "fromdate", "todate", "fromtimehr", "fromtimemin", "totimehr", "totimemin"},
                {"_id", "eid", "timehr", "timemin", "alarm"},
                {"_id", "eid", "note"},
         /*3*/  {"_id", "type", "oid", "title", "isalarm", "isnotify", "notifyhr", "notifymin"},
                {"_id", "tid", "statement"},
                {"_id", "tid", "iswholeday"},
                {"_id", "tid", "fromdate", "fromtimehr", "fromtimemin", "todate", "totimehr", "totimemin"},
                {"_id", "tid", "noofitems"},
                {"_id", "tid", "isdesp", "desp"},
          /*4*/ {"_id", "title", "notification", "listno", "date", "done"},
                {"_id", "tid", "listitem"},
                {"_id", "tid", "nhr", "nmin"},
           /*5*/{"_id", "title", "isdesp", "desp", "date", "done", "nothr", "notmin", "isalarm"}

        };
        this.columnName = columnName;
        String columnTypes[][] = {
            /*1*/{"INTEGER", "VARCHAR(255)", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "VARCHAR(255)", "INTEGER", "INTEGER", "INTEGER", "VARCHAR(255)", "INTEGER", "INTEGER", "INTEGER", "INTEGER"},
                {"INTEGER", "INTEGER", "VARCHAR(5000)"},
                {"INTEGER", "INTEGER", "VARCHAR(1000)"},
                {"INTEGER", "INTEGER", "VARCHAR(1000)"},
                {"INTEGER", "INTEGER", "VARCHAR(255)"},
                {"INTEGER", "INTEGER", "VARCHAR(255)", "VARCHAR(1000)"},
            /*2*/{"INTEGER", "VARCHAR(100)", "VARCHAR(250)", "INTEGER", "INTEGER", "INTEGER", "VARCHAR(40)", "INTEGER", "INTEGER", "VARCHAR(40)"},
                {"INTEGER", "INTEGER", "VARCHAR(100)", "INTEGER", "INTEGER", "VARCHAR(100)", "INTEGER", "INTEGER"},
                {"INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER"},
                {"INTEGER", "INTEGER", "VARCHAR(1000)"},
            /*3*/{"INTEGER", "INTEGER", "INTEGER", "VARCHAR(200)", "INTEGER", "INTEGER", "INTEGER", "INTEGER"},
                {"INTEGER", "INTEGER", "VARCHAR(500)"},
                {"INTEGER", "INTEGER", "INTEGER"},
                {"INTEGER", "INTEGER", "VARCHAR(50)", "INTEGER", "INTEGER", "VARCHAR(50)", "INTEGER", "INTEGER"},
                {"INTEGER", "INTEGER", "INTEGER"},
                {"INTEGER", "INTEGER", "INTEGER", "VARCHAR(250)"},
            /*4*/{"INTEGER", "VARCHAR(100)", "INTEGER", "INTEGER", "VARCHAR(20)", "INTEGER"},
                {"INTEGER", "INTEGER", "VARCHAR(500)"},
                {"INTEGER", "INTEGER", "INTEGER", "INTEGER"},
            /*5*/{"INTEGER", "VARCHAR(100)", "INTEGER", "VARCHAR(100)", "VARCHAR(100)", "INTEGER", "INTEGER", "INTEGER", "INTEGER"}
        };

        this.columnTypes = columnTypes;


        int columnNo0[] = {16, 3, 3, 3, 3, 4, 10, 8, 5, 3, 8, 3, 3, 8, 3, 4, 6, 3, 4, 9};
        columnNo = columnNo0;
        String[] tableName0 = {"pending", "desp", "pros", "cons", "customs", "extras", "event", "timeevent", "notify", "notes", "today", "todaypending", "todayevent1", "todayevent2", "todaylist", "todayremainder", "todolist", "listitems", "tnotify", "remainder"};

        TABLE_NAME = tableName0;

        isset =1;

    }


}
