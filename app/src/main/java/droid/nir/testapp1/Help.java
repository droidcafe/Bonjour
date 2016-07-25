package droid.nir.testapp1;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import droid.nir.defcon3.help_main;

public class Help extends ActionBarActivity implements View.OnClickListener {

    int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        TextView illustration = null;
        Intent intent = getIntent();
        if(intent!=null)
        {
            id = intent.getExtras().getInt("helpid");
        }


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Help");
        setContentView(R.layout.activity_help);

        switch (id)
        {
            case 0:
                CardView cardView = (CardView) findViewById(R.id.pending_cards);
                cardView.setVisibility(View.VISIBLE);
                illustration = (TextView) findViewById(R.id.illus1);
                break;
            case 1:
                CardView cardView1 = (CardView) findViewById(R.id.pending_cards2);
                cardView1.setVisibility(View.VISIBLE);
                illustration = (TextView) findViewById(R.id.illus2);

                break;
            case 2:
                CardView cardView2 = (CardView) findViewById(R.id.pending_cards3);
                cardView2.setVisibility(View.VISIBLE);
                illustration = (TextView) findViewById(R.id.illus3);
                break;
            case 3:
                CardView cardView3 = (CardView) findViewById(R.id.pending_cards4);
                cardView3.setVisibility(View.VISIBLE);
                illustration = (TextView) findViewById(R.id.illus4);
                break;
        }

        illustration.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
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
        Intent newintent = new Intent(Help.this,help_main.class);
        newintent.putExtra("round",id);
        newintent.putExtra("from",1);

        startActivity(newintent);

    }
}
