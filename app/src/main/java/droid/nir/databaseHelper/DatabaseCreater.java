package droid.nir.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import droid.nir.testapp1.noveu.Util.Log;
import android.widget.Toast;

import droid.nir.testapp1.noveu.dB.metaValues.dBmetaData;
import droid.nir.testapp1.toast;

/**
 * Created by user on 7/4/2015.
 */
public class DatabaseCreater extends SQLiteOpenHelper {

    private static final String DATABASE_SCHEMA = "MyDataBase";
    private  static int DATABASE_VERSION = dBmetaData.DATABASE_VERSION_LEGACY;
    private String CreateSQL[];
    private  String TABLE_NAME[];
    private  String DropSQL[];
    private  int[] columnNo;
    private Context context;
    public toast maketoast;
    private   String[][] columnName,columnTypes;
    private ContentValues contentValues;
    private SQLiteDatabase db;

    public DatabaseCreater(Context context)
    {
        super(context, DATABASE_SCHEMA, null, DATABASE_VERSION);
        this.context = context;
        maketoast = new toast(context);

    }

    public void intialisecreatesql()
    {
        CreateSQL = new String[TABLE_NAME.length];

        for(int i=0;i<TABLE_NAME.length;i++)
        {
            CreateSQL[i] = "Create table "+ TABLE_NAME[i] +" ( "+columnName[i][0] +" INTEGER PRIMARY KEY AUTOINCREMENT , ";
            DropSQL[i] = "Drop table if exists "+ TABLE_NAME[i] +" ;" ;
            for(int j=1;j<columnNo[i];j++)
            {
                String temp = columnName[i][j] +" " + columnTypes[i][j]+ " ";
                if((j+1)!=columnNo[i])
                    temp =  temp.concat(" , ");

                CreateSQL[i] = CreateSQL[i].concat(temp);
            }
            CreateSQL[i] = CreateSQL[i].concat(" );");
//            maketoast.akeText(CreateSQL[i]);
        }
    }

    public  void intialisedrop()
    {
        DropSQL = new String[TABLE_NAME.length];

        for(int i=0;i<TABLE_NAME.length;i++) {

            DropSQL[i] = "Drop table if exists " + TABLE_NAME[i] + " ;";
        }

    }


    public void intialise(String TABLE_NAME[], int columnNo[], String[][] columnName,String[][] columnTypes,int databaseversionNumber)
    {

       // DATABASE_VERSION = databaseversionNumber;
        //this.columnName = new String[TABLE_NAME.length][];
        this.columnName = columnName;
        this.TABLE_NAME = TABLE_NAME;
        this.columnTypes = columnTypes;
        this.columnNo = columnNo;



    }

    //CreateSQL  = "Create table "+ TABLE_NAME +" ( " +column0 +" INTEGER PRIMARY KEY AUTOINCREMENT , " + column1 +" VARCHAR(555));

    class AsyncCreate extends AsyncTask<SQLiteDatabase , Void,Void> {
        @Override
        protected Void doInBackground(SQLiteDatabase... db) {
            intialisecreatesql();
            SQLiteDatabase dbb = db[0];
            for (int i = 0; i < CreateSQL.length; i++) {
              //  maketoast.akeText(CreateSQL[i]);
                try {
                    dbb.execSQL(CreateSQL[i]);

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
            Toast.makeText(context, "oncreate the tble", Toast.LENGTH_LONG).show();
            Log.i("oncreate", "Created Successfully");
        }
    }


    class  AsyncUpgrade extends AsyncTask<Object,Void,Void>
    {
        @Override
        protected Void doInBackground(Object... params) {

            SQLiteDatabase db = (SQLiteDatabase) params[0];

            intialisedrop();
            for(int i=0;i<DropSQL.length;i++)
            {
                try {

                   // db.execSQL(DropSQL[i]);

                } catch (SQLException e) {
                  //  Toast.makeText(context, ""+e, Toast.LENGTH_LONG).show();
                    Log.i("onUpgrade", "exception "+e);
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Toast.makeText(context, "Upgrade", Toast.LENGTH_LONG).show();
            Log.i("onUpgrade", "upgrade successfully");
         //   onCreate(db);
            super.onPostExecute(aVoid);
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        AsyncCreate asyncCreate = new AsyncCreate();
        asyncCreate.execute(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

        AsyncUpgrade asyncUpgrade = new AsyncUpgrade();
        asyncUpgrade.execute(db);


    }
    public long insert(SQLiteDatabase db , ContentValues contentValues , String  tableName)
    {
        try {

            return db.insert(tableName, null, contentValues);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            maketoast.makeText(""+e);
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

            return db.query(tableName,columnNamesRequired,selection,selectionArgs,groupBy,having,orderBy);
        }
        else
            return null;

    }

    public int deleterow(SQLiteDatabase db,String tablename,String whereclause, String[] whereArgs)
    {
        return  db.delete(tablename ,whereclause, whereArgs);
    }



}
