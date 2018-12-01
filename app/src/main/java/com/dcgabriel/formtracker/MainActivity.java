package com.dcgabriel.formtracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton addFab;
    private SectionsPagerAdapter tabAdapter;
    private ManageFragmentFromActivity callBack; //uses the interface to apply button actions to the fragments
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar); //theme was set to splashscreen them when application is opened. must reset to R.style.AppTheme_NoActionBar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.scholarship); //sets the title to the default tab title
        toolbar.setTitleTextAppearance(this, R.style.tajawalTextAppearance);
        setSupportActionBar(toolbar);
        setupViewPager();
        onTabChange();
        initializePreferences();

        addFab = (FloatingActionButton) findViewById(R.id.mainAddFab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.addButton();
            }
        });
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
        int id = item.getItemId();

        switch (id) {
            case (R.id.action_more):
                showMoreMenu();
                break;
            case (R.id.action_refresh):
                Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();
                callBack.refreshButton();
                break;
            case (R.id.action_sort):
                Log.d(TAG, "onOptionsItemSelected: sort");
                showSortMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult:************");
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void showMoreMenu() {
        PopupMenu dropDownMenu = new PopupMenu(MainActivity.this, findViewById(R.id.action_more));
        dropDownMenu.getMenuInflater().inflate(R.menu.more_menu, dropDownMenu.getMenu());

        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.action_settings):
                        Intent settings = new Intent(MainActivity.this, Settings.class);
                        startActivity(settings);
                        return true;
                    case (R.id.action_about):
                        Intent aboutPage = new Intent(MainActivity.this, AboutPage.class);
                        startActivity(aboutPage);
                        return true;
                }
                return false;
            }
        });
        dropDownMenu.show();
    }

    private void showSortMenu() {
        PopupMenu dropDownSort = new PopupMenu(MainActivity.this, findViewById(R.id.action_sort));
        dropDownSort.getMenuInflater().inflate(R.menu.sort_menu, dropDownSort.getMenu());
        //This will refer to the default, ascending or descending item.
        dropDownSort.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.sort_creation):
                        callBack.sortButton(ManageFragmentFromActivity.SORT_CREATION);
                        return true;
                    case (R.id.sort_name):
                        callBack.sortButton(ManageFragmentFromActivity.SORT_NAME);
                        return true;
                    case (R.id.sort_deadline):
                        callBack.sortButton(ManageFragmentFromActivity.SORT_DEADLINE);
                        return true;
                    case (R.id.sort_status):
                        callBack.sortButton(ManageFragmentFromActivity.SORT_STATUS);
                }
                return false;
            }
        });
        dropDownSort.show();
    }

    private void setupViewPager() {
        Log.d(TAG, "setupViewPager: ************");
        viewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutId);
        tabAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new ScholarshipTabFragment(), "Scholarship");
        tabAdapter.addFragment(new CollegeTabFragment(), "College");
        tabAdapter.addFragment(new EmploymentTabFragment(), "Job");
        tabAdapter.addFragment(new OthersTabFragment(), "Other");
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        //test
        //currentFragment = adapter.getItem(0);

        //add tab icon
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_attach_money_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_school_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_work_black_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_note_add_black_24dp);

        // check out  https://stackoverflow.com/questions/45602139/casting-interface-in-activity
        callBack = (ManageFragmentFromActivity) tabAdapter.getItem(0); // sets the default callback to the scholarship tab
    }

    private void onTabChange() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Window window = MainActivity.this.getWindow();

                callBack = (ManageFragmentFromActivity) tabAdapter.getItem(tabLayout.getSelectedTabPosition());  //sets the callback to the current fragment
                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        toolbar.setTitle(R.string.scholarship);
                        toolbar.setBackgroundColor(getResources().getColor(R.color.scholarshipColor)); //temp find better way to change primary color
                        window.setStatusBarColor(getResources().getColor(R.color.scholarshipColor));
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.scholarshipColor));
                        break;
                    case 1:
                        toolbar.setBackgroundColor(getResources().getColor(R.color.collegeColor)); //temp find better way to change primary color
                        window.setStatusBarColor(getResources().getColor(R.color.collegeColor));
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.collegeColor));
                        toolbar.setTitle(R.string.college);
                        break;
                    case 2:
                        toolbar.setBackgroundColor(getResources().getColor(R.color.employmentColor)); //temp find better way to change primary color
                        window.setStatusBarColor(getResources().getColor(R.color.employmentColor));
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.employmentColor));
                        toolbar.setTitle(R.string.employment);
                        break;
                    case 3:
                        toolbar.setBackgroundColor(getResources().getColor(R.color.othersColor)); //temp find better way to change primary color
                        window.setStatusBarColor(getResources().getColor(R.color.othersColor));
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.othersColor));
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

    private void initializePreferences() {

        //initializes date format to MM/dd/yyyy when the app is opened the very first time
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        if (sharedPreferences.getString("DateFormat", "N/A") == null || sharedPreferences.getString("DateFormat", "N/A").equals("N/A")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("DateFormat", getString(R.string.MMddyyyy));
            editor.apply();
        }
    }

    //
    public interface ManageFragmentFromActivity {
        final int SORT_NAME = 1;
        final int SORT_DEADLINE = 2;
        final int SORT_CREATION = 3;
        final int SORT_STATUS = 4;

        void addButton();

        void refreshButton();

        void sortButton(int sortType);
    }
}


//todo make multiple fragment for landscape mode
//todo add notification
//todo create image for empty view
//todo fix splascreen image
//todo save sorting preference
// todo add dashboard/ statistics of all the forms
//todo create app intro. shows features
//todo when icon is change, the adaptive, legacy icons must be changed as well
