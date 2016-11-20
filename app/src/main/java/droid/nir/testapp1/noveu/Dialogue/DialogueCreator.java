package droid.nir.testapp1.noveu.Dialogue;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import droid.nir.testapp1.R;

/**
 * Created by droidcafe on 11/12/2016.
 */

public class DialogueCreator extends DialogFragment {

    OnDialogueClickListener mCallBack;
    String title;
    int arrayid;

    public static DialogueCreator newInstance(OnDialogueClickListener callback,int title, String dialoguetitle, int arrayid) {
        DialogueCreator frag = new DialogueCreator();
        frag.setOnDialogueClickListener(callback);
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putString("dialoguetitle", dialoguetitle);
        args.putInt("arrayid",arrayid);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallBack = (OnDialogueClickListener) context;

        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + " must implement OnDialogueClickListener");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args!=null)
        {
            title = args.getString("dialoguetitle");
            arrayid = args.getInt("arrayid");
        }
        else
        {
            title= "Unsuccesfull";
            arrayid = R.array.importance_arrays;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(getActivity());

        alertdialogbuilder.setTitle(title).setItems(arrayid, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCallBack.onClick(DialogueCreator.this,which);
            }
        });

        return alertdialogbuilder.create();
    }

    public void setOnDialogueClickListener(OnDialogueClickListener callBack) {
        mCallBack = callBack;
    }
    public interface OnDialogueClickListener
    {
        void onClick(DialogFragment dialogFragment,int m);
    }
}
