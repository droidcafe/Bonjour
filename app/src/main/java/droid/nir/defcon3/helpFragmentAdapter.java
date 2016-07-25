package droid.nir.defcon3;




import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by user on 11/1/2015.
 */
public class helpFragmentAdapter extends FragmentPagerAdapter {

    Context context;
    String text[] = { "Welcome" ,"Pending Decisions","Events","To do lists","Reminder"};

    public helpFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {


        switch(position)
        {
           // case 0:
             //   return  new help_welcome();

          /*  case 0:
               // return new help_pending();
            case 1:
                return new help_event();
            case 2:
                return new help_todo();
            case 3:
                return new help_reminder();*/
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return 4;
    }
}
