package droid.nir.testapp1;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by user on 7/3/2015.
 */
public class inputManager {

    Context context;
    inputManager(Context context)
    {
        this.context = context;
    }

    public  void key(TextView textView)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT);
    }

    public String getStrings(EditText editText)
    {
        return editText.getText().toString();

    }

    public int getid(String tempid)
    {
        return context.getResources().getIdentifier(tempid, "id", context.getApplicationContext().getPackageName());
    }

    public String gettextStrings(TextView textView)
    {
        return textView.getText().toString();
    }
}
