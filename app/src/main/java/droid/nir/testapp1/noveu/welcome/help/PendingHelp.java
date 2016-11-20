package droid.nir.testapp1.noveu.welcome.help;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.constants.constants;

public class PendingHelp extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_help);
        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title));
        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title2));
        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title3));
        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title4));
        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.title5));

        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.desp1));
        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.desp2));
        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.desp3));
        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.desp4));
        Import.settypefaces(this, "SourceSansPro-Regular.otf", (TextView) findViewById(R.id.desp5));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                String mailid[] = {constants.dev_mail};
                Import.composeEmail(this,mailid,"Help [Pending Decisions]",null);
                break;
        }
    }


}
