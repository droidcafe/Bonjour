package droid.nir.testapp1.noveu.welcome;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.Import;

public class Version extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        Import.settypefaces(this,"Ribbon_V2_2011.otf", (TextView) findViewById(R.id.about_title));
        Import.settypefaces(this,"Raleway-Medium.ttf", (TextView) findViewById(R.id.about_desp));
        Import.settypefaces(this,"SourceSansPro-Regular.otf", (TextView) findViewById(R.id.version));

    }

}
