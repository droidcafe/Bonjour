package droid.nir.testapp1;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import droid.nir.testapp1.noveu.Home.Home;
import droid.nir.testapp1.noveu.Util.Log;


/**
 * A simple {@link Fragment} subclass.
 */
public class Navigation_Fragment2 extends Fragment implements View.OnClickListener {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Context context;
    private TextView home,decision,event,list,remainder;

    public Navigation_Fragment2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_, container, false);

        home = (TextView) view.findViewById(R.id.navhome);
        decision= (TextView) view.findViewById(R.id.navdecision);
        event= (TextView) view.findViewById(R.id.navevent);
        list= (TextView) view.findViewById(R.id.navtodolist);
        remainder= (TextView) view.findViewById(R.id.navremainder);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView2);

        home.setOnClickListener(this);
        decision.setOnClickListener(this);
        event.setOnClickListener(this);
        list.setOnClickListener(this);
        remainder.setOnClickListener(this);
        imageView.setOnClickListener(this);

        MaterialRippleLayout.on(home)
                .rippleOverlay(true)
                .rippleAlpha(0.4f)
                .rippleColor(11053224)
                .rippleHover(true)
                .rippleFadeDuration(2)
                .rippleDelayClick(false)
                .create();
        MaterialRippleLayout.on(decision)
                .rippleOverlay(true)
                .rippleAlpha(0.4f)
                .rippleColor(11053224)
                .rippleHover(true)
                .rippleFadeDuration(200)
                .rippleDelayClick(false)
                .create();

        MaterialRippleLayout.on(event)
                .rippleOverlay(true)
                .rippleAlpha(0.4f)
                .rippleColor(11053224)
                .rippleHover(true)
                .rippleFadeDuration(200)
                .rippleDelayClick(false)
                .create();

        MaterialRippleLayout.on(list)
                .rippleOverlay(true)
                .rippleAlpha(0.4f)
                .rippleColor(11053224)
                .rippleHover(true)
                .rippleFadeDuration(200)
                .rippleDelayClick(false)
                .create();

        MaterialRippleLayout.on(remainder)
                .rippleOverlay(true)
                .rippleAlpha(0.4f)
                .rippleColor(11053224)
                .rippleHover(true)
                .rippleFadeDuration(200)
                .rippleDelayClick(false)
                .create();


        return view;

    }

    public void setupnew (DrawerLayout drawerlayout, final Toolbar toolbar) {
        mDrawerLayout = drawerlayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerlayout,toolbar, R.string.drawer_open,R.string.drawer_close){


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
                Log.i("openein", "open in 2");


            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
                Log.i("close","close");

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);



                if(slideOffset<.4)
                {
                    toolbar.setAlpha(1 - slideOffset);
                }


                ((Home)getActivity()).ondrawerslide(slideOffset);

            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }


    public void setup(DrawerLayout drawerlayout, final Toolbar toolbar, final FloatingActionMenu floatingActionMenu) {
        mDrawerLayout = drawerlayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerlayout,toolbar, R.string.drawer_open,R.string.drawer_close){


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
                Log.i("openein", "open in 2");
                if(floatingActionMenu.isOpen())
                {
                    floatingActionMenu.close(true);

                }
                else {

                }

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
                Log.i("close","close");

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);



                if(slideOffset<.4)
                {
                    toolbar.setAlpha(1-slideOffset);
                }
                if(floatingActionMenu.isOpen()) {
                    floatingActionMenu.close(true);

                }

                ((MainActivity)getActivity()).ondrawerslide(slideOffset);

            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.navhome:
                Intent intent = new Intent(context,MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.navdecision:
                Intent intent1 = new Intent(context,AllDecisions.class);
                startActivity(intent1);

                break;
            case R.id.navevent:
                Intent intent2 = new Intent(context,AllEvents.class);
                startActivity(intent2);

                break;
            case R.id.navtodolist:
                Intent intent3 = new Intent(context,Home.class);
                startActivity(intent3);
                break;
            case R.id.navremainder:


                break;

            case R.id.imageView2:
                //  Toast.makeText(context, R.string.imageclicking, Toast.LENGTH_LONG).show();
                break;

        }
    }
    public void setup(DrawerLayout drawerlayout, final Toolbar toolbar) {

        mDrawerLayout = drawerlayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerlayout,toolbar, R.string.drawer_open,R.string.drawer_close){


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
                Log.i("openein", "open");


            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
                Log.i("close","close");

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                if(slideOffset<.4)
                {
                    toolbar.setAlpha(1-slideOffset);
                }


            }

        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }


}
