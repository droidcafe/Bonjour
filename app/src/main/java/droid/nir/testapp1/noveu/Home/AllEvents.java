package droid.nir.testapp1.noveu.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Calendar;
import java.util.List;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Events.Add_Event;
import droid.nir.testapp1.noveu.Events.loaders.loadEvent;
import droid.nir.testapp1.noveu.Home.data.dataEvent;
import droid.nir.testapp1.noveu.NavDrw.setNav;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.TimeUtil;

public class AllEvents extends AppCompatActivity implements View.OnClickListener {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.events);


        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupNav();
        findViewById(R.id.fab).setOnClickListener(this);

        context = this;
        setupEventList();

    }

    private void setupEventList() {
        new AsyncLoad(this).execute();
    }

    private void setupNav() {
        new setNav(this,this).setupNavigation(R.id.nav_event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Intent goinginent2 = new Intent(this, Add_Event.class);
                Calendar calendar = Calendar.getInstance();
                goinginent2.putExtra("customdate", TimeUtil.getDate(calendar));
                goinginent2.putExtra("e1", calendar.get(Calendar.DAY_OF_MONTH));
                goinginent2.putExtra("e2", calendar.get(Calendar.MONTH));
                goinginent2.putExtra("e3", calendar.get(Calendar.YEAR));
                startActivity(goinginent2);
                break;
        }
    }

    public static class AsyncLoad extends AsyncTask<Void, Void, List<dataEvent>> {

        Context context;
        Activity activity;
        AsyncLoad(Context context,Activity activity) {
            this.context = context;
            this.activity = activity;
        }
        @Override
        protected List<dataEvent> doInBackground(Void... voids) {

            return new loadEvent().laodAllEvents(context);
        }

        @Override
        protected void onPostExecute(List<dataEvent> dataEvents) {
            super.onPostExecute(dataEvents);

            if (dataEvents.isEmpty()) {
                Import.setBackGroundColor(context, activity, R.id.home_back, R.color.tsecondary);
                recyclerView.setVisibility(View.GONE);
                Import.allDone(context, alldone_pic, alldone_title, alldone_promo);
            }
        }
    }
}
