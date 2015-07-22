package org.guateora.oraconmigo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.guateora.oraconmigo.fragments.FragmentMain;
import org.guateora.oraconmigo.fragments.FragmentUnete;
import org.guateora.oraconmigo.utils.ParseConstants;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    public Location mCurrentLocation;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());

        // Add Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        setUpDrawer(toolbar);

        //View Pager
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        setupViewPager(viewPager);

        //Tabs with view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        tabLayout.setupWithViewPager(viewPager);

        setUpLocation();
    }

    private void setUpLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
            LocationListener locationListener = new MyLocationListener();
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        }else{
            Toast.makeText(this, "Por favor activa el GPS", Toast.LENGTH_SHORT).show();
        }

    }

    private void setUpDrawer(Toolbar toolbar){
        // Configure Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_left);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }
        };

        drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

        //Navigation Listener
        NavigationView main_drawer_navigation = (NavigationView) findViewById(R.id.main_drawer_navigation);

        if(ParseUser.getCurrentUser() != null){
            Menu drawer_menu = main_drawer_navigation.getMenu();
            MenuItem logInOut = drawer_menu.findItem(R.id.loginout);
            logInOut.setTitle(getString(R.string.logout));
        }

        main_drawer_navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                /*TODO FIX WITH RIGHT ACTIONS*/
                switch (menuItem.getItemId()) {
                    case R.id.guia_completa:
                        //Intent mainIntent = new Intent(MainActivity.this, ProfileActivity.class);
                        //startActivity(mainIntent);
                        break;
                    case R.id.mapa_oradores:
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.faqs:
                       /* Intent intentFaqs = new Intent(MainActivity.this, FAQActivity.class);
                        startActivity(intentFaqs);*/
                        break;
                    case R.id.loginout:
                        if (ParseUser.getCurrentUser() != null) {
                            ParseUser.logOut();
                            finish();
                            startActivity(getIntent());
                        } else {
                            fbLogin(menuItem.getActionView());
                        }
                        break;
                    default:
                        Log.w("TAG", "Do nothing");
                        break;
                }

                return true;
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(FragmentMain.newInstance(), getString(R.string.tab_1_title));
        adapter.addFrag(new FragmentMain(), getString(R.string.tab_2_title));
        adapter.addFrag(FragmentUnete.newInstance(), getString(R.string.tab_3_title));
        viewPager.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void fbLogin(final View parent){
        dialog = ProgressDialog.show(this, "", getResources().getString(R.string.dlg_loading), true, false);

        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, Arrays.asList("email"), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    dialog.dismiss();
                    Snackbar
                            .make(parent, R.string.error_couldnt_login, Snackbar.LENGTH_LONG)
                            .setAction(R.string.try_again, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fbLogin(parent);
                                }
                            })
                            .show();
                } else if (user.isNew()) {
                    getFacebookIdInBackground();
                } else {
                    dialog.dismiss();
                    finish();
                    startActivity(getIntent());
                }
            }
        });
    }

    private void getFacebookIdInBackground() {
        GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject user, GraphResponse response) {
                if (user != null) {
                    try {
                        //Log.w("info user", user.toString());

                        final String first_name = user.get("first_name").toString();
                        final String last_name = user.get("last_name").toString();
                        final String fbId = user.get("id").toString();
                        final String email = user.get("email").toString();
                        final String gender = user.get("gender").toString();

                        ParseUser.getCurrentUser().put(ParseConstants.TABLE_USER_FIELD_FBID, fbId);
                        ParseUser.getCurrentUser().put(ParseConstants.TABLE_USER_FIELD_NAME, first_name + " " + last_name);
                        ParseUser.getCurrentUser().put(ParseConstants.TABLE_USER_FIELD_FIRSTNAME, first_name);
                        ParseUser.getCurrentUser().put(ParseConstants.TABLE_USER_FIELD_LASTNAME, last_name);
                        ParseUser.getCurrentUser().put(ParseConstants.TABLE_USER_FIELD_EMAIL, email);
                        ParseUser.getCurrentUser().put(ParseConstants.TABLE_USER_FIELD_GENDER, gender);

                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                dialog.dismiss();
                                if (e == null) {
                                    finish();
                                    startActivity(getIntent());
                                } else {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, getString(R.string.error_unknown) + " - " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        dialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, getString(R.string.error_unknown), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }).executeAsync();

    }

    @Override
    public void onPause() {
        super.onPause();
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            mCurrentLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
