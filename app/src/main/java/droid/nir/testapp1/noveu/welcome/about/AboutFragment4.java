package droid.nir.testapp1.noveu.welcome.about;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import droid.nir.testapp1.R;


public class AboutFragment4 extends Fragment {

    private OnAboutHelperListener aboutHelperListener;
    public AboutFragment4() {

    }

    public static AboutFragment4 newInstance(int screen) {
        AboutFragment4 fragment = new AboutFragment4();
        Bundle args = new Bundle();
        args.putInt("screen_position",screen);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int screenPosition = getArguments().getInt("screen_position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_fragment4, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(aboutHelperListener != null){
            aboutHelperListener.changeFont(view);
            aboutHelperListener.setDimensions(view);
        }
//        Window window =getActivity().getWindow();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(getResources().getColor(R.color.about_dark4));
//        }

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
