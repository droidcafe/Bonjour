package droid.nir.testapp1.noveu.ui.util;

import android.content.Context;

import droid.nir.testapp1.R;

/**
 * Created by droidcafe on 11/25/2016.
 */

public class ProgressDialog {

    private android.app.ProgressDialog mProgressDialog;
    Context context;

    public ProgressDialog(Context context) {
        this.context = context;
    }

    public void showProgressDialog() {
        showProgressDialog(R.string.loading);
    }
    public void showProgressDialog(int message_id) {
        if (mProgressDialog == null) {
            mProgressDialog = new android.app.ProgressDialog(context);
            mProgressDialog.setMessage(context.getString(message_id));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    public void disMissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


}
