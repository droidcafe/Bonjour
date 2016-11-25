package droid.nir.testapp1.noveu.Projects;


import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Dialogue.DialogueProjectManager;
import droid.nir.testapp1.noveu.NavDrw.setNav;
import droid.nir.testapp1.noveu.Projects.Adapter.ProjectAdapter;
import droid.nir.testapp1.noveu.Util.AutoRefresh;
import droid.nir.testapp1.noveu.Util.DesignUtil;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.dB.DBProvider;
import droid.nir.testapp1.noveu.dB.Project;
import droid.nir.testapp1.noveu.ui.util.GridRecyclerSpaceDecoration;


public class ProjectManager extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {


    ProjectAdapter mAdapter;
    static final int LOADER_ID = 0101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupNav();
        Loader<Object> loader =  getLoaderManager().getLoader(0);
        if(loader!=null&&!loader.isReset())
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        else
            getLoaderManager().initLoader(LOADER_ID, null, this);

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
    }

    private void setupRecycler(Cursor cursor) {
        Log.d("pm", "" + cursor.getCount());
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new GridRecyclerSpaceDecoration(2,
                DesignUtil.dpToPx(this,5), true));
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

    private void refresh() {
        Log.d("pm","refreshing");
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("pm", "create loader");
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
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.swapAdapter(null,true);
     //   getLoaderManager().restartLoader(LOADER_ID,null,this);

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
     * @param no the no of tasks in that new project
     */

    public void doPositiveInsert(String text, int id,int no) {
        Log.d("ProjectManager",""+text);
            mAdapter.addItem(text, no, id);

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

    public void doPositiveDelete(String text, int id,int no_tasks )
    {

        mAdapter.deleteProject();
        Log.d("dpm", "new pid " + id+" "+text);
        if(text != null){
            doPositiveInsert(text, id,no_tasks);
            Log.d("dpm", "new pid " + no_tasks);
        }
       // refresh();
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
        getMenuInflater().inflate(R.menu.project_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            refresh();
        }

        return super.onOptionsItemSelected(item);
    }
}