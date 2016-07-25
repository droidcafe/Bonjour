package droid.nir.testapp1.noveu.NavDrw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import droid.nir.testapp1.noveu.Util.Log;
import android.view.MenuItem;
import android.view.View;

import droid.nir.testapp1.AllDecisions;
import droid.nir.testapp1.AllEvents;
import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Home.Home;
import droid.nir.testapp1.noveu.Projects.ProjectManager;
import droid.nir.testapp1.noveu.bonjoursettings.BonjourSettings;
import droid.nir.testapp1.noveu.dB.Tasks;

/**
 * Created by droidcafe on 3/13/2016.
 */
public class setNav implements NavigationView.OnNavigationItemSelectedListener {

    static Context context;
    static Activity activity;

     int checkeditem;
    public setNav(Context context, Activity activity){
        this.activity = activity;
        this.context = context;
    }

    public void setupNavigation(int checkedItem) {



        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(setNav.this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_home_nav);


        navigationView.setCheckedItem(checkedItem);
        checkeditem = checkedItem;

        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawer, (Toolbar) activity.findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if(checkeditem==item.getItemId())
        {
            DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        }
        else{
            switch (item.getItemId())
            {
                case R.id.nav_tasks:
                    Intent goinginent0 = new Intent(context, Home.class);
                    context.startActivity(goinginent0);
                    activity.finish();
                    break;
                case R.id.nav_pending:
                    Intent goinginent1 = new Intent(context, AllDecisions.class);
                    context.startActivity(goinginent1);
                    activity.finish();
                    break;
                case R.id.nav_event:
                    Intent goinginent2 = new Intent(context, AllEvents.class);
                    context.startActivity(goinginent2);
                    activity.finish();
                    break;
                case R.id.nav_today:

                    break;
                case R.id.nav_pro:
                    Intent goinginent4 = new Intent(context, ProjectManager.class);
                    context.startActivity(goinginent4);
                    activity.finish();
                    break;
                case R.id.nav_settings:
                    Intent goinginent5 = new Intent(context, BonjourSettings.class);
                    context.startActivity(goinginent5);
                    activity.finish();
                    break;

            }

        }


        return true;
    }
}
