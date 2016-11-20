package droid.nir.testapp1.noveu.Tasks.show;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Tasks.data.SharedData;
import droid.nir.testapp1.noveu.Util.Import;

public class Show_Expand extends AppCompatActivity {

    static Import anImport;
    static int repeatselected, alarmselected;
    static String dateselected;
    static int mode = 0, remmode = 0, projectid, timehr, timemin, inital_remmode;
    static Context context;
    static Activity activity;
    static String extras;
    static boolean isMoreVisible, isShared;
    static int choice, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__expand);

        SharedData.clearAll();
        anImport = new Import(this);
        context = this;
        activity = this;

        setupinitial();

      //  getArguments();
        //setupviews();


    }

    private void setupinitial() {

        mode = 0;
        remmode = 0;
        inital_remmode = 0;
        extras = "";
        choice = 0;
        id = -1;
        isMoreVisible = false;
        isShared = false;
    }


}
