package droid.nir.testapp1.noveu.welcome.about;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.welcome.Initial;
import droid.nir.testapp1.noveu.welcome.auth.SignIn;

public class About extends FragmentActivity implements ViewPager.OnPageChangeListener, OnAboutHelperListener, View.OnClickListener {


    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private int position = 0;
    private static final int NUM_PAGES = 4;
    private static final int[] page_dots = {R.id.page1, R.id.page2, R.id.page3, R.id.page4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_new);

      //  Import.getLogs();
        position = 0;
        mPager = (ViewPager) findViewById(R.id.aboutPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        //   mPager.setPageTransformer(true, new DepthPageTransform());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(this);
        setup();
        changeParentView(position);
        changePageIndicator(position);

    }

    private void setup() {
        Import.settypefaces(this, "Raleway-Light.ttf", (TextView) findViewById(R.id.skip));
        findViewById(R.id.skip).setOnClickListener(this);
        findViewById(R.id.next).setOnClickListener(this);

        int version = -1;
        Intent intent = getIntent();
        if (intent != null) {
            version = intent.getIntExtra("version", -4);
            Log.d("ab", "version " + version);
            if (version != constants.VERSION && version != -4) {
                Log.d("ab", "starting inital " + version);
                new Initial.AsyncInitial(this, this, version).execute();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeParentView(position);
        changePageIndicator(position);
    }

    @Override
    public void onBackPressed() {

        if (mPager.getCurrentItem() == 0) {

            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("ab", "page scrolled " + position);
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("ab", "page selected " + position);
        this.position = position;
        changeParentView(position);
        changePageIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("ab", "page state " + state);


    }

    @Override
    public void changeFont(View view) {

        Context context = view.getContext();
        Import.settypefaces(context, "Ribbon_V2_2011.otf", (TextView) view.findViewById(R.id.about_title));
        Import.settypefaces(context, "Raleway-Medium.ttf", (TextView) view.findViewById(R.id.about_desp));
    }

    @Override
    public void setDimensions(View view) {

        Log.d("about", "setting dimesions");
        ImageView sub1 = (ImageView) view.findViewById(R.id.promo_subpic1);
        ImageView sub2 = (ImageView) view.findViewById(R.id.promo_subpic2);
        ImageView sub3 = (ImageView) view.findViewById(R.id.promo_subpic3);

        int sub1_left = (int) getResources().getDimension(R.dimen.promosub1_margin_left);
        int sub1_bottom = (int) getResources().getDimension(R.dimen.promosub1_margin_bottom);
        int sub2_left = (int) getResources().getDimension(R.dimen.promosub2_margin_left);
        int sub3_left = (int) getResources().getDimension(R.dimen.promosub3_margin_left);
        int sub3_bottom = (int) getResources().getDimension(R.dimen.promosub3_margin_bottom);
        int sub3_right = (int) getResources().getDimension(R.dimen.promosub3_margin_right);

        Log.d("about", "setting dimesions " + sub1_left + sub3_bottom + sub1_bottom + sub2_left);


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(sub1_left, 0, 0, sub1_bottom);
        params.addRule(RelativeLayout.ABOVE, R.id.man);
        sub1.setLayoutParams(params);

        params2.addRule(RelativeLayout.RIGHT_OF, sub1.getId());
        params2.setMargins(sub2_left, 0, 0, 0);
        params2.addRule(RelativeLayout.ABOVE, R.id.man);
        sub2.setLayoutParams(params2);

        params3.addRule(RelativeLayout.RIGHT_OF, sub2.getId());
        params3.setMargins(sub3_left, 0, sub3_right, sub3_bottom);
        params3.addRule(RelativeLayout.ABOVE, R.id.man);
        sub3.setLayoutParams(params3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip:
                goHome();
                break;
            case R.id.next:
                if (position == NUM_PAGES - 1) {
                    goHome();
                } else {
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
                }
        }
    }

    private void goHome() {
//        Import.setSharedPref(this, SharedKeys.about_shown_till, position);
//        Intent return_intent = new Intent();
//        return_intent.putExtra("shown_till",position);
//        setResult(constants.SUCCESS_CODE, return_intent);

        startActivity(new Intent(this, SignIn.class));
        finish();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("ab", "postion " + position);
            switch (position) {
                case 0:
                    return AboutFragment1.newInstance(position);
                case 1:
                    return AboutFragment2.newInstance(position);
                case 2:
                    return AboutFragment3.newInstance(position);
                case 3:
                    return AboutFragment4.newInstance(position);

            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private void changeParentView(int position) {

        switch (position) {
            case 0:
                findViewById(R.id.pagerparent).setBackgroundColor(getResources().getColor(R.color.about_primary2));
                Import.setStatusBarColor(this, this, R.color.about_dark1);
                findViewById(R.id.skip).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.next)).setText(getResources().getString(R.string.next));
                break;
            case 1:
                findViewById(R.id.pagerparent).setBackgroundColor(getResources().getColor(R.color.about_primary3));
                Import.setStatusBarColor(this, this, R.color.about_dark2);
                findViewById(R.id.skip).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.next)).setText(getResources().getString(R.string.next));
                break;
            case 2:
                findViewById(R.id.pagerparent).setBackgroundColor(getResources().getColor(R.color.about_primary4));
                Import.setStatusBarColor(this, this, R.color.about_dark3);
                findViewById(R.id.skip).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.next)).setText(getResources().getString(R.string.next));
                break;
            case 3:
                findViewById(R.id.pagerparent).setBackgroundColor(getResources().getColor(R.color.about_primary4));
                Import.setStatusBarColor(this, this, R.color.about_dark4);
                findViewById(R.id.skip).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.next)).setText(getResources().getString(R.string.start));
                break;


        }
    }


    private void changePageIndicator(int position) {

        Resources res = getResources();
        for (int i = 0; i < NUM_PAGES; i++) {
            int pic_id = (i == position) ? R.drawable.indication_full : R.drawable.indication_outline;
            Drawable drawable = res.getDrawable(pic_id);
            ((ImageView) findViewById(page_dots[i])).setImageDrawable(drawable);
        }
    }
}


