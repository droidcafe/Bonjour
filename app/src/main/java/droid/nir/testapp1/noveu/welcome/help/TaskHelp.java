package droid.nir.testapp1.noveu.welcome.help;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.FirebaseUtil;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.constants.constants;

public class TaskHelp extends AppCompatActivity implements View.OnClickListener {

    FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_help);

        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title));
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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseUtil.recordScreenView(this,"help task",mFirebaseAnalytics);

        Bundle fireBundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseUtil.help_task,fireBundle);
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
