package droid.nir.testapp1.noveu.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.welcome.auth.SignIn;

/**
 * Created by droidcafe on 11/25/2016.
 */

public class AuthUtil {



    public static GoogleSignInAccount silentSignIn(GoogleApiClient mGoogleApiClient){
        OptionalPendingResult<GoogleSignInResult> opr = getOptionalResult(mGoogleApiClient);
        GoogleSignInResult gsr = getSignInResult(opr);
        return getUserAccount(gsr);
    }

    public static GoogleSignInAccount getUserAccount(
            OptionalPendingResult<GoogleSignInResult> opr) {
        return getUserAccount(getSignInResult(opr));
    }
    public static GoogleSignInAccount getUserAccount(GoogleSignInResult result){
        return (result != null) ? result.getSignInAccount() : null;
    }

    public static GoogleSignInResult getSignInResult(OptionalPendingResult<GoogleSignInResult> opr) {
        return (isUserSignedIn(opr)) ? opr.get() : null;
    }

    public static OptionalPendingResult<GoogleSignInResult> getOptionalResult(GoogleApiClient mGoogleApiClient) {
        OptionalPendingResult<GoogleSignInResult> opr =
                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        return opr;
    }

    public static boolean isUserSignedIn(OptionalPendingResult<GoogleSignInResult> opr) {
        return opr.isDone();
    }

    public static void signOut(GoogleApiClient mGoogleApiClient, final Context context, Activity activity) {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        proceedSignOut(context);
                    }
                });
        FirebaseAuth.getInstance().signOut();
        activity.startActivity(new Intent(context,SignIn.class));
        activity.finish();
    }

    public static GoogleApiClient getGoogleClient(Context context,
                                                  FragmentActivity activity,
                                                  GoogleApiClient.OnConnectionFailedListener listener) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        return new GoogleApiClient.Builder(context)
                .enableAutoManage(activity,listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public static void proceddSignIn(Context context,GoogleSignInAccount account) {
        Import.setSharedPref(context, SharedKeys.user_name, account.getGivenName());
        Import.setSharedPref(context, SharedKeys.user_display_name, account.getDisplayName());
        Import.setSharedPref(context, SharedKeys.user_email, account.getEmail());
        Import.setSharedPref(context, SharedKeys.user_google_id, account.getId());

    }

    public static void proceedSignOut(Context context) {
        Import.setSharedPref(context, SharedKeys.user_name, "");
        Import.setSharedPref(context, SharedKeys.user_display_name, "");
        Import.setSharedPref(context, SharedKeys.user_signed_status, -1);
        Import.setSharedPref(context, SharedKeys.user_email, "");
    }


}
