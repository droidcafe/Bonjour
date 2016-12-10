package droid.nir.testapp1.noveu.welcome.help.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.FirebaseUtil;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.welcome.help.HelpLoad;

public class PendingHelp extends AppCompatActivity implements View.OnClickListener {

    FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_help);

        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        findViewById(R.id.refresh).setOnClickListener(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseUtil.recordScreenView(this,"help pending",mFirebaseAnalytics);

        Bundle fireBundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseUtil.help_pending,fireBundle);
        setupHelpList();
    }

    private void setupHelpList() {
        Log.d("ph","setting up list");
        new HelpLoad(this,this).load("decisions");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                String mailid[] = {constants.dev_mail};
                Import.composeEmail(this,mailid,"Help [Pending Decisions]",null);
                break;
            case R.id.refresh:
                setupHelpList();
                break;
        }
    }


}
