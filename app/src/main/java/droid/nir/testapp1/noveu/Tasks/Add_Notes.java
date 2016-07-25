package droid.nir.testapp1.noveu.Tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import droid.nir.testapp1.noveu.Util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Tasks.data.SharedData;
import droid.nir.testapp1.noveu.constants.constants;

public class Add_Notes extends AppCompatActivity {

    @Bind(R.id.notes)
    EditText note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__notes);

        ButterKnife.bind(this);
        getArguments();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               returnParent();
            }
        });
    }

    private void getArguments() {

        int mode = getIntent().getExtras().getInt("mode");
        if(mode==5||mode==6||mode==8||mode==9)
        {
            setNote();
        }
        else {
            SharedData.notes="";
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        saveNote();
        Log.d("note", "notes save " + SharedData.notes);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setNote();
        Log.d("note", "Restore " + SharedData.notes);
    }

    private void returnParent() {

        saveNote();
        Intent return_intent = new Intent();
        if(!SharedData.notes.equals(""))
        {
            Log.d("an","non empty");
            return_intent.putExtra("mode",5);
        }
        else
            return_intent.putExtra("mode",-1);

        setResult(constants.SUCCESS_CODE, return_intent);

        finish();
    }

    private void saveNote()
    {
        String notes = note.getText().toString();
        Log.d("note", "notes save " + notes);
        SharedData.notes  = notes;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNote();
    }

    private void setNote()
    {
        note.setText(SharedData.notes);
    }
}
