package org.guateora.oraconmigo.fragments;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.guateora.oraconmigo.MainActivity;
import org.guateora.oraconmigo.R;
import org.guateora.oraconmigo.utils.ParseConstants;
import org.guateora.oraconmigo.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

        setUpPray(view);

        FloatingActionButton checkin_btn = (FloatingActionButton) view.findViewById(R.id.main_check_in_pray);
        checkin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location currentLocation = ((MainActivity) getActivity()).mCurrentLocation;
                if (currentLocation != null) {
                    Log.w("TAG", "Longitude: " + currentLocation.getLongitude());
                }
            }
        });

        return view;
    }

    private void setUpPray(final View view){
        Calendar today_init = Utils.getTodayInit();
        Calendar today_end = Utils.getTodayEnd();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.TABLE_PRAY);
        query.whereGreaterThan(ParseConstants.TABLE_PRAY_FIELD_DATE, today_init.getTime());
        query.whereLessThan(ParseConstants.TABLE_PRAY_FIELD_DATE, today_end.getTime());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> prayList, ParseException e) {
                if (e == null) {
                    if(prayList.size() > 0){
                        ParseObject pray = prayList.get(0);

                        TextView title = (TextView) view.findViewById(R.id.main_title);
                        title.setText(pray.getString(ParseConstants.TABLE_PRAY_FIELD_DAY));

                        TextView text = (TextView) view.findViewById(R.id.main_text);
                        text.setText(pray.getString(ParseConstants.TABLE_PRAY_FIELD_TEXT));

                        TextView verse = (TextView) view.findViewById(R.id.main_verse);
                        verse.setText(pray.getString(ParseConstants.TABLE_PRAY_FIELD_VERSE));

                        View pray_container = view.findViewById(R.id.main_pray_container);
                        pray_container.setBackgroundColor(Color.parseColor(pray.getString(ParseConstants.TABLE_PRAY_FIELD_COLOR)));

                    } else{
                        Log.d("PRAY", "There is nothing on Pray: ");
                    }
                } else {
                    Log.d("PRAY", "Error: " + e.getMessage());
                }
            }
        });
    }
}
