package droid.nir.testapp1.noveu.sync.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;

import droid.nir.testapp1.noveu.Tasks.Add_Expand;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.IntentActions;
import droid.nir.testapp1.noveu.notifications.handlers.NotificationHandler;


public class NotificationResponseService extends IntentService {

    public NotificationResponseService() {
        super("NotificationResponseService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            switch (intent.getAction()) {

                case IntentActions.ACTION_NOTIFICATION_DISMISSPERMANENT:
                    processCancel(intent);
                    break;
                case IntentActions.ACTION_NOTIFICATION_DISMISSALARM:
                    processCancel(intent);
                    break;

            }

        }

    }

    private void processProceedTask(Intent intent) {
        Log.d("nrs","pro "+intent.getAction());
        Bundle bundle = intent.getExtras();
        openExpand(bundle);
        return;
    }

    private void processCancel(Intent intent)
    {
        NotificationHandler.cancel(getApplicationContext(), intent.getIntExtra("nid", -1));

    }

    private void openExpand(Bundle bundle) {
        Intent intent_expand = new Intent(this, Add_Expand.class);
        Bundle bundle_expand = new Bundle();
        bundle_expand.putInt("taskid", bundle.getInt("taskid"));
        bundle_expand.putInt("choice", 1);

        intent_expand.putExtras(bundle_expand);
        startActivity(intent_expand);

    }

}
