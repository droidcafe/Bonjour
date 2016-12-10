package droid.nir.testapp1.noveu.welcome.help;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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
import droid.nir.testapp1.noveu.Util.NetworkUtil;
import droid.nir.testapp1.noveu.ui.util.ProgressDialog;
import droid.nir.testapp1.noveu.welcome.data.Help;

/**
 * Created by droidcafe on 12/10/2016.
 */

public class HelpLoad implements ValueEventListener {

    private ProgressDialog progressDialog;
    private Activity activity;
    private Context context;
    private String config, helpType;
    FirebaseAnalytics mFirebaseAnalytics;

    public HelpLoad(Context context, Activity activity) {
        progressDialog = new ProgressDialog(context);
        this.activity = activity;
        this.context = context;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

    }

    public void load(String helpType) {
        this.helpType = helpType;

        if (!NetworkUtil.isNetworkAvailable(context)) {
            mFirebaseAnalytics.logEvent(FirebaseUtil.network_error,new Bundle());
            helpFailure(R.string.network_error);
            return;
        }
        progressDialog.showProgressDialog();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("help/" + helpType).child("config").addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d("hl", "data of " + dataSnapshot.getKey() );
        switch (dataSnapshot.getKey()) {
            case "config":
                config = (String) dataSnapshot.getValue();
                Log.d("hl", "config is " + config);

                if (config == null || config.equals("")) {
                    Log.d("hl","config not present");
                    helpFailure(R.string.help_error);
                    return;
                }
                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                mRootRef.child("help/" + helpType).child("list").addListenerForSingleValueEvent(this);
                break;
            case "list":
                HashMap<String, Help> helpMap = new HashMap<>();
                Log.d("hl", "on data change list " + dataSnapshot.getChildrenCount());

                for (DataSnapshot helpSnapShot : dataSnapshot.getChildren()) {
                    droid.nir.testapp1.noveu.welcome.data.Help help = helpSnapShot.getValue(Help.class);
                    Log.d("hl", "data " + help.title + " " + help.desp);
                    helpMap.put(helpSnapShot.getKey(), help);
                }


                if (helpMap.isEmpty()) {
                    helpFailure(R.string.help_error);
                    return;
                }
                helpSuccess(helpMap);

                break;
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("hl", "cancelled " + databaseError.getMessage());

        helpFailure(R.string.help_error);
    }

    void helpFailure(int errorTextid) {

        progressDialog.disMissProgressDialog();

        String errorText = Import.getString(context,errorTextid);
        TextView hiddenText = (TextView) activity.findViewById(R.id.hiddentext);
        hiddenText.setVisibility(View.VISIBLE);
        activity.findViewById(R.id.refresh).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.help_list).setVisibility(View.GONE);

        hiddenText.setText(errorText);

        mFirebaseAnalytics.logEvent(FirebaseUtil.help_error,new Bundle());

    }

    void helpSuccess(HashMap<String, Help> helpMap) {

        progressDialog.disMissProgressDialog();

        RecyclerView helpList = (RecyclerView) activity.findViewById(R.id.help_list);

        helpList.setVisibility(View.VISIBLE);
        activity.findViewById(R.id.refresh).setVisibility(View.GONE);
        activity.findViewById(R.id.hiddentext).setVisibility(View.GONE);

        HelpAdapter helpAdapter = new HelpAdapter(helpMap, config);
        helpList.setLayoutManager(new LinearLayoutManager(context));
        helpList.setHasFixedSize(true);

        helpList.setAdapter(helpAdapter);
    }
}
