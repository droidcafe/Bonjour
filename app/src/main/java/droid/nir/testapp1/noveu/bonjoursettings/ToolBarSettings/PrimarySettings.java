package droid.nir.testapp1.noveu.bonjoursettings.ToolBarSettings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.FirebaseUtil;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.welcome.Version;
import droid.nir.testapp1.noveu.welcome.help.ui.Help;

/**
 * Created by droidcafe on 9/15/2016.
 */
public class PrimarySettings {

    public static void primarySetting(Context context, Activity activity, int id,
                                      FirebaseAnalytics mFirebaseAnalytics){
        switch(id){

            case R.id.action_help:

                activity.startActivity(new Intent(context, Help.class));
                Bundle fireBundle = new Bundle();
                fireBundle.putString("name",activity.getLocalClassName());
                mFirebaseAnalytics.logEvent(FirebaseUtil.menu_help,fireBundle);
                break;
            case R.id.action_share:

                String sharetext = context.getResources().getString(R.string.sharetext);
                Intent shar = new Intent();
                shar.setAction(Intent.ACTION_SEND);
                shar.setType("text/plain");
                shar.putExtra(Intent.EXTRA_TEXT, sharetext);

                Bundle fire_share = new Bundle();
                fire_share.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"bonjour");
                fire_share.putString(FirebaseAnalytics.Param.ITEM_ID, activity.getLocalClassName());
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,fire_share);

                activity.startActivity(Intent.createChooser(shar, context.getResources().getString(R.string.shareusing)));
                break;
            case R.id.action_feedback:
                String[] mailid = {constants.dev_mail};
                Bundle fireFeedBack = new Bundle();
                fireFeedBack.putString("name",activity.getLocalClassName());
                mFirebaseAnalytics.logEvent(FirebaseUtil.menu_feedback,fireFeedBack);
                Import.composeEmail(activity, mailid, "FeedBack", null);
                break;
            case R.id.action_rate:
                Bundle fireRate = new Bundle();
                fireRate.putString("name",activity.getLocalClassName());
                mFirebaseAnalytics.logEvent(FirebaseUtil.menu_rate,fireRate);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.app_uri)));
                activity.startActivity(intent);
                break;
            case R.id.action_about:

                activity.startActivity(new Intent(context, Version.class));
                break;

        }
    }
}
