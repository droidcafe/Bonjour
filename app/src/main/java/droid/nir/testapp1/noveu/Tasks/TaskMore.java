package droid.nir.testapp1.noveu.Tasks;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.dB.DBProvider;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TaskMore.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TaskMore#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskMore extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public TaskMore() {
    }


    public static TaskMore newInstance() {
        TaskMore fragment = new TaskMore();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task__more, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();
        Import.settypefaces(context,"Raleway-Light.ttf", (TextView) view.findViewById(R.id.share_text));
        Import.settypefaces(context,"Raleway-Light.ttf", (TextView) view.findViewById(R.id.delete_text));

        (view.findViewById(R.id.share)).setOnClickListener(this);
        (view.findViewById(R.id.delete)).setOnClickListener(this);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMoreInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        Uri uri = Uri.EMPTY;
        switch (v.getId()) {
            case R.id.share:
                uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, "sharetask");
                break;
            case R.id.delete:
                uri = Uri.withAppendedPath(DBProvider.CONTENT_URI_TASKS, "deletetask");
                break;
        }
        onButtonPressed(uri);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onMoreInteraction(Uri uri);
    }
}
