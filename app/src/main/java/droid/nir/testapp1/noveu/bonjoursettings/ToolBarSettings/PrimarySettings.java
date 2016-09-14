package droid.nir.testapp1.noveu.bonjoursettings.ToolBarSettings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import droid.nir.defcon3.FirstScreen;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.welcome.help.Help;

/**
 * Created by droidcafe on 9/15/2016.
 */
public class PrimarySettings {

    public static void primarySetting(Context context,Activity activity,int id){
        switch(id){

            case R.id.action_help:

                activity.startActivity(new Intent(context, Help.class));

                break;
            case R.id.action_share:

                String sharetext = context.getResources().getString(R.string.sharetext);
                Intent shar = new Intent();
                shar.setAction(Intent.ACTION_SEND);
                shar.setType("text/plain");
                shar.putExtra(Intent.EXTRA_TEXT, sharetext);
                activity.startActivity(Intent.createChooser(shar, context.getResources().getString(R.string.shareusing)));
                break;
            case R.id.action_feedback:
                String[] mailid = {constants.dev_mail};
                Import.composeEmail(activity, mailid, "FeedBack", null);
                break;
            case R.id.action_rate:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.app_uri)));
                activity.startActivity(intent);
                break;
            case R.id.action_about:

                activity.startActivity(new Intent(context, FirstScreen.class));
                break;

        }
    }
}
