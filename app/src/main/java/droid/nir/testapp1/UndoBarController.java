/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package droid.nir.testapp1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

public class UndoBarController {
    private View mBarView;
    private TextView mMessageView;
    private ViewPropertyAnimator mBarAnimator;
    private Handler mHideHandler = new Handler();

    private UndoListener mUndoListener;

    // State objects
    private Parcelable mUndoToken;
    private CharSequence mUndoMessage;

    public interface UndoListener {
        void onUndo(Parcelable token);
    }

    public UndoBarController(View undoBarView, UndoListener undoListener) {
        mBarView = undoBarView;
        mBarAnimator = mBarView.animate();
        mUndoListener = undoListener;

       /* mMessageView = (TextView) mBarView.findViewById(R.id.undobar_message);
        mBarView.findViewById(R.id.undobar_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideUndoBar(false);
                        mUndoListener.onUndo(mUndoToken);
                    }
                });
*/
        hideUndoBar(true);
    }

    public void showUndoBar(boolean immediate, CharSequence message, Parcelable undoToken) {
        mUndoToken = undoToken;
        mUndoMessage = message;
        mMessageView.setText(mUndoMessage);

        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable,
                mBarView.getResources().getInteger(R.integer.undobar_hide_delay));

        mBarView.setVisibility(View.VISIBLE);
        if (immediate) {
            mBarView.setAlpha(1);
        } else {
            mBarAnimator.cancel();
            mBarAnimator
                    .alpha(1)
                    .setDuration(
                            mBarView.getResources()
                                    .getInteger(android.R.integer.config_shortAnimTime))
                    .setListener(null);
        }
    }

    public void hideUndoBar(boolean immediate) {
        mHideHandler.removeCallbacks(mHideRunnable);
        if (immediate) {
            mBarView.setVisibility(View.VISIBLE);
            mBarView.setAlpha(0);
            mUndoMessage = null;
            mUndoToken = null;

        } else {
            mBarAnimator.cancel();
            mBarAnimator
                    .alpha(0)
                    .setDuration(mBarView.getResources()
                            .getInteger(android.R.integer.config_shortAnimTime))
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mBarView.setVisibility(View.VISIBLE);
                            mUndoMessage = null;
                            mUndoToken = null;
                        }
                    });
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence("undo_message", mUndoMessage);
        outState.putParcelable("undo_token", mUndoToken);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mUndoMessage = savedInstanceState.getCharSequence("undo_message");
            mUndoToken = savedInstanceState.getParcelable("undo_token");

            if (mUndoToken != null || !TextUtils.isEmpty(mUndoMessage)) {
                showUndoBar(true, mUndoMessage, mUndoToken);
            }
        }
    }

    private Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hideUndoBar(false);
        }
    };
}
