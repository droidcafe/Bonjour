package droid.nir.testapp1.noveu.Dialogue;



import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import droid.nir.testapp1.noveu.Util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Tasks.Add_Expand;
import droid.nir.testapp1.noveu.Tasks.Add_minimal;
import droid.nir.testapp1.noveu.dB.DBProvider;
import droid.nir.testapp1.noveu.dB.Project;

/**
 * Created by droidcafe on 2/25/2016.
 */
public class    DialogueSelectorTasks extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, View.OnClickListener {
    SimpleCursorAdapter cursorAdapter;
    ListView listView;
    int choice;
      /**
       * 1 - from add minimal
        *2 - from add expand
         */

    public static DialogueSelectorTasks newInstance(int choice)
    {
        DialogueSelectorTasks dialogueSelectorTasks = new DialogueSelectorTasks();
        Bundle args = new Bundle();
        args.putInt("choice", choice);
        dialogueSelectorTasks.setArguments(args);
        return dialogueSelectorTasks;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        choice = getArguments().getInt("choice");
        AlertDialog.Builder alertBuilder= new AlertDialog.Builder(getActivity());
        alertBuilder.setView(R.layout.dialog_selectortask)
                .setTitle(getResources().getString(R.string.pickpro));

        return alertBuilder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        listView = (ListView) getDialog().findViewById(R.id.prolist);
        getDialog().findViewById(R.id.addpro).setOnClickListener(this);
        String[] from = {Project.columnNames[0][1],Project.columnNames[0][2]};
        int to[] ={R.id.proname,R.id.prosize};
       cursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_projectselector,null,from,to);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(this);
        getLoaderManager().initLoader(0,null,this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS,"project");
        return new CursorLoader(getActivity(),uri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //String select =  listView.getItemAtPosition(position);
    TextView textView = (TextView) view.findViewById(R.id.proname);
        String select = textView.getText().toString();
        Log.d("dst", " " + position + " " + id + " " + select);
        if (choice==1)
        ((Add_minimal)getActivity()).renameProject(select,(int)id);
        else if (choice==2)
            ((Add_Expand)getActivity()).renameProject(select, (int) id);

        dismiss();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addpro:
                dismiss();
                DialogFragment alertDialog = DialogueProjectManager.newInstanceInsert(choice);
                alertDialog.show(getFragmentManager(), "dialogs");
                break;
        }
    }
}
