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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Home.Home;
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
    User user;
    String name ="";
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
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("si", "onAuthStateChanged:signed_in:" + user.getUid());
                     uploadUser();
                    proceedHome();
                } else {
                    Log.d("si", "onAuthStateChanged:signed_out");
                }

            }
        };
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
 /*       OptionalPendingResult<GoogleSignInResult> opr = AuthUtil.getOptionalResult(mGoogleApiClient);
        if (AuthUtil.isUserSignedIn(opr)) {
            Log.d("si", "Got cached sign-in");
            updateUI("Already signed is as " + AuthUtil.getUserAccount(opr).getDisplayName());
            AuthUtil.proceddSignIn(this,AuthUtil.getUserAccount(opr));
            proceedHome();

        } else {
            progressDialog.showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    Log.d("si", "onstart handleSignInResult:" + googleSignInResult.isSuccess());
                    progressDialog.disMissProgressDialog();
                    if (googleSignInResult.isSuccess())
                        handleGoogleSignIn(googleSignInResult);
                }
            });
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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
        progressDialog.showProgressDialog(R.string.signing_in,false);
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
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignIn(result);
        }
    }

    private void handleGoogleSignIn(GoogleSignInResult result) {
        Log.d("si", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            AuthUtil.proceddSignIn(this,account);
            user = User.saveUser(account);
            name = account.getGivenName();

            Log.d("si", " " + account.getId() + " " + account.getEmail());
            firebaseAuthWithGoogle(account);
        } else {
            handleSignInFailure();
        }
    }

    private void uploadUser() {
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mProceedChild = mRootRef.child("user_proceed");
        final DatabaseReference mUserChild = mRootRef.child("users");

        Log.d("si","proceed "+mProceedChild.getKey()+" "+mProceedChild.getParent());
        mProceedChild.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int proceedVaulue = dataSnapshot.getValue(Integer.class);
                if (proceedVaulue == 1){
                    Log.d("si","uploading");
                    if (user != null) {
                        DatabaseReference myRef = mUserChild.child(user.id);
                        myRef.setValue(user);
                    }else{
                        User user = User.saveUser(SignIn.this);
                        DatabaseReference myRef = mUserChild.child(user.id);
                        myRef.setValue(user);
                    }
                }else{
                    Log.d("si","permission denied");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    private void handleSignInSuccess(String name) {

        String welcome = getResources().getString(R.string.welcome, name);
        updateUI(welcome);
    }

    private void handleSignInFailure() {
        progressDialog.disMissProgressDialog();
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.signin_parent);
        String failed = Import.getString(this, R.string.sign_in_failure);
        Snackbar.make(coordinatorLayout, failed, Snackbar.LENGTH_LONG).show();

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("si", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("si", "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("si", "signInWithCredential" + task.getException());
                            progressDialog.disMissProgressDialog();
                            updateUI(getResources().getString(R.string.sign_in_failure));
                        }
                    }
                });
    }

    private void proceedHome() {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD,name );
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);

        progressDialog.disMissProgressDialog();
        Import.setSharedPref(this,SharedKeys.user_signed_status,1);

        String welcome = getResources().getString(R.string.welcome, name);

        Bundle welcome_bundle = new Bundle();
        welcome_bundle.putString("snackMessage",welcome);
        Intent welcome_intent = new Intent(SignIn.this, Home.class);
        welcome_intent.putExtras(welcome_bundle);


        startActivity(welcome_intent);
        finish();

    }


    private void updateUI(String message) {
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.signin_parent);
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("signin", "failed to connect");

        progressDialog.disMissProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Log.sign_in_connect_error_id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Log.sign_in_connect_error);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }
}
