package droid.nir.testapp1.noveu.welcome.help.ui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.FirebaseUtil;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.ui.util.ProgressDialog;
import droid.nir.testapp1.noveu.welcome.data.Help;
import droid.nir.testapp1.noveu.welcome.help.HelpAdapter;

public class TaskHelp extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_help);

        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title));
     /*   Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title2));
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
        FirebaseUtil.recordScreenView(this, "help task", mFirebaseAnalytics);
        Bundle fireBundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseUtil.help_task, fireBundle);

        setupHelpList();
    }

    private void setupHelpList() {
        new AsyncLoad(this, this).execute();

    }

    static class AsyncLoad extends AsyncTask<Void, Void, HashMap<String, String>> implements ValueEventListener {

        ProgressDialog progressDialog;
        Activity activity;
        Context context;
        String config;

        AsyncLoad(Context context, Activity activity) {
            progressDialog = new ProgressDialog(context);
            this.activity = activity;
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.showProgressDialog();
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... voids) {

            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            final HashMap<String, String> helpMap = new HashMap<>();
            Query helpQ = mRootRef.child("help/tasks").child("list");
            mRootRef.child("help/tasks").child("config").addListenerForSingleValueEvent(this);

          /*  helpQ.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("th", "on data change list " + dataSnapshot.getChildrenCount());

                    for (DataSnapshot helpSnapShot : dataSnapshot.getChildren()) {
                        droid.nir.testapp1.noveu.welcome.data.Help help = helpSnapShot.getValue(Help.class);
                        Log.d("th", "data " + help.title + " " + help.desp);
                        helpMap.put(help.title, help.desp);
                    }

                    progressDialog.disMissProgressDialog();
                    HelpAdapter helpAdapter = new HelpAdapter(helpMap);
                    RecyclerView helpList = (RecyclerView) activity.findViewById(R.id.help_list);
                    helpList.setLayoutManager(new LinearLayoutManager(context));
                    helpList.setHasFixedSize(true);

                    helpList.setAdapter(helpAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("th", "cancelled " + databaseError.getMessage());
                }
            });*/

            return helpMap;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> helpMap) {
            super.onPostExecute(helpMap);


        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d("th","data of "+dataSnapshot.getKey()+" "+dataSnapshot.getRef().toString());
            switch (dataSnapshot.getKey()){
                case "config":
                    config = (String) dataSnapshot.getValue();
                    Log.d("th","config is "+config);
                    break;
                case "list":
                    HashMap<String ,Help > helpMap = new HashMap<>();
                    Log.d("th", "on data change list " + dataSnapshot.getChildrenCount());

                    for (DataSnapshot helpSnapShot : dataSnapshot.getChildren()) {
                        droid.nir.testapp1.noveu.welcome.data.Help help = helpSnapShot.getValue(Help.class);
                        Log.d("th", "data " + help.title + " " + help.desp);
                        helpMap.put(helpSnapShot.getKey(),help);
                    }

                    progressDialog.disMissProgressDialog();
                    HelpAdapter helpAdapter = new HelpAdapter(helpMap,config);
                    RecyclerView helpList = (RecyclerView) activity.findViewById(R.id.help_list);
                    helpList.setLayoutManager(new LinearLayoutManager(context));
                    helpList.setHasFixedSize(true);

                    helpList.setAdapter(helpAdapter);
                    break;
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                String mailid[] = {constants.dev_mail};
                Import.composeEmail(this, mailid, "Help [Tasks]", null);
                break;
        }
    }
}
