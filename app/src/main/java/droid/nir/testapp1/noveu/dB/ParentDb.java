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
import droid.nir.testapp1.noveu.dB.initial.inital_projects;
import droid.nir.testapp1.noveu.dB.metaValues.dBmetaData;

/**
 * Created by user on 12/25/2015.
 */
public class ParentDb extends SQLiteOpenHelper {

    String[][] columnName;
    String[][] columnTypes;
    int[] tableLength;
    String[] TABLE_NAME;
    Context context;
    int ifsset =0;

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

        if(ifsset<1)
        {
            settingDatabase();
            Import.setSharedPref(context, SharedKeys.DbVersion, dBmetaData.DATABASE_VERSION);
            Import.setSharedPref(context, SharedKeys.DbVersion_old, dBmetaData.DATABASE_VERSION);
        }
        AsyncCreate asyncCreate = new AsyncCreate();
        asyncCreate.execute(sqLiteDatabase);


    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if(ifsset<1)
        {
            settingDatabase();
            Import.setSharedPref(context, SharedKeys.DbVersion, newVersion);
            Import.setSharedPref(context, SharedKeys.DbVersion_old,oldVersion);
        }
        Log.d("Parentdb", "New ver " + newVersion + " old ver : " + oldVersion);
        droptable = true;
        if(droptable)
        {

          //  new AsyncDrop().execute(sqLiteDatabase);
        }
        else{
            new AsyncCreate().execute(sqLiteDatabase);
        }
    }

    class AsyncCreate extends AsyncTask<SQLiteDatabase , Void,Void> {
        @Override
        protected Void doInBackground(SQLiteDatabase... db) {
            String[] CreateSQL =  intialisecreatesql();
            SQLiteDatabase dbb = db[0];
            for (int i = 0; i < CreateSQL.length; i++) {
                //  maketoast.akeText(CreateSQL[i]);
                try {
                    dbb.execSQL(CreateSQL[i]);
                    Log.d("Parentdb", "created " + CreateSQL[i]);

                } catch (SQLException e) {
                    //  Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show();
                    Log.i("oncreate ", "exception " + e);
                }


            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
          //  Toast.makeText(context, "oncreate the tble", Toast.LENGTH_LONG).show();
          //  Log.i("oncreate", "Created Successfully");


           new inital_projects().execute(context);
        }
    }


    class  AsyncDrop extends AsyncTask<SQLiteDatabase,Void,SQLiteDatabase>
    {
        @Override
        protected SQLiteDatabase doInBackground(SQLiteDatabase... params) {

            SQLiteDatabase db =  params[0];

            String[] DropSQL = intialisedrop();
            for(int i=0;i<DropSQL.length;i++)
            {
                try {

                    db.execSQL(DropSQL[i]);
                    Log.d("Parentdb", "Dropped "+DropSQL[i]);

                } catch (SQLException e) {
                    //  Toast.makeText(context, ""+e, Toast.LENGTH_LONG).show();
                    Log.i("onUpgrade", "exception "+e);
                }

            }

            return db;
        }

        @Override
        protected void onPostExecute(SQLiteDatabase db) {
            super.onPostExecute(db);
         //   Toast.makeText(context, "Upgrade", Toast.LENGTH_LONG).show();
            Log.i("onUpgrade", "upgrade successfully");
            onCreate(db);

        }
    }

    public String[] intialisecreatesql() {
       String[] CreateSQL = new String[TABLE_NAME.length];

        for (int i = 0; i < TABLE_NAME.length; i++) {
            CreateSQL[i] = "Create table if not exists " + TABLE_NAME[i] + " ( " + columnName[i][0] + " INTEGER PRIMARY KEY AUTOINCREMENT , ";

            for (int j = 1; j < tableLength[i]; j++) {
                String temp = columnName[i][j] + " " + columnTypes[i][j] + " ";
                if ((j + 1) != tableLength[i])
                    temp = temp.concat(" , ");

                CreateSQL[i] = CreateSQL[i].concat(temp);
            }
            CreateSQL[i] = CreateSQL[i].concat(" );");
            Log.d("Parentdb",""+CreateSQL[i]);
        }

        return CreateSQL;
    }

    public String[] intialisedrop() {
      String[]  DropSQL = new String[TABLE_NAME.length];

        for (int i = 0; i < TABLE_NAME.length; i++) {

            DropSQL[i] = "Drop table if exists " + TABLE_NAME[i] + " ;";
            Log.d("Parentdb",""+DropSQL[i]);
        }
        return DropSQL;
    }

    public  SQLiteDatabase returnSQl()
    {
        return getWritableDatabase();
    }

    public void intialise(String TABLE_NAME[], int columnNo[], String[][] columnName,String[][] columnTypes)
    {
        Log.d("parentdb","intialise ");
        this.columnName = columnName;
        this.TABLE_NAME = TABLE_NAME;
        this.columnTypes = columnTypes;
        this.tableLength = columnNo;

    }
    public void settingDatabase(String[][] columnNames , String[][] columnTypes, String[] tableNames, int[] tableLenth)
    {
        ifsset =1;
        this.columnName = columnNames;
        this.columnTypes = columnTypes;
        this.tableLength = tableLenth;
        this.TABLE_NAME = tableNames;
    }

    public void settingDatabase()
    {
        ifsset=1;
        columnName = dBmetaData.columnNames;
        columnTypes = dBmetaData.columnTypes;
        tableLength = dBmetaData.tableLenth;
        TABLE_NAME = dBmetaData.tableNames;
    }



    public long insert(SQLiteDatabase db , ContentValues contentValues , String  tableName)
    {
        try {

            return db.insert(tableName, null, contentValues);

        } catch (Exception e) {
            // TODO Auto-generated catch block
           Log.e("Parend","Error "+e);
        }
        return 0;
    }

    public  int update(SQLiteDatabase db, String tableName, ContentValues contentValues , String selection , String[] selectionArgs)
    {

        if(db!=null)
        {
            return db.update(tableName,contentValues,selection,selectionArgs);
        }

        return  -1;
    }

    public Cursor select(SQLiteDatabase db, String tableName,String[] columnNamesRequired, String selection, String[] selectionArgs,String groupBy,String having,String orderBy)
    {
        if(db!=null)
        {

            return db.query(tableName, columnNamesRequired, selection, selectionArgs, groupBy, having, orderBy);
        }
        else
            return null;

    }

    public Cursor select(SQLiteDatabase db,boolean distinct, String tableName,String[] columnNamesRequired, String selection, String[] selectionArgs,String groupBy,String having,String orderBy,String limit)
    {
        if(db!=null)
        {

            return db.query(distinct,tableName, columnNamesRequired, selection, selectionArgs, groupBy, having, orderBy,limit);
        }
        else
            return null;

    }




}

