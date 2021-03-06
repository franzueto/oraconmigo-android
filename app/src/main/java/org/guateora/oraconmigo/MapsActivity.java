package org.guateora.oraconmigo;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.guateora.oraconmigo.utils.ParseConstants;

import java.util.List;

public class MapsActivity extends AppCompatActivity {

    private static final String TAG = "MapsActivity";

    public static final String CENTER_MAP_LATITUDE = "CENTER_MAP_LATITUDE";
    public static final String CENTER_MAP_LONGITUDE= "CENTER_MAP_LONGITUDE";

    private final static int ZOOM_VALUE = 12;

    private final static LatLng DEFAULT_CENTER_MAP = new LatLng(14.59495, -90.51812); //GUATEMALA

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private View mapsContainer;

    private Double extraLat;
    private Double extraLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Add Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_maps));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mapsContainer = findViewById(R.id.maps_container);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            extraLat = extras.getDouble(CENTER_MAP_LATITUDE);
            extraLon = extras.getDouble(CENTER_MAP_LONGITUDE);
        }

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if(extraLat != null && extraLon !=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(extraLat, extraLon), ZOOM_VALUE));
        } else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_CENTER_MAP, ZOOM_VALUE));
        }

        getCheckins();
        /*

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(14.60000, -90.51812))
                .title("INICIO"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(14.62459, -90.51487))
                .title("META")
                .snippet("Este es un texto de prueba.")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));*/
    }

    private void getCheckins(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.TABLE_CHECKIN);
        query.include(ParseConstants.TABLE_CHECKIN_FIELD_USER);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> listCheckins, ParseException e) {
                if(e == null){
                    for(ParseObject checkin : listCheckins){
                        ParseUser user = checkin.getParseUser(ParseConstants.TABLE_CHECKIN_FIELD_USER);

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(
                                        checkin.getDouble(ParseConstants.TABLE_CHECKIN_FIELD_LAT)
                                        , checkin.getDouble(ParseConstants.TABLE_CHECKIN_FIELD_LONG))
                                )
                                .title(user.getString(ParseConstants.TABLE_USER_FIELD_NAME))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                    }
                } else{
                    Snackbar
                            .make(mapsContainer, R.string.error_unknown, Snackbar.LENGTH_LONG)
                            .setAction(R.string.try_again, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getCheckins();
                                }
                            })
                            .show();
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }
}
