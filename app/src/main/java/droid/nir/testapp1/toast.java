package droid.nir.testapp1;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by user on 7/2/2015.
 */
public class toast  {


    Context context;
    public toast(Context context)
    {
        this.context = context;

    }
    public  void makeText(String string)
    {
        Toast.makeText(context, " "+ string , Toast.LENGTH_LONG).show();
    }

    public  void akeText(String string)
    {
        Toast.makeText(context, " "+ string , Toast.LENGTH_SHORT).show();
    }


}
