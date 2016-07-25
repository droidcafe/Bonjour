package droid.nir.testapp1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.marcohc.robotocalendar.RobotoCalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class MainActivity2Activity extends ActionBarActivity implements RobotoCalendarView.RobotoCalendarListener {

    RobotoCalendarView robotoCalendarView;
    int currentIndex;
    Calendar currentCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);

        robotoCalendarView = (RobotoCalendarView) findViewById(R.id.robotoCalendarPicker);
        robotoCalendarView.setRobotoCalendarListener(this);

        currentCalendar = Calendar.getInstance(Locale.getDefault());
        robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSelected(Date date) {

        robotoCalendarView.markDayAsSelectedDay(date);

        final Random random = new Random(System.currentTimeMillis());
        final int style = random.nextInt(3);
        switch (style) {
            case 0:
                robotoCalendarView.markFirstUnderlineWithStyle(RobotoCalendarView.BLUE_COLOR, date);
                break;
            case 1:
                robotoCalendarView.markSecondUnderlineWithStyle(RobotoCalendarView.GREEN_COLOR, date);
                break;
            case 2:
                robotoCalendarView.markFirstUnderlineWithStyle(RobotoCalendarView.RED_COLOR, date);
                break;
            default:
                break;
        }

    }

    @Override
    public void onRightButtonClick() {
        currentIndex++;
        UpdateCalendar();
        if(currentIndex==0)
        {
            robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());
        }

    }

    private void UpdateCalendar() {
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH,currentIndex);
        robotoCalendarView.initializeCalendar(currentCalendar);

    }

    @Override
    public void onLeftButtonClick() {
        currentIndex--;
        UpdateCalendar();

        if(currentIndex==0)
        {
            robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());
        }

    }
}
