package droid.nir.defcon3;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import droid.nir.testapp1.R;

public class FirstScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"Ribbon_V2_2011.otf");

        TextView textView= (TextView) findViewById(R.id.appname);
        TextView textView2= (TextView) findViewById(R.id.moto);


        textView.setTypeface(typeface);

        loadViewPager();

    }

    private void loadViewPager() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent newintent = new Intent(FirstScreen.this,help_main.class);
                newintent.putExtra("round", 0);
                newintent.putExtra("from",0);
                startActivity(newintent);
                FirstScreen.this.finish();

            }
        },2000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_screen, menu);
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
}
