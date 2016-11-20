package droid.nir.animation;

import android.animation.ObjectAnimator;
import droid.nir.testapp1.noveu.Util.Log;


import droid.nir.testapp1.Pending_Recycler_Data;

/**
 * Created by user on 9/6/2015.
 */
public class Recycleranims {


    public void animate(Pending_Recycler_Data.ViewHold holder, boolean isup) {

        Log.d("recycleranims","calling animation "+true);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(holder.pending_cards, "translationY",isup==true?100: -100, 0);
        objectAnimator.setDuration(1000);
        objectAnimator.start();

    }
}
