package droid.nir.testapp1.noveu.welcome.about;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.welcome.about.animations.DepthPageTransform;
import droid.nir.testapp1.noveu.welcome.about.animations.ZoomPageTransform;

public class About extends FragmentActivity implements ViewPager.OnPageChangeListener {


    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private int position =0;
    private  static  final  int NUM_PAGES = 4;
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
        changeParentView(position);

    }

    @Override
    protected void onResume() {
        super.onResume();
        changeParentView(position);
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
        changeParentView(position);
        this.position = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("ab","page state "+state);


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
    }


