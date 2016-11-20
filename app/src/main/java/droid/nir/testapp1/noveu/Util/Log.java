package droid.nir.testapp1.noveu.Util;

/**
 * Created by droidcafe on 7/9/2016.
 */
public class Log {
    static final boolean LOG = true;

    public static void i(String tag, String string) {
        if (LOG) android.util.Log.i(tag, string);
    }

    public static void e(String tag, String string) {
        if (LOG) android.util.Log.e(tag, string);
    }

    public static void d(String tag, String string) {
        if (LOG) android.util.Log.d(tag, string);
    }

    public static void v(String tag, String string) {
        if (LOG) android.util.Log.v(tag, string);
    }

    public static void w(String tag, String string) {
        if (LOG) android.util.Log.w(tag, string);
    }


    public static final String sign_in_connect_error = "SIGN_IN_CONNECTION_FAILED";
    public static final String sign_in_connect_error_id = "345";

    public static final String sign_in_success = "SIGN_IN_SUCCESS";
}