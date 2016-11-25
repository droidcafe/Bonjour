package droid.nir.testapp1.noveu.welcome.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.AuthUtil;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.ui.util.ProgressDialog;

public class SignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final int RC_GOOGLE_SIGN_IN = 301;
    GoogleApiClient mGoogleApiClient;
    private FirebaseAnalytics mFirebaseAnalytics;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.signin_title));
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        progressDialog = new ProgressDialog(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = AuthUtil.getOptionalResult(mGoogleApiClient);
        if (opr != null) {
            Log.d("si", "Got cached sign-in");
            updateUI("Already signed is as "+ AuthUtil.getUserAccount(opr).getDisplayName());
        }else{
            progressDialog.showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    progressDialog.hideProgressDialog();
                    handleGoogleSignIn(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("signin", "failed to connect");

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Log.sign_in_connect_error_id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Log.sign_in_connect_error);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                googleSignIn();
                break;
            case R.id.sign_out_button:
                googleSignOut();
                break;

        }
    }

    private void googleSignIn() {
        Log.d("si", "lauching signinintent");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    private void googleSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI("Signed out " + Import.getSharedPref(SharedKeys.user_name, SignIn.this));
                        Import.setSharedPref(SignIn.this, SharedKeys.user_name, "");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("si", "result");
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("si", "result for signin " + resultCode + " " + result);
            handleGoogleSignIn(result);
        }
    }

    private void handleGoogleSignIn(GoogleSignInResult result) {
        Log.d("si", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();
            Log.d("si", " " + account.getId() + " " + account.getEmail());
            Log.d("si", " token id " + account.getIdToken());
            handleSignInSuccess(account.getDisplayName());
        } else {
            // Signed out, show unauthenticated UI.
            handleSignInFailure();
        }
    }

    private void handleSignInSuccess(String name) {

        String welcome = getResources().getString(R.string.welcome, name);
        updateUI(welcome);
        Import.setSharedPref(this, SharedKeys.user_name, name);
    }

    private void handleSignInFailure() {
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.signin_parent);
        String failed = Import.getString(this, R.string.sign_in_failure);
        Snackbar.make(coordinatorLayout, failed, Snackbar.LENGTH_LONG).show();

    }

    private void updateUI(String message) {
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.signin_parent);
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }
}
