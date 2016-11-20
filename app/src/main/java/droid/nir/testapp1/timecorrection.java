package droid.nir.testapp1;

/**
 * Created by user on 8/30/2015.
 */
public class timecorrection {

    public String formatime(int hr,int min)
    {

        String temp, temp2;
        if (min < 10) {
            temp = ":0" + Integer.toString(min);

        } else {
            temp = ":" + Integer.toString(min);
        }
        if (hr < 10) {
            temp2 = "0" + Integer.toString(hr);

        } else {
            temp2 = Integer.toString(hr);
        }

       return  temp2.concat(temp);
    }
}
