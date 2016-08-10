package droid.nir.testapp1.noveu.Dialogue;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Projects.ProjectManager;
import droid.nir.testapp1.noveu.Tasks.Add_Expand;
import droid.nir.testapp1.noveu.Tasks.Add_minimal;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.dB.DBProvider;
import droid.nir.testapp1.noveu.dB.Project;
import droid.nir.testapp1.toast;

/**
 * Created by droidcafe on 2/24/2016.
 */
public class DialogueProjectManager extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {


    public enum DialogueMode {insert, update, delete}

    ;

    DialogueMode mode;
    String proname;
    MaterialBetterSpinner spinnerProlist;

    /**
     * to create dialogue for adding new labels
     *
     * @param from screen from which it was called
     *             0 - from {@link  ProjectManager}
     *             1- from {@link Add_minimal}
     *             2- from {@link Add_Expand}
     * @return
     */
    public static DialogueProjectManager newInstanceInsert(int from) {
        DialogueProjectManager proAdd = new DialogueProjectManager();
        Bundle bundle = new Bundle();
        bundle.putInt("mode", 0);
        bundle.putInt("from", from);
        proAdd.setArguments(bundle);
        return proAdd;
    }

    public static DialogueProjectManager newInstanceUpdate(String proname, int id) {
        DialogueProjectManager proAdd = new DialogueProjectManager();
        Bundle bundle = new Bundle();
        bundle.putString("proname", proname);
        bundle.putInt("proid", id);
        bundle.putInt("mode", 1);
        proAdd.setArguments(bundle);
        Log.d("dpm", "new instance " + proname + " " + bundle.toString());
        return proAdd;
    }

    public static DialogueProjectManager newInstanceDelete(String proname, int id) {
        DialogueProjectManager proAdd = new DialogueProjectManager();
        Bundle bundle = new Bundle();
        bundle.putString("proname", proname);
        bundle.putInt("proid", id);
        bundle.putInt("mode", 2);
        proAdd.setArguments(bundle);
        return proAdd;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        int choice = getArguments().getInt("mode");
        final int from = getArguments().getInt("from", -1);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        final toast maketext = new toast(getActivity());
        /**
         * insert
         */
        if (choice == 0) {
            mode = DialogueMode.insert;

            alertBuilder.setView(R.layout.dialogue_project_manager);
            alertBuilder.setTitle(getResources().getString(R.string.addproject_title));
            alertBuilder.setPositiveButton(getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            final EditText tv = (EditText) getDialog().findViewById(R.id.proname);

                            String text = tv.getText().toString();
                            if (tv.equals(""))
                                maketext.makeText(getResources().getString(R.string.noproname));
                            else {
                                int id = Project.doPositiveInsert(getActivity(), text);
                                switch (from){
                                    case 0:
                                        ((ProjectManager) getActivity()).doPositiveInsert(text,id);
                                        break;
                                    case 1:
                                        ((Add_minimal) getActivity()).renameProject(text,id);
                                        break;
                                    case 2:
                                        ((Add_Expand)getActivity()).renameProject(text, id);
                                }

                            }
                        }
                    }
            )
                    .setNegativeButton(getResources().getText(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // ((ProjectManager) getActivity()).doNegativeClick();
                                }
                            }
                    )
                    .create();
        }
        /**
         * update
         */
        else if (choice == 1) {
            proname = getArguments().getString("proname");
            final int id = getArguments().getInt("proid");
            // proid = id;
            mode = DialogueMode.update;

            alertBuilder.setView(R.layout.dialogue_project_manager);
            alertBuilder.setTitle(getResources().getString(R.string.updateproject_title));
            alertBuilder.setPositiveButton(getResources().getString(R.string.update),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            final EditText tv = (EditText) getDialog().findViewById(R.id.proname);

                            String text = tv.getText().toString();
                            if (tv.equals(""))
                                maketext.makeText(getResources().getString(R.string.noproname));
                            else {
                                ((ProjectManager) getActivity()).doPositiveUpdate(text, id);

                            }
                        }
                    }
            )
                    .setNegativeButton(getResources().getText(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // ((ProjectManager) getActivity()).doNegativeClick();
                                }
                            }
                    )
            ;

        }

        /**
         * delete the project
         */

        else if (choice == 2) {
            mode = DialogueMode.delete;

            proname = getArguments().getString("proname");
            final int id = getArguments().getInt("proid");
            alertBuilder.setView(R.layout.dialogue_projectmanager_delet);
            alertBuilder.setTitle(getActivity().getString(R.string.DElete, new Object[]{proname}));
            alertBuilder.setPositiveButton(getResources().getString(R.string.delete),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            ((ProjectManager) getActivity()).doPositiveDelete(id);


                        }
                    }
            )
                    .setNegativeButton(getResources().getText(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // ((ProjectManager) getActivity()).doNegativeClick();
                                }
                            }
                    )
            ;

        }
        return alertBuilder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        Import anImport = new Import(getActivity());
        switch (mode) {
            case insert:
                TextView intro = (TextView) getDialog().findViewById(R.id.intro);
                anImport.settypefaces("SourceSansPro-Regular.otf", intro);
                intro.setText(getResources().getText(R.string.addproject));
                break;
            case update:
                EditText projectname = (EditText) getDialog().findViewById(R.id.proname);
                TextView introtext = (TextView) getDialog().findViewById(R.id.intro);
                anImport.settypefaces("SourceSansPro-Regular.otf", introtext);
                introtext.setText(getResources().getText(R.string.updateproject));
                projectname.setText(proname);
                break;

            case delete:

                TextView delete = (TextView) getDialog().findViewById(R.id.contexttext);
                anImport.settypefaces("SourceSansPro-Regular.otf", delete);
                delete.setText(getResources().getText(R.string.deleteproject));

                String[] list = {getString(R.string.prodelete_task1, new Object[]{proname}), getString(R.string.prodelete_task2)};

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, list);

                MaterialBetterSpinner spinner = (MaterialBetterSpinner) getDialog().findViewById(R.id.taskoptions);
                spinner.setHint(getString(R.string.prodelete_hint, new Object[]{proname}));
                Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "SourceSansPro-Regular.otf");
                spinner.setTypeface(typeface);
                //    spinner.setHintTextColor(getResources().getColor(R.color.black));
                spinner.setAdapter(adapter);

                spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        if (position == 0) {
                            getDialog().findViewById(R.id.moveto).setVisibility(View.GONE);

                        } else if (position == 1) {
                            getLoaderManager().initLoader(0, null, DialogueProjectManager.this);

                        }
                    }
                });

        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.taskoptions:

                break;
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("dpm", "loader create");
        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, "project");
        String selection = "" + Project.columnNames[0][1] + " <> ?";
        String selectionArgs[] = {proname};
        return new CursorLoader(getActivity(), uri, new String[]{"_id", Project.columnNames[0][1]}, selection, selectionArgs, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("dpm", "loader finish");
        String list[] = new String[data.getCount()];
        int ids[] = new int[data.getCount()];
        int i = 0;
        data.moveToPosition(-1);
        Log.d("dpm", "loader " + data.getPosition());

        while (data.moveToNext()) {
            list[i] = data.getString(data.getColumnIndex(Project.columnNames[0][1]));
            ids[i++] = data.getInt(data.getColumnIndex(Project.columnNames[0][0]));
        }
        getDialog().findViewById(R.id.moveto).setVisibility(View.VISIBLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, list);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "SourceSansPro-Regular.otf");
        spinnerProlist = (MaterialBetterSpinner) getDialog().findViewById(R.id.moveto);
        spinnerProlist.setTypeface(typeface);
        spinnerProlist.setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        Log.d("dpm", "loader reset");
    }

}
