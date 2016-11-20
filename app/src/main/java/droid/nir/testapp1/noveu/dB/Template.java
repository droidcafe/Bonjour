package droid.nir.testapp1.noveu.dB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import droid.nir.testapp1.toast;

/**
 * Created by droidcafe on 2/23/2016.
 */
public class Template {

    Context context;
    public static String[][] columnNames = {

    };
    public static String[][] columnTypes = {
    };
    public static String[] tableNames = {};
    public static int[] columnNos = {};
    toast maketext;

    public Template(Context context)
    {
        this.context =context;
        maketext = new toast(context);

    }


    public void update(String[] values, int intvalues[], SQLiteDatabase db,int oid)
    {

        Asyncupdate asyncupdate = new Asyncupdate();
        asyncupdate.execute();
    }

    public class  Asyncupdate extends AsyncTask<Void,Void,Void>
    {

        public Asyncupdate() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

    }


    public void insert() {
        Asyncinsert asyncinsert = new Asyncinsert();
        asyncinsert.execute();
    }

    class  Asyncinsert extends AsyncTask<Void,Void,Long>
    {

        public Asyncinsert() {

        }

        @Override
        protected Long doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
        }
    }


}


