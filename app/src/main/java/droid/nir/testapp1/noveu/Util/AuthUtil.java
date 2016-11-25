package droid.nir.testapp1.noveu.Util;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

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
        return (opr != null) ? opr.get() : null;
    }

    public static OptionalPendingResult<GoogleSignInResult> getOptionalResult(GoogleApiClient mGoogleApiClient) {
        OptionalPendingResult<GoogleSignInResult> opr =
                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        return (isUserSignedIn(opr)) ? opr : null;
    }

    public static boolean isUserSignedIn(OptionalPendingResult<GoogleSignInResult> opr) {
        return opr.isDone();
    }


}
