/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package droid.nir.defcon3;



import android.content.Context;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;


import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;


import java.util.Calendar;


import droid.nir.testapp1.R;

public class ScreenSlidePageFragment extends FragmentActivity {

    Context context;

    ViewPager mViewPager;
    PagerAdapter mPagerAdapter;
    FloatingActionMenu leftCenterMenu;
    FloatingActionButton leftCenterButton;
    Calendar calendar;
    String dateselected = "", todaydate;
    int date, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendar = Calendar.getInstance();
        date = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        todaydate = date + "/" + month + "/" + year;
        context = this;
        setContentView(R.layout.help_dialog);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        // mPagerAdapter = new helpAdapter(context,leftCenterMenu);

        helpFragmentAdapter helpFragmentAdapte = new helpFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(helpFragmentAdapte);


    }

}

