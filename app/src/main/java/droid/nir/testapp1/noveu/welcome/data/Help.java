package droid.nir.testapp1.noveu.welcome.data;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by droidcafe on 12/9/2016.
 */

@IgnoreExtraProperties
public class Help {

    String title,desp;

    public Help() {
        // required for firebase calls
    }

    public Help(String title,String desp) {
        this.title = title;
        this.desp = desp;
    }
}
