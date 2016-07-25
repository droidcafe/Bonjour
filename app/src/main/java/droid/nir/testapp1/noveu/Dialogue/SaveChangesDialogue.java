package droid.nir.testapp1.noveu.Dialogue;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Projects.ProjectManager;


public class SaveChangesDialogue extends DialogFragment {

    int choice;
    public static SaveChangesDialogue newInstance(int choice) {
        SaveChangesDialogue fragment = new SaveChangesDialogue();
        Bundle args = new Bundle();
        args.putInt("choice", choice);
        fragment.setArguments(args);
        return fragment;
    }

}