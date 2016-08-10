package droid.nir.testapp1.noveu.Projects;


import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import droid.nir.testapp1.noveu.Util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Dialogue.DialogueProjectManager;
import droid.nir.testapp1.noveu.NavDrw.setNav;
import droid.nir.testapp1.noveu.Projects.Adapter.ProjectAdapter;
import droid.nir.testapp1.noveu.Util.AutoRefresh;
import droid.nir.testapp1.noveu.dB.DBProvider;
import droid.nir.testapp1.noveu.dB.Project;


public class ProjectManager extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {


    ProjectAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupNav();
        Loader<Object> loader =  getLoaderManager().getLoader(0);
        if(loader!=null&&!loader.isReset())
            getLoaderManager().restartLoader(0, null, this);
        else
            getLoaderManager().initLoader(0, null, this);

        findViewById(R.id.fab).setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(AutoRefresh.isRefreshNeeded(this)) {
            refresh();
          //  AutoRefresh.setRefreshDone(this);
        }
    }

    private void setupNav() {
        new setNav(this,this).setupNavigation(R.id.nav_pro);
        //((NavigationView)findViewById(R.id.nav_view)).setCheckedItem(R.id.nav_pro);
    }

    private void setupRecycler(Cursor cursor) {
        Log.d("pm",""+cursor.getCount());
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mAdapter =new ProjectAdapter(this,this,cursor);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.project_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
           refresh();
        }

        return super.onOptionsItemSelected(item);
    }
    private void refresh() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("pm","create loader");
        Uri uri =Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS,"project");
        return new CursorLoader(this,uri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("pm","finished loader");
        setupRecycler(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("projectmanager", "loader reset");
      //  getLoaderManager().restartLoader()
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.fab:
                setupProjectAddDialog();
                break;
        }
    }

    private void setupProjectAddDialog() {

        DialogFragment alertDialog = DialogueProjectManager.newInstanceInsert(0);
        alertDialog.show(getFragmentManager(), "dialogs");
    }

    /**
     *  add a new project
     * @param text the new or updated project name
     * @param id
     */

    public void doPositiveInsert(String text, int id) {
        Log.d("ProjectManager",""+text);


//            Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS,"project");
//            ContentValues contentValues = new ContentValues(2);
//            contentValues.put(Project.columnNames[0][1],text);
//            contentValues.put(Project.columnNames[0][2], 0);
//            Uri uriInsert =  getContentResolver().insert(uri,contentValues);
//
//            int id = Integer.parseInt(uriInsert.getLastPathSegment());
            mAdapter.addItem(text, 0, id);

    }

    /**
     * update a project
     * @param newProname
     * @param id
     */

    public void doPositiveUpdate(String newProname, int id)
    {
        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS,"project");
        ContentValues contentValues = new ContentValues(2);
        contentValues.put(Project.columnNames[0][1],newProname);
        contentValues.put(Project.columnNames[0][2], Project.getProjectSize(id));
        String where  = ""+Project.columnNames[0][0] + " = "+id;
        int update =  getContentResolver().update(uri, contentValues, where, null);


        mAdapter.changeProjectName(newProname);
        mAdapter.notifyItemChanged(mAdapter.lastClickedPosition);
    }

    public void doPositiveDelete(int id)
    {
        String where = Project.columnNames[0][0] + " = "+ id;
        Project.delete(this , 0, where,null,new Integer[]{id}, Project.deleteMode.quick);
        mAdapter.deleteProject();
      //  mAdapter.notifyItemRemoved(mAdapter.lastClickedPosition);
        //mAdapter.deleteProject();
    }
}