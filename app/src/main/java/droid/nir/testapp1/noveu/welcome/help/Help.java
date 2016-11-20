package droid.nir.testapp1.noveu.welcome.help;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;

public class Help extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help3);

        Import.settypefaces(this, "Raleway-Medium.ttf", (TextView) findViewById(R.id.pendingdecision));
        Import.settypefaces(this, "Raleway-Medium.ttf", (TextView) findViewById(R.id.tasks));
        Import.settypefaces(this, "Raleway-Medium.ttf", (TextView) findViewById(R.id.events));
        Import.settypefaces(this, "Raleway-Medium.ttf", (TextView) findViewById(R.id.labels));

        Import.setBackGroundColor(this, this, R.id.tasks_layout, R.color.hprimary_secondary);
        Import.setBackGroundColor(this, this, R.id.event_layout, R.color.hprimary_secondary);

        findViewById(R.id.tasks).setOnClickListener(this);
        findViewById(R.id.pendingdecision).setOnClickListener(this);
        findViewById(R.id.events).setOnClickListener(this);
        findViewById(R.id.labels).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tasks:
                startActivity(new Intent(this, TaskHelp.class));
                break;
            case R.id.pendingdecision:
                startActivity(new Intent(this, PendingHelp.class));
                break;
            case R.id.events:
                startActivity(new Intent(this, EventHelp.class));
                break;
            case R.id.labels:
                startActivity(new Intent(this,LabelHelp.class));
                break;
        }
    }
}
