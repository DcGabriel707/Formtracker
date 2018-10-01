package com.dcgabriel.formtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private final int ADD_ITEM_REQUEST = 1; //used for startActivityForResult
    private final int UPDATE_ITEM_REQUEST = 2; //test
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Fragment currentFragment;
    private FloatingActionButton addFab;
    private SectionsPagerAdapter adapter;
    private ManageFragmentFromActivity callBack; //uses the interface to apply button actions to the fragments
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar); //theme was set to splashscreen them when application is opened. must reset to R.style.AppTheme_NoActionBar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.scholarship); //sets the title to the default tab title
        setSupportActionBar(toolbar);
        setupViewPager();
        onTabChange();

        addFab = (FloatingActionButton) findViewById(R.id.mainAddFab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.addButton();
            }
        });
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        // check out  https://stackoverflow.com/questions/45602139/casting-interface-in-activity
        if (fragment instanceof ManageFragmentFromActivity) {
            callBack = (ManageFragmentFromActivity) fragment;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ************");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (id) {
            case (R.id.action_settings):
                Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.action_refresh):
                Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();

                break;
            case (R.id.action_sort):
                Log.d(TAG, "onOptionsItemSelected: sort");
                Toast.makeText(MainActivity.this, "Sorting", Toast.LENGTH_SHORT).show();
                callBack.sortButton();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupViewPager() {
        Log.d(TAG, "setupViewPager: ************");
        viewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutId);
        adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ScholarshipTabFragment(), "Scholarship");
        adapter.addFragment(new CollegeTabFragment(), "College");
        adapter.addFragment(new EmploymentTabFragment(), "Job");
        adapter.addFragment(new OthersTabFragment(), "Other");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //test
        //currentFragment = adapter.getItem(0);


        //add tab icon
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_attach_money_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_school_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_work_black_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_note_add_black_24dp);
    }

    private void onTabChange() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Window window = MainActivity.this.getWindow();
                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        toolbar.setTitle(R.string.scholarship);
                        toolbar.setBackgroundColor(getResources().getColor(R.color.scholarshipColor)); //temp find better way to change primary color
                        window.setStatusBarColor(getResources().getColor(R.color.scholarshipColor));
                        addFab.setBackgroundColor(getResources().getColor(R.color.scholarshipColor));
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.scholarshipColor));
                        break;
                    case 1:
                        toolbar.setBackgroundColor(getResources().getColor(R.color.collegeColor)); //temp find better way to change primary color
                        window.setStatusBarColor(getResources().getColor(R.color.collegeColor));
                        addFab.setBackgroundColor(getResources().getColor(R.color.collegeColor));
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.collegeColor));
                        setTheme(R.style.CollegeTheme);
                        toolbar.setTitle(R.string.college);
                        break;
                    case 2:
                        toolbar.setBackgroundColor(getResources().getColor(R.color.employmentColor)); //temp find better way to change primary color
                        window.setStatusBarColor(getResources().getColor(R.color.employmentColor));
                        addFab.setBackgroundColor(getResources().getColor(R.color.employmentColor));
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.employmentColor));
                        setTheme(R.style.EmploymentTheme);
                        toolbar.setTitle(R.string.employment);
                        break;
                    case 3:
                        toolbar.setBackgroundColor(getResources().getColor(R.color.othersColor)); //temp find better way to change primary color
                        window.setStatusBarColor(getResources().getColor(R.color.othersColor));
                        addFab.setBackgroundColor(getResources().getColor(R.color.othersColor));
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.othersColor));
                        setTheme(R.style.OthersTheme);
                        toolbar.setTitle(R.string.others);
                        break;
                    default:
                        toolbar.setTitle(R.string.app_name);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult:************");
        super.onActivityResult(requestCode, resultCode, data);
        // currentFragment.onActivityResult(requestCode, resultCode, data); //todo works but find better alternative. works only on scholarship fragment
        Toast.makeText(this, "onActivityResult from MainActivity", Toast.LENGTH_SHORT).show();
    }

    //
    public interface ManageFragmentFromActivity {
        void addButton();

        void sortButton();
    }
}


//todo make multiple fragment for landscape mode
//todo should i put FAB on each tab fragments or just on main activity?
//todo make splashscreen
//todo add notification
//todo add action. when user click on the deadline, calendar will be opened
// FAB on each fragment is not properly appearing when toolbar is collapsed. FAB on each fragment can have good transition animation. FAB on Main activity requires more code, need to call fragment.onActivityResult for each tab