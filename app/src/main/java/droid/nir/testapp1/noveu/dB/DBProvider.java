package droid.nir.testapp1.noveu.dB;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import droid.nir.testapp1.noveu.Util.Log;



/**
 * Created by droidcafe on 2/23/2016.
 */
public class DBProvider  extends ContentProvider{

    public static final String AUTHORITY = "droid.nir.testapp1.noveu.dbprovider";
    public static final String BASE_PATH_TASKS = "tasks";
    public static final Uri CONTENT_URI_TASKS =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_TASKS );


    public static final int ALL_ROWS = 0;
    public static final int GET_ROWS = 1;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH_TASKS + "/*", ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH_TASKS +  "/*/#", GET_ROWS);
    }


    @Override
    public boolean onCreate() {

      ParentDb parentDb =ParentDb.getInstance(getContext());
        SQLiteDatabase database = parentDb.returnSQl();
        Log.d("dbprovider","oncreate "+database);
        return true;
    }



    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        ParentDb parentDb =ParentDb.getInstance(getContext());
        SQLiteDatabase database = parentDb.returnSQl();
        switch (uriMatcher.match(uri))
        {
            case ALL_ROWS:
                //give every task in db
                String tableName = uri.getLastPathSegment();
                Log.d("dbprovider","all tasks "+uri);
                return database.query(tableName,projection,selection,selectionArgs,null,null,sortOrder,null);

            case GET_ROWS:

                Log.d("dbprovider","particular tasks "+uri);
                //particular id
        }

        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        ParentDb parentDb =ParentDb.getInstance(getContext());
        SQLiteDatabase database = parentDb.returnSQl();
        Log.d("dbprovider","insert "+uri);
        String tableName = uri.getLastPathSegment();
        long id = database.insert(tableName,
                null, values);
        return Uri.parse(BASE_PATH_TASKS + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        ParentDb parentDb =ParentDb.getInstance(getContext());
        SQLiteDatabase database = parentDb.returnSQl();
        int rows =  database.delete(uri.getLastPathSegment(), selection, selectionArgs);
      //  database.c
        return  rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        ParentDb parentDb =ParentDb.getInstance(getContext());
        SQLiteDatabase database = parentDb.returnSQl();
        int rows =  database.update(uri.getLastPathSegment(),
                values, selection, selectionArgs);

       // parentDb.close();
        return rows;
    }


}
