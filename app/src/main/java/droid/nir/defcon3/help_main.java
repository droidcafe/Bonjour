package droid.nir.defcon3;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;

import android.graphics.Typeface;
import android.os.Handler;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.Calendar;

import droid.nir.testapp1.Add_Event2;
import droid.nir.testapp1.Add_Pending;
import droid.nir.testapp1.Add_Todo_list;
import droid.nir.testapp1.MainActivity;
import droid.nir.testapp1.R;

public class help_main extends ActionBarActivity implements View.OnClickListener, View.OnLongClickListener {


    Context context;
    FloatingActionMenu leftCenterMenu;
    FloatingActionButton leftCenterButton;
    Calendar calendar;
    String dateselected = "", todaydate;
    int date, month, year,isnext;
    ImageView img_animation,hand20,hand21,hand22,hand23;
    TranslateAnimation animation,downanimation;
    int round,from=0,isopened=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent passintent = getIntent();
        isnext =1;
        if(passintent!=null)
        {
           round = passintent.getExtras().getInt("round");
            from = passintent.getExtras().getInt("from");
        }
        calendar = Calendar.getInstance();
        date = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        todaydate = date + "/" + month + "/" + year;
        context = this;

        setContentView(R.layout.activity_help_main);

        int right = (int)getResources().getDimension(R.dimen.handpaddingright);
        int bottm = (int)getResources().getDimension(R.dimen.handpaddingbottom);
        float xfinal = getResources().getInteger(R.integer.floatf);
        final TextView title = (TextView) findViewById(R.id.title);
        final TextView subtitle = (TextView) findViewById(R.id.subtitle);
        img_animation = (ImageView) findViewById(R.id.hand);
        img_animation.setPadding(0,0,right,bottm);
        ImageView background  = (ImageView) findViewById(R.id.background);
        hand20  = (ImageView) findViewById(R.id.hand20);
        hand21  = (ImageView) findViewById(R.id.hand21);
        hand22  = (ImageView) findViewById(R.id.hand22);
        hand23  = (ImageView) findViewById(R.id.hand23);

        final Typeface typeface = Typeface.createFromAsset(getAssets(),"Raleway-Medium.ttf");

        final Typeface typeface2 = Typeface.createFromAsset(getAssets(),"Raleway-Light.ttf");

        if(from==0)
        {
            findViewById(R.id.skip).setOnClickListener(this);
            findViewById(R.id.next).setOnClickListener(this);
        }
        else
        {
            findViewById(R.id.skip).setVisibility(View.GONE);
            findViewById(R.id.next).setVisibility(View.GONE);
            findViewById(R.id.stick).setVisibility(View.GONE);
        }




       title.setTypeface(typeface);
        subtitle.setTypeface(typeface2);

        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(3000);

        final Animation inn = new AlphaAnimation(0.0f, 1.0f);
        inn.setDuration(3000);

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(2000);

        animation = new TranslateAnimation(0.0f, xfinal,
                0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(800);  // animation duration
        animation.setRepeatCount(200);  // animation repeat count
        animation.setRepeatMode(2);


        downanimation = new TranslateAnimation(0.0f,0.0f,
                0.0f, 50.0f);
        downanimation.setDuration(800);
        downanimation.setRepeatCount(200);
        downanimation.setRepeatMode(2);
        switch (round)
        {
            case 0:
                title.setText(getResources().getString(R.string.pendingdecisions));
                subtitle.setText(getResources().getString(R.string.pendingmoto));
                background.setImageResource(R.drawable.button_action_blue);
                break;
            case 1:
                title.setText(getResources().getString(R.string.events));
                subtitle.setText(getResources().getString(R.string.eventmoto));
                background.setImageResource(R.drawable.button_action_blue);
              //  background.setImageDrawable(getDrawable(R.drawable.bb6));
                break;

            case 2:
                title.setText(getResources().getString(R.string.todolist));
                subtitle.setText(getResources().getString(R.string.todolistmoto));
                background.setImageResource(R.drawable.button_action_blue);
                break;

            case 3:
                title.setText(getResources().getString(R.string.reminders));
                subtitle.setText(getResources().getString(R.string.remindermoto));
                background.setImageResource(R.drawable.button_action_blue);
                break;
        }

        title.startAnimation(inn);
        subtitle.startAnimation(in);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // Update UI

                switch (round) {
                    case 0:
                        subtitle.setText(getResources().getString(R.string.hcreatepending));

                        break;
                    case 1:
                        subtitle.setText(getResources().getString(R.string.hcreateevent));

                        break;

                    case 2:
                        subtitle.setText(getResources().getString(R.string.hcreatetodolist));

                        break;

                    case 3:
                        subtitle.setText(getResources().getString(R.string.hcreatereminder));
                        break;
                }
                Log.d("help_main", "runnig calling");
                subtitle.startAnimation(inn);
                setupFloatingButton();
                img_animation.setVisibility(View.VISIBLE);
                img_animation.startAnimation(animation);

            }
        }, 5000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isopened==0)
                {

                }
//                leftCenterMenu.open(true);
            }
        },9000);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // Update UI

                Log.d("help_main", "runnig calling");
                if(from==0&&isnext==1)
                {
                    if(round!=3)
                    {


                        Intent newintent = new Intent(help_main.this, help_main.class);
                        newintent.putExtra("round",++round);
                        newintent.putExtra("from",0);
                        startActivity(newintent);
                         help_main.this.finish();
                    }
                    else {



                        Intent newintent =new Intent(help_main.this, MainActivity.class);
                        newintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);

                    }
                }
                else  {
                    finish();
                }


            }
        }, 10000);



        // repeat animation (left to right, right to left )
        //animation.setFillAfter(true);


        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d("help_main", "start calling");
            }


            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("help_main", "end calling");
                subtitle.startAnimation(out);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help_main, menu);
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


    public void setupFloatingButton() {
        int redActionButtonSize = context.getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
        int redActionButtonMargin = context.getResources().getDimensionPixelOffset(R.dimen.action_button_marginn);
        int redActionButtonContentSize = context.getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        int redActionButtonContentMargin = context.getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        int redActionMenuRadius = context.getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
        int blueSubActionButtonSize = context.getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        int blueSubActionButtonContentMargin = context.getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

        final ImageView fabIconStar = new ImageView(context);
        fabIconStar.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_plus_white_36dp));

        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
        starParams.setMargins(redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin);
        fabIconStar.setLayoutParams(starParams);

        FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconStarParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);

        leftCenterButton = new FloatingActionButton.Builder(context)
                .setContentView(fabIconStar, fabIconStarParams)
                .setBackgroundDrawable(R.drawable.button_action_blue_selector)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
                .setLayoutParams(starParams)
                .build();

        // Set up customized SubActionButtons for the right center menu
        SubActionButton.Builder lCSubBuilder = new SubActionButton.Builder(context);
        lCSubBuilder.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_action_red_selector));

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);
        lCSubBuilder.setLayoutParams(blueContentParams);
        // Set custom layout params
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        lCSubBuilder.setLayoutParams(blueParams);

        ImageView lcIcon1 = new ImageView(context);

        ImageView lcIcon2 = new ImageView(context);
        ImageView lcIcon3 = new ImageView(context);
        ImageView lcIcon4 = new ImageView(context);


        lcIcon1.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hourglass_empty_white_24dp));
        lcIcon2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_event_white_24dp));
        lcIcon3.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_format_list_bulleted_white_24dp));
        lcIcon4.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_alarm_add_white_24dp));

        SubActionButton sb1 = lCSubBuilder.setContentView(lcIcon1, blueContentParams).build();
        SubActionButton sb2 = lCSubBuilder.setContentView(lcIcon2, blueContentParams).build();
        SubActionButton sb3 = lCSubBuilder.setContentView(lcIcon3, blueContentParams).build();
        SubActionButton sb4 = lCSubBuilder.setContentView(lcIcon4, blueContentParams).build();


        // Build another menu with custom options
        leftCenterMenu = new FloatingActionMenu.Builder(context)
                .addSubActionView(sb1)
                .addSubActionView(sb2)
                .addSubActionView(sb3)
                .addSubActionView(sb4)

                .setRadius(redActionMenuRadius)
                .attachTo(leftCenterButton)
                .build();

        leftCenterMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                fabIconStar.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconStar, pvhR);
                animation.start();
                Log.d("opening", "openied");
                img_animation.clearAnimation();
                img_animation.setVisibility(View.GONE);
                isopened=1;
                switch (round)
                {
                    case 0:
                        hand20.setVisibility(View.VISIBLE);
                        hand20.startAnimation(help_main.this.animation);
                        break;
                    case 1:
                        hand21.setVisibility(View.VISIBLE);
                        hand21.startAnimation(help_main.this.animation);
                        break;

                    case 2:
                        hand22.setVisibility(View.VISIBLE);
                        hand22.startAnimation(help_main.this.animation);
                        break;

                    case 3:
                        hand23.setVisibility(View.VISIBLE);
                        hand23.startAnimation(help_main.this.downanimation);
                        break;
                }

            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                fabIconStar.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconStar, pvhR);
                animation.start();

                switch (round)
                {
                    case 0:
                        hand20.clearAnimation();
                        hand20.setVisibility(View.GONE);
                        break;
                    case 1:
                        hand21.clearAnimation();
                        hand21.setVisibility(View.GONE);
                        break;

                    case 2:
                        hand22.clearAnimation();
                        hand22.setVisibility(View.GONE);
                        break;

                    case 3:
                        hand23.clearAnimation();
                        hand23.setVisibility(View.GONE);
                        break;
                }

            }
        });


//        sb1.setId(0xb);
//        sb2.setId(0xc);
//        sb3.setId(0xd);
//        sb4.setId(0xe);
       /* sb1.setOnClickListener(this);
        sb2.setOnClickListener(this);
        sb3.setOnClickListener(this);
        sb4.setOnClickListener(this);*/

        sb1.setOnLongClickListener(this);
        sb2.setOnLongClickListener(this);
        sb3.setOnLongClickListener(this);
        sb4.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case 0xb:
                Intent goinginent1 = new Intent(this, Add_Pending.class);
                goinginent1.putExtra("customdate", todaydate);
                goinginent1.putExtra("p1", date);
                goinginent1.putExtra("p2", month);
                goinginent1.putExtra("p3", year);
                startActivity(goinginent1);

                break;
            case 0xc:
                Intent goinginent2 = new Intent(this, Add_Event2.class);
                goinginent2.putExtra("customdate", todaydate);
                goinginent2.putExtra("e1", date);
                goinginent2.putExtra("e2", month);
                goinginent2.putExtra("e3", year);
                startActivity(goinginent2);
                break;
            case 0xd:
                Intent goinginent3 = new Intent(this, Add_Todo_list.class);
                goinginent3.putExtra("customdate", todaydate);
                goinginent3.putExtra("l1", date);
                goinginent3.putExtra("l2", month);
                goinginent3.putExtra("l3", year);
                goinginent3.putExtra("oid",-3);
                startActivity(goinginent3);
                break;

            case 0xe:
             //   setupremainderdialog();
                //startActivity(new Intent(this,trial_alarm.class));
                break;

            case R.id.skip:
                isnext =0;
                startActivity(new Intent(help_main.this, MainActivity.class));
                break;
            case R.id.next:

                isnext =0;
                if(round!=3)
                {
                    Intent newintent = new Intent(help_main.this, help_main.class);
                    newintent.putExtra("round",++round);
                    newintent.putExtra("from",0);
                    startActivity(newintent);
                    help_main.this.finish();
                }
                else {

                    Intent newintent =new Intent(help_main.this, MainActivity.class);
                    newintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(newintent);

                }
break;
        }
       // leftCenterMenu.close(true);

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
