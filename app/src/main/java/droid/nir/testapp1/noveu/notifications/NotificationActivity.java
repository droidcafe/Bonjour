package droid.nir.testapp1.noveu.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Tasks.Add_Expand;
import droid.nir.testapp1.noveu.constants.IntentActions;
import droid.nir.testapp1.noveu.notifications.handlers.NotificationHandler;
import droid.nir.testapp1.noveu.sync.services.PlayBackService;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            Log.d("na"," "+intent.getAction());
            switch (intent.getAction()) {
                case IntentActions.ACTION_NOTIFICATION_PROCEED_TASK:
                    processProceedTask(intent);
                    break;
                case IntentActions.ACTION_NOTIFICATION_PROCEED_ALARM_TASK:
                    processProceedTask(intent);
                    processCancel(intent);
                    break;
                case IntentActions.ACTION_SHARE_TASK:
                    processProceedTask(intent);
            }

            finish();
        }

    }

    private void processProceedTask(Intent intent) {
        Bundle bundle = intent.getExtras();
        openExpand(bundle , intent.getStringExtra("extra"));
        return;
    }

    private void processCancel(Intent intent)
    {
        NotificationHandler.cancel(getApplicationContext(),intent.getIntExtra("nid",-1));

    }

    private void openExpand(Bundle bundle , String extra) {
        Intent intent_expand = new Intent(this, Add_Expand.class);
        intent_expand.putExtra("extra",extra);
        Bundle bundle_expand = new Bundle();
        bundle_expand.putInt("taskid", bundle.getInt("taskid"));
        bundle_expand.putInt("choice", 1);

        intent_expand.putExtras(bundle_expand);
        startActivity(intent_expand);

    }

}
