package droid.nir.testapp1.noveu.Tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Tasks.Adapter.SublistAdapter;
import droid.nir.testapp1.noveu.Tasks.data.SharedData;
import droid.nir.testapp1.noveu.constants.constants;

public class Add_SubTasks extends AppCompatActivity implements View.OnClickListener {

    SublistAdapter sublistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__sub_tasks);


        getArguments();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        findViewById(R.id.add_sub).setOnClickListener(this);

        setupList();

    }

    private void getArguments() {

        int mode = getIntent().getExtras().getInt("mode");
        if(!(mode==3||mode==4||mode==8||mode==9))
        {
           SharedData.list = new ArrayList<>();
            SharedData.subTaskdone = new ArrayList<>();
        }
    }

    private int searchDone(int i)
    {
       return SharedData.subTaskdone.indexOf(i);
    }
    private void setupList() {

        RecyclerView taskList = (RecyclerView) findViewById(R.id.todolist);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        sublistAdapter = new SublistAdapter(searchDone(-1));
        taskList.setAdapter(sublistAdapter);

        taskList.setHasFixedSize(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_sub:
                addSubList();
                break;
            case R.id.fab:
               returnParent();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        addSubList();
    }

    private void returnParent() {
        addSubList();
        Intent return_intent = new Intent();
        return_intent.putExtra("mode",SharedData.list.size());
        return_intent.putExtra("size",searchDone(-1));

        setResult(constants.SUCCESS_CODE,return_intent);
        finish();
    }

    private void addSubList() {

        TextView subtaskview =(TextView) findViewById(R.id.new_sub_task);
        String subtask = subtaskview.getText().toString();
        if (!subtask.equals("")) {
           sublistAdapter.addItem(subtask,0);
            subtaskview.setText("");
        }
    }
}
