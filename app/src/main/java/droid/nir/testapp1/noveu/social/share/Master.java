package droid.nir.testapp1.noveu.social.share;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import droid.nir.testapp1.Bonjour;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.Import;

/**
 * Created by droidcafe on 7/10/2016.
 */
public class Master {

    public static Intent share(String title , String sharetext) {

        Context context = Bonjour.getContext();
        sharetext = sharetext.concat(Import.getString(context, R.string.promotion_share) + "\n" );
        sharetext = sharetext.concat(Import.getString(context, R.string.promotion_download) +
                Import.getString(context , R.string.app_url) +"\n" );

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, sharetext);
        if (Build.VERSION.SDK_INT >= 21) {
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }
        share.putExtra(Intent.EXTRA_SUBJECT,"" +title);

        return share;
    }
}
