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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Home.Home;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.constants.SharedKeys;
import droid.nir.testapp1.noveu.constants.constants;
import droid.nir.testapp1.noveu.welcome.Initial;
import droid.nir.testapp1.noveu.welcome.about.animations.DepthPageTransform;
import droid.nir.testapp1.noveu.welcome.about.animations.ZoomPageTransform;

public class About extends FragmentActivity implements ViewPager.OnPageChangeListener, OnAboutHelperListener, View.OnClickListener {


    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private int position =0;
    private  static  final  int NUM_PAGES = 4;
    private static final int[] page_dots = {R.id.page1,R.id.page2,R.id.page3,R.id.page4};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_new);

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

        int version = -1;
        Intent intent = getIntent();
        if (intent != null) {
            version = intent.getIntExtra("version",-4);
            Log.d("ab","version "+version);
            if (version != constants.VERSION && version!= -4){
                Log.d("ab","starting inital");
                Initial.startInitialops(this, this, version);
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
        Log.d("ab","page scrolled "+position);

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("ab","page selected "+position);
        this.position = position;
        changeParentView(position);
        changePageIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("ab","page state "+state);


    }

    @Override
    public void changeFont(View view) {

        Context context = view.getContext();
        Import.settypefaces(context, "Ribbon_V2_2011.otf", (TextView) view.findViewById(R.id.about_title));
        Import.settypefaces(context, "Raleway-Medium.ttf", (TextView) view.findViewById(R.id.about_desp));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip:
                goHome();
                break;
        }
    }

    private void goHome() {
        Import.setSharedPref(this, SharedKeys.about_shown_till, position);
        Intent return_intent = new Intent();
        return_intent.putExtra("shown_till",position);
        setResult(constants.SUCCESS_CODE, return_intent);
        finish();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("ab","postion "+position);
            switch ( position){
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
            return  NUM_PAGES;
        }
    }

    private void changeParentView(int position){

            switch (position) {
                case 0:
                    findViewById(R.id.pagerparent).setBackgroundColor(getResources().getColor(R.color.about_primary2));
                    Import.setStatusBarColor(this, this, R.color.about_dark1);
                    break;
                case 1:
                    findViewById(R.id.pagerparent).setBackgroundColor(getResources().getColor(R.color.about_primary3));
                    Import.setStatusBarColor(this, this, R.color.about_dark2);
                    break;
                case 2:
                    findViewById(R.id.pagerparent).setBackgroundColor(getResources().getColor(R.color.about_primary4));
                    Import.setStatusBarColor(this, this, R.color.about_dark3);
                    break;
                case 3:
                    findViewById(R.id.pagerparent).setBackgroundColor(getResources().getColor(R.color.about_primary4));
                    Import.setStatusBarColor(this, this, R.color.about_dark4);
                    break;


            }
        }


    private void changePageIndicator(int position) {

        Resources res = getResources();
        for (int i = 0; i < NUM_PAGES; i++) {
            int pic_id = (i== position) ? R.drawable.indication_full : R.drawable.indication_outline;
            Drawable drawable = res.getDrawable(pic_id);
            ((ImageView) findViewById(page_dots[i])).setImageDrawable(drawable);
        }
    }
    }


