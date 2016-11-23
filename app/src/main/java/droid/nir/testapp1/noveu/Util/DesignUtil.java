package droid.nir.testapp1.noveu.Util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by droidcafe on 11/24/2016.
 */

public class DesignUtil {

    /**
     * Converting dp to pixel
     */
    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * convret the dimension values mention in {#values.dimen} to
     * dp values depending upon the phone screen
     * for to be used in screen. Used in cards while displaying
     * the time to give it correct padding
     * @param context
     * @param dimen
     * @return
     */
    public static int getdpMeasurement(Context context,int dimen){
        return (int)context.getResources().getDimension(dimen);
    }

}

