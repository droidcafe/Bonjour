package droid.nir.testapp1.noveu.welcome.data;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by droidcafe on 12/9/2016.
 */

@IgnoreExtraProperties
public class Help {

    /**
     * type = type of help
     * 0 - normal title and desp
     * 1 - only title
     * 2 - only desp
     */
    public String title,desp;


    public Help() {
        // required for firebase calls
    }

    public Help(String title,String desp) {
        this.title = title;
        this.desp = desp;
    }
}
