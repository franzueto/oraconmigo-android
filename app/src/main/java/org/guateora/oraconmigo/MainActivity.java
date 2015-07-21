package org.guateora.oraconmigo;

import android.content.Intent;
import android.support.design.widget.NavigationView;
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
import android.view.MenuItem;
import android.view.View;

import org.guateora.oraconmigo.fragments.FragmentMain;
import org.guateora.oraconmigo.fragments.FragmentUnete;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        /*Intent intent = new Intent(MainActivity.this, CandidaturasActivity.class);
                        startActivity(intent);*/
                        break;
                    case R.id.faqs:
                       /* Intent intentFaqs = new Intent(MainActivity.this, FAQActivity.class);
                        startActivity(intentFaqs);*/
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
        adapter.addFrag(new FragmentMain(), getString(R.string.tab_1_title));
        adapter.addFrag(new FragmentMain(), getString(R.string.tab_2_title));
        adapter.addFrag(new FragmentUnete(), getString(R.string.tab_3_title));
        viewPager.setAdapter(adapter);
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
}
