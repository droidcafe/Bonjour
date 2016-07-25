package droid.nir.testapp1;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import droid.nir.testapp1.noveu.Util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;




/**
 * A simple {@link Fragment} subclass.
 */
public class Navigation_Fragment extends Fragment implements View.OnClickListener {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Context context;
    private TextView home,decision,event,list,remainder;

    public Navigation_Fragment() {
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
        imageView.setOnClickListener(this);

        home.setOnClickListener(this);
        decision.setOnClickListener(this);
        event.setOnClickListener(this);
        list.setOnClickListener(this);
        remainder.setOnClickListener(this);


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




    public void setup(DrawerLayout drawerlayout, final Toolbar toolbar, final FloatingActionMenu floatingActionMenu) {
        mDrawerLayout = drawerlayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerlayout,toolbar, R.string.drawer_open,R.string.drawer_close){


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
                Log.i("openein", "open in 1");
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

                Log.d("alldecisions", "sliding on nav frag");


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
                getActivity().finish();
                break;
            case R.id.navevent:
                Intent intent2 = new Intent(context,AllEvents.class);
                startActivity(intent2);
                getActivity().finish();
                break;
              case R.id.navtodolist:
                  Intent intent3 = new Intent(context,AllLists.class);
                  startActivity(intent3);
                  getActivity().finish();
                break;
            case R.id.navremainder:
                Intent intent4 = new Intent(context,AllRemainders.class);
                startActivity(intent4);
                getActivity().finish();
                break;

            case R.id.imageView2:
               // Toast.makeText(context,R.string.imageclicking,Toast.LENGTH_LONG).show();
                break;

        }
    }
    public void setup(DrawerLayout drawerlayout, final Toolbar toolbar, final int choice) {

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
                if(choice==0)
                ((AllDecisions)getActivity()).ondrawerslide(slideOffset);
                else if(choice==1)
                    ((AllEvents)getActivity()).ondrawerslide(slideOffset);
                else if(choice==2)
                    ((AllLists)getActivity()).ondrawerslide(slideOffset);
                else if(choice==3)
                    ((AllRemainders)getActivity()).ondrawerslide(slideOffset);


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
