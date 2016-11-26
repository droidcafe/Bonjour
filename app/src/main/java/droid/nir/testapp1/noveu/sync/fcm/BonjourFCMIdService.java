package droid.nir.testapp1.noveu.sync.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import droid.nir.testapp1.noveu.Util.Log;

public class BonjourFCMIdService extends FirebaseInstanceIdService {
    public BonjourFCMIdService() {
    }

    String TAG = "bfis";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Refreshed token: " + refreshedToken);
//        sendRegistrationToServer(refreshedToken);

    }
}
