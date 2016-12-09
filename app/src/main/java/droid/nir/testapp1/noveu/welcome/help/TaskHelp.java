package droid.nir.testapp1.noveu.welcome.help;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.FirebaseUtil;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.ui.util.ProgressDialog;

public class TaskHelp extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_help);

       /* Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title));
        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title2));
        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title3));
        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title4));
        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title5));
        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title6));
        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title7));

        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.desp1));
        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.desp2));
        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.desp3));
        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.desp4));
        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.desp5));
        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.desp6));
        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.desp7));

*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseUtil.recordScreenView(this,"help task",mFirebaseAnalytics);
        Bundle fireBundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseUtil.help_task,fireBundle);

        setupHelpList();
    }

    private void setupHelpList() {
        new AsyncLoad(this).execute();
    }

    static class AsyncLoad extends AsyncTask<Void,Void,HashMap<String,String>> {

        ProgressDialog progressDialog;
        String desp;

        AsyncLoad(Context context) {
            progressDialog = new ProgressDialog(context);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.showProgressDialog();
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... voids) {

            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            mRootRef.child("tasks").child("config").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("th","on data change config");
                    String config = (String) dataSnapshot.getValue();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });






            return null;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                String mailid[] = {constants.dev_mail};
                Import.composeEmail(this,mailid,"Help [Tasks]",null);
                break;
        }
    }
}
