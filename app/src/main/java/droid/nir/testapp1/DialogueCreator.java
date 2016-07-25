package droid.nir.testapp1;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by user on 7/10/2015.
 */
public class DialogueCreator extends DialogFragment{



    public static DialogueCreator newInstance(int title,String dialoguetitle,int arrayid) {
       DialogueCreator frag = new DialogueCreator();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putString("dialoguetitle", dialoguetitle);
        args.putInt("arrayid",arrayid);
        frag.setArguments(args);
        return frag;
    }

    String title;
    int arrayid;
    public DialogueCreator()
    {
        super();
    }

    public interface DialogListener
    {
        public void userClicked(DialogFragment dialogFragment,int m);
    }

    DialogListener mDialogListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

       try {

           mDialogListener = (DialogListener) activity;

       }
       catch (ClassCastException e)
       {
           throw new ClassCastException(activity.toString()
               + " must implement NoticeDialogListener");
       }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        if(args!=null)
        {
            title = args.getString("dialoguetitle");
            arrayid = args.getInt("arrayid");
        }
        else
        {
            title= "Unsuccesffull";
            arrayid = R.array.importance_arrays;
        }

        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(getActivity());

        alertdialogbuilder.setTitle(title).setItems(arrayid, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // Toast.makeText(getActivity(),""+which+" ",Toast.LENGTH_LONG).show();
                mDialogListener.userClicked(DialogueCreator.this,which);
            }
        });

        return alertdialogbuilder.create();

    }
}
