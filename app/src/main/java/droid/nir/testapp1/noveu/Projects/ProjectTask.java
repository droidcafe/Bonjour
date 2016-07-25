package droid.nir.testapp1.noveu.Projects;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import droid.nir.testapp1.noveu.Util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;


import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Home.data.dataHome;
import droid.nir.testapp1.noveu.Projects.Adapter.ProjectTaskAdapter;
import droid.nir.testapp1.noveu.Tasks.Add_minimal;
import droid.nir.testapp1.noveu.Tasks.Loaders.LoadTask;
import droid.nir.testapp1.noveu.Util.AutoRefresh;
import droid.nir.testapp1.noveu.constants.constants;

public class ProjectTask extends AppCompatActivity implements View.OnClickListener {

    static Activity activity;
    static Context context;
    static String proname;
    static  int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity =this;
        context =this;

        findViewById(R.id.fab).setOnClickListener(this);
       id =  getArguments();
        setupRecycler(id);


    }

    @Override
    protected void onResume() {
        super.onResume();

       runDelayedRefresh();
    }

    private void runDelayedRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AutoRefresh.isRefreshNeeded(ProjectTask.this)) {
                    refresh();
                }
            }
        }, 1500);
    }

    private int getArguments() {

        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        proname = bundle.getString("proname");

        getSupportActionBar().setTitle(proname);

        return id;
    }

    private void setupRecycler(int id) {

        new AsyncLoad().execute(id);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Intent intent = new Intent(this,Add_minimal.class);
                Bundle bundle = new Bundle();
                bundle.putInt("proid",id);

                intent.putExtras(bundle);
                startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.refresh:
                refresh();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        setupRecycler(id);
    }

    private static class AsyncLoad extends AsyncTask<Integer,Integer,List<dataHome>>{

        @Override
        protected List<dataHome> doInBackground(Integer... params) {
            LoadTask loadTask = new LoadTask(activity,context);
            String preselection = "pid = "+params[0];
            //String preselectionArgs[] = {params[0]};
            loadTask.loadPreSelection(preselection, null);

            List<dataHome> taskList = loadTask.loadAllTasks();

            Log.d(constants.projectTaskTAG,"no of this "+taskList.size());
            return taskList;
        }

        @Override
        protected void onPostExecute(List<dataHome> dataHomes) {
            super.onPostExecute(dataHomes);

            if(dataHomes.size()==0)
            {
                TextView hiddentext = (TextView) activity.findViewById(R.id.hiddentext);
                hiddentext.setVisibility(View.VISIBLE);
                hiddentext.setText(context.getString(R.string.noprotask,new Object[]{proname}));

                RecyclerView tasklist = (RecyclerView) activity.findViewById(R.id.tasklist);
                tasklist.setVisibility(View.GONE);
            }
            else {
                RecyclerView tasklist = (RecyclerView) activity.findViewById(R.id.tasklist);
                tasklist.setVisibility(View.VISIBLE);
                activity.findViewById(R.id.hiddentext).setVisibility(View.GONE);
                tasklist.setLayoutManager(new LinearLayoutManager(context));
                ProjectTaskAdapter taskAdapter = new ProjectTaskAdapter(context, dataHomes);
                tasklist.setAdapter(taskAdapter);
            }
        }
    }

}
