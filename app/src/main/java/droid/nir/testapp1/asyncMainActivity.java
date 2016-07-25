package droid.nir.testapp1;

import android.os.AsyncTask;

import java.util.List;

/**
 * Created by user on 7/19/2015.
 *
 * */
 public  class asyncMainActivity extends AsyncTask<Void,Void, List<pending_data>> {
    @Override
    protected List<pending_data> doInBackground(Void... params) {
        return null;
    }


    @Override
    protected void onPostExecute(List<pending_data> aVoid) {
        super.onPostExecute(aVoid);
        //asyncMainActivity2.execute(aVoid);
    }
}

class asyncMainActivity2 extends AsyncTask<List<pending_data>, Void, Void> {
    @Override
    protected Void doInBackground(List<pending_data>... params) {
        return null;
    }
}
