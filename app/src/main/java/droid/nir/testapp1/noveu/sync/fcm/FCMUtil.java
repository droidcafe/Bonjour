package droid.nir.testapp1.noveu.sync.fcm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import droid.nir.testapp1.noveu.Home.Home;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.constants.constants;

/**
 * Created by droidcafe on 11/26/2016.
 */

public class FCMUtil {

    public static void handleFCMIntent(Context context, Activity activity,Bundle bundle) {
        String type = (String) bundle.get("type");
        Log.d("fu", "type " + type);
        if (type.equals(constants.fcm_type_modes[1])) { /** update */
            String update_version = (String) bundle.get("version");
            String version_present = Import.getSharedPref(SharedKeys.fcm_update_availabe,context);

            if(update_version.equals(version_present))
                return;

            Import.setSharedPref(context,SharedKeys.fcm_update_availabe,update_version);
            activity.setIntent(new Intent(context,Home.class));
            Intent playIntent = Import.getPlayStoreIntent(context);
            activity.startActivity(playIntent);
            activity.finish();
        }
    }
}
