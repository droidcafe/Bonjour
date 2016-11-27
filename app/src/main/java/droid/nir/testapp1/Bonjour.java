package droid.nir.testapp1;

import android.app.Application;
import android.content.Context;

/**
 * Created by droidcafe on 2/27/2016.
 */
public class Bonjour extends Application {

    private static Context sContext = null;
    @Override
    public void onCreate() {
        super.onCreate();

    //    Stetho.initializeWithDefaults(this);
        sContext = getApplicationContext();
    }

    public static Context getContext()
    {
        return sContext;
    }
}

