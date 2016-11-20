package droid.nir.testapp1.noveu.Util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by droidcafe on 11/12/2016.
 */

public class InputManager {

    public static String getStrings(EditText editText)
    {
        return editText.getText().toString();

    }

    public static int getid(Context context, String tempid)
    {
        return context.getResources().getIdentifier(tempid, "id", context.getApplicationContext().getPackageName());
    }

    public static  void showKeyBoard(Context context,View textView)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT);
    }

    public static String gettextStrings(TextView textView)
    {
        return textView.getText().toString();
    }
}
