package droid.nir.testapp1.noveu.welcome.about;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Util.Log;


public class AboutFragment2 extends Fragment {


    private OnAboutHelperListener aboutHelperListener;
    public AboutFragment2() {

    }

    public static AboutFragment2 newInstance(int screen) {
        AboutFragment2 fragment = new AboutFragment2();
        Bundle args = new Bundle();
        args.putInt("screen_position",screen);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("af","oncreeate");
        if (getArguments() != null) {
            int screenPosition = getArguments().getInt("screen_position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_fragment2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("af","onviewcreated");
        if(aboutHelperListener != null){
            aboutHelperListener.changeFont(view);
            aboutHelperListener.setDimensions(view);

        }

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("af","onrestored");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        aboutHelperListener = (OnAboutHelperListener) context;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        aboutHelperListener = null;
    }

}
