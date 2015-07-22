package org.guateora.oraconmigo.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import org.guateora.oraconmigo.MainActivity;
import org.guateora.oraconmigo.MapsActivity;
import org.guateora.oraconmigo.R;
import org.guateora.oraconmigo.utils.ParseConstants;
import org.guateora.oraconmigo.utils.Utils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by franz on 7/20/2015.
 */
public class FragmentMain extends Fragment {

    private static final String TAG = "FragmentMain";

    private ProgressDialog dialog;

    public static FragmentMain newInstance() { return new FragmentMain(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        setUpPray(view);

        FloatingActionButton checkin_btn = (FloatingActionButton) view.findViewById(R.id.main_check_in_pray);
        checkin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location currentLocation = ((MainActivity) getActivity()).mCurrentLocation;
                if (currentLocation != null) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        saveLocation(currentUser, currentLocation, view);
                    } else {
                        Snackbar
                                .make(view, R.string.error_not_logged_in, Snackbar.LENGTH_LONG)
                                .setAction(R.string.fb_login, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((MainActivity) getActivity()).fbLogin();
                                    }
                                })
                                .show();
                    }
                    Log.w(TAG, "Longitude: " + currentLocation.getLongitude());
                } else {
                    Snackbar
                            .make(view, R.string.error_not_location, Snackbar.LENGTH_LONG)
                            .setAction(R.string.try_again, this)
                            .show();
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
                    if (prayList.size() > 0) {
                        ParseObject pray = prayList.get(0);

                        TextView title = (TextView) view.findViewById(R.id.main_title);
                        title.setText(pray.getString(ParseConstants.TABLE_PRAY_FIELD_DAY));

                        TextView text = (TextView) view.findViewById(R.id.main_text);
                        text.setText(pray.getString(ParseConstants.TABLE_PRAY_FIELD_TEXT));

                        TextView verse = (TextView) view.findViewById(R.id.main_verse);
                        verse.setText(pray.getString(ParseConstants.TABLE_PRAY_FIELD_VERSE));

                        View pray_container = view.findViewById(R.id.main_pray_container);
                        pray_container.setBackgroundColor(Color.parseColor(pray.getString(ParseConstants.TABLE_PRAY_FIELD_COLOR)));

                        if (pray.has(ParseConstants.TABLE_PRAY_FIELD_IMAGE)) {
                            ImageView main_image = (ImageView) view.findViewById(R.id.main_image);
                            ParseFile image_file = pray.getParseFile(ParseConstants.TABLE_PRAY_FIELD_IMAGE);

                            Picasso.with(getActivity())
                                    .load(image_file.getUrl())
                                    .into(main_image);
                        }

                    } else {
                        Log.w(TAG, "There is nothing on Pray.");
                    }
                } else {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    private void saveLocation(ParseUser currentUser, Location currentLocation, final View parent){
        ParseObject checkin = new ParseObject(ParseConstants.TABLE_CHECKIN);
        checkin.put(ParseConstants.TABLE_CHECKIN_FIELD_USER, currentUser);
        checkin.put(ParseConstants.TABLE_CHECKIN_FIELD_LAT, currentLocation.getLatitude());
        checkin.put(ParseConstants.TABLE_CHECKIN_FIELD_LONG, currentLocation.getLongitude());

        dialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.dlg_saving), true, false);

        checkin.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                if(e == null){
                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    startActivity(intent);
                } else{
                    Snackbar
                            .make(parent, R.string.error_unknown, Snackbar.LENGTH_LONG)
                            .show();
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }
}
