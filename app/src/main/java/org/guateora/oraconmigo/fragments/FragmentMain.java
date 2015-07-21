package org.guateora.oraconmigo.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.guateora.oraconmigo.MainActivity;
import org.guateora.oraconmigo.R;

/**
 * Created by franz on 7/20/2015.
 */
public class FragmentMain extends Fragment {

    public static FragmentMain newInstance() { return new FragmentMain(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        FloatingActionButton checkin_btn = (FloatingActionButton) view.findViewById(R.id.main_check_in_pray);
        checkin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location currentLocation = ((MainActivity) getActivity()).mCurrentLocation;
                if(currentLocation != null){
                    Log.w("TAG", "Longitude: " + currentLocation.getLongitude());
                }
            }
        });

        return view;
    }
}
