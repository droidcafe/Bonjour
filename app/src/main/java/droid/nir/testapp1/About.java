package droid.nir.testapp1;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class About extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_about);

        setContentView(R.layout.activity_first_screen);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"Ribbon_V2_2011.otf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(),"Airplane.ttf");
        TextView textView= (TextView) findViewById(R.id.appname);
        TextView textView2= (TextView) findViewById(R.id.moto);


        textView.setTypeface(typeface);
        textView2.setTypeface(typeface2);


        TextView hpending = (TextView) findViewById(R.id.hpending);
        TextView hevents = (TextView) findViewById(R.id.hevents);
        TextView hlists = (TextView) findViewById(R.id.hlists);
        TextView hreminders= (TextView) findViewById(R.id.hreminders);


        hpending.setOnClickListener(this);
        hevents.setOnClickListener(this);
        hlists.setOnClickListener(this);
        hreminders.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        Intent newintent =new Intent(About.this, Help.class);
        switch (v.getId())
        {
            case R.id.hpending:

                newintent.putExtra("helpid",0);
                break;
            case R.id.hevents:
                newintent.putExtra("helpid",1);
                break;
            case R.id.hlists:
                newintent.putExtra("helpid",2);
                break;
            case R.id.hreminders:
                newintent.putExtra("helpid",3);
                break;
        }

        startActivity(newintent);
    }
}
