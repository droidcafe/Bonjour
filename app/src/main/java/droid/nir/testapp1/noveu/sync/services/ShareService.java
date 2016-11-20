package droid.nir.testapp1.noveu.sync.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import droid.nir.testapp1.Bonjour;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Tasks.Loaders.LoadTask;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.IntentActions;
import droid.nir.testapp1.noveu.social.share.TaskShare;


public class ShareService extends IntentService {

    public ShareService() {
        super("ShareService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if(action.equals(IntentActions.ACTION_SHARE_TASK)) {
                int tid = intent.getIntExtra("tid" , -1);
                Context context = getApplicationContext();
                if (tid >= 0) {
                    TaskShare taskShare = new TaskShare();
                    if(taskShare.loadTask(context , tid) ) {
                        Intent share = taskShare.share(context);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(Intent.createChooser(share,
                                context.getResources().getString(R.string.shareusing)));
                    }
                }
            }
        }
    }

}
