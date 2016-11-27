package droid.nir.testapp1.noveu.welcome.auth;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.SharedKeys;

/**
 * Created by droidcafe on 11/27/2016.
 */

public class User {

    public String name,email,displayName,photoUrl,id,status;
    int age;

    public static User saveUser(GoogleSignInAccount account){
        User user = new User();
        user.name = account.getGivenName();
        user.displayName = account.getDisplayName();
        user.email = account.getEmail();
        user.id = account.getId();
        user.photoUrl = String.valueOf(account.getPhotoUrl());
        user.status = "signed_in";
        Log.d("user","name "+user.name+" id "+user.id);
        return user;
    }

    public static User saveUser(Context context) {
        User user = new User();

        user.name = Import.getSharedPref( SharedKeys.user_name, context);
        user.displayName = Import.getSharedPref( SharedKeys.user_display_name, context);
        user.email = Import.getSharedPref( SharedKeys.user_email, context);
        user.id = Import.getSharedPref( SharedKeys.user_google_id, context);
        user.photoUrl = Import.getSharedPref( SharedKeys.user_google_photo_url, context);
        user.status = "signed_in";
        return user;
    }
}
