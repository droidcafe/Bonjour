package droid.nir.testapp1.noveu.dB;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.dB.metaValues.dBmetaData;
import droid.nir.testapp1.noveu.welcome.Initial;

/**
 * Created by user on 12/25/2015.
 */
public class ParentDb extends SQLiteOpenHelper {

    String[][] columnName;
    String[][] columnTypes;
    int[] tableLength;
    String[] TABLE_NAME;
    Context context;
    int ifsset = 0;

    boolean droptable = false;
    private static ParentDb mInstance = null;

    public ParentDb(Context context) {
        super(context, dBmetaData.DATABASE_SCHEMA, null, dBmetaData.DATABASE_VERSION);
        this.context = context;

    }

    public ParentDb(Context context, Activity activity) {
        super(context, dBmetaData.DATABASE_SCHEMA, null, dBmetaData.DATABASE_VERSION);
        this.context = context;

    }

    public static ParentDb getInstance(Context ctx) {

        if (mInstance == null) {
            mInstance = new ParentDb(ctx.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Import.setSharedPref(context,SharedKeys.db_bonjour, constants.LOCK_CODE);

        if (ifsset < 1) {
            settingDatabase();
            Import.setSharedPref(context, SharedKeys.DbVersion, dBmetaData.DATABASE_VERSION);
            Import.setSharedPref(context, SharedKeys.DbVersion_old, dBmetaData.DATABASE_VERSION);
        }
        dBmetaData.setExtras();
        create(sqLiteDatabase);

        Import.setSharedPref(context,SharedKeys.db_bonjour, constants.SUCCESS_CODE);
        int db_ops_status_modes = Import.getSharedPref(context,SharedKeys.db_ops_status);
        Log.d("pd","db_ops_mode "+db_ops_status_modes);
        if(db_ops_status_modes == constants.db_ops_status_modes[4]){
            Log.d("pd","finished pd . db not done because of all ");
            Import.updateSharedPref(context,SharedKeys.db_ops_status, -constants.db_ops_status_modes[2]);
        }else if(db_ops_status_modes == constants.db_ops_status_modes[2]){
            Log.d("pd","calling pd after parent db");
            Import.setSharedPref(context,SharedKeys.db_ops_status, -constants.db_ops_status_modes[2]);
            Initial.startDBops(context);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Import.setSharedPref(context, SharedKeys.update, 1);
        if (ifsset < 1) {
            settingDatabase();
            Import.setSharedPref(context, SharedKeys.DbVersion, newVersion);
            Import.setSharedPref(context, SharedKeys.DbVersion_old, oldVersion);
        }
        Log.d("Parentdb", "New ver " + newVersion + " old ver : " + oldVersion);
        droptable = false;
        if (droptable) {
            drop(sqLiteDatabase);
            onCreate(sqLiteDatabase);
        } else {
            //   new AsyncCreate().execute(sqLiteDatabase);
        }
    }

    public void create(SQLiteDatabase db) {
        String[] CreateSQL = intialisecreatesql();
        for (int i = 0; i < CreateSQL.length; i++) {
            try {
                db.execSQL(CreateSQL[i]);
                Log.d("Parentdb", "created " + CreateSQL[i]);

            } catch (SQLException e) {
                Log.i("oncreate ", "exception " + e);
            }
        }
    }

    public void drop(SQLiteDatabase db) {

        String[] DropSQL = intialisedrop();
        for (int i = 0; i < DropSQL.length; i++) {
            try {

                db.execSQL(DropSQL[i]);
                Log.d("Parentdb", "Dropped " + DropSQL[i]);

            } catch (SQLException e) {
                Log.i("onUpgrade", "exception " + e);
            }
        }
    }




    public String[] intialisecreatesql() {
        String[] CreateSQL = new String[TABLE_NAME.length];

        int column_index = 0;
        for (int i = 0; i < TABLE_NAME.length; i++) {
            CreateSQL[i] = "Create table if not exists " + TABLE_NAME[i] + " ( " + columnName[i][0] + " INTEGER PRIMARY KEY AUTOINCREMENT , ";

            for (int j = 1; j < tableLength[i]; j++) {
                String temp = columnName[i][j] + " " + columnTypes[i][j] + " ";
                String extra = dBmetaData.extra_constraints.get(column_index++);
                if (extra != null) {
                    temp = temp.concat(extra + " ");
                }

                if ((j + 1) != tableLength[i])
                    temp = temp.concat(" , ");

                CreateSQL[i] = CreateSQL[i].concat(temp);
            }
            CreateSQL[i] = CreateSQL[i].concat(" );");
            Log.d("Parentdb", "" + CreateSQL[i]);
        }

        return CreateSQL;
    }

    public String[] intialisedrop() {
        String[] DropSQL = new String[TABLE_NAME.length];

        for (int i = 0; i < TABLE_NAME.length; i++) {

            DropSQL[i] = "Drop table if exists " + TABLE_NAME[i] + " ;";
            Log.d("Parentdb", "" + DropSQL[i]);
        }
        return DropSQL;
    }

    public SQLiteDatabase returnSQl() {
        return getWritableDatabase();
    }

    public void intialise(String TABLE_NAME[], int columnNo[], String[][] columnName, String[][] columnTypes) {
        Log.d("parentdb", "intialise ");
        this.columnName = columnName;
        this.TABLE_NAME = TABLE_NAME;
        this.columnTypes = columnTypes;
        this.tableLength = columnNo;

    }

    public void settingDatabase(String[][] columnNames, String[][] columnTypes, String[] tableNames, int[] tableLenth) {
        ifsset = 1;
        this.columnName = columnNames;
        this.columnTypes = columnTypes;
        this.tableLength = tableLenth;
        this.TABLE_NAME = tableNames;
    }

    public void settingDatabase() {
        ifsset = 1;
        columnName = dBmetaData.columnNames;
        columnTypes = dBmetaData.columnTypes;
        tableLength = dBmetaData.tableLenth;
        TABLE_NAME = dBmetaData.tableNames;
    }


    public long insert(SQLiteDatabase db, ContentValues contentValues, String tableName) {
        try {

            return db.insert(tableName, null, contentValues);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("Parend", "Error " + e);
        }
        return 0;
    }

    public int update(SQLiteDatabase db, String tableName, ContentValues contentValues, String selection, String[] selectionArgs) {

        if (db != null) {
            return db.update(tableName, contentValues, selection, selectionArgs);
        }

        return -1;
    }

    public Cursor select(SQLiteDatabase db, String tableName, String[] columnNamesRequired, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        if (db != null) {

            return db.query(tableName, columnNamesRequired, selection, selectionArgs, groupBy, having, orderBy);
        } else
            return null;

    }

    public Cursor select(SQLiteDatabase db, boolean distinct, String tableName, String[] columnNamesRequired, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        if (db != null) {

            return db.query(distinct, tableName, columnNamesRequired, selection, selectionArgs, groupBy, having, orderBy, limit);
        } else
            return null;

    }

    public Cursor rawQuery(SQLiteDatabase db, String selection, String selectionArgs[]) {
        if (db != null) {
            return db.rawQuery(selection, selectionArgs);
        }

        return null;
    }



    class AsyncCreate extends AsyncTask<SQLiteDatabase, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dBmetaData.setExtras();

        }

        @Override
        protected Void doInBackground(SQLiteDatabase... db) {


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("oncreate", "Created Successfully");
           // Initial.startDBops(context);
        }
    }


    class AsyncDrop extends AsyncTask<SQLiteDatabase, Void, SQLiteDatabase> {
        @Override
        protected SQLiteDatabase doInBackground(SQLiteDatabase... params) {

            return params[0];
        }

        @Override
        protected void onPostExecute(SQLiteDatabase db) {
            super.onPostExecute(db);
            //   Toast.makeText(context, "Upgrade", Toast.LENGTH_LONG).show();
            Log.i("onUpgrade", "upgrade successfully");
            onCreate(db);
        }
    }


}

