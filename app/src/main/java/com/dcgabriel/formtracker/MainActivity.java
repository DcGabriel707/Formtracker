package com.dcgabriel.formtracker;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
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
import android.widget.PopupMenu;
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

        switch (id) {
            case (R.id.action_more):
                Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                PopupMenu dropDownMenu = new PopupMenu(MainActivity.this, findViewById(R.id.action_more));
                dropDownMenu.getMenuInflater().inflate(R.menu.more_menu, dropDownMenu.getMenu());
                dropDownMenu.show();
                break;
            case (R.id.action_refresh):
                Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();
                callBack.refreshButton();
                break;
            case (R.id.action_sort):
                Log.d(TAG, "onOptionsItemSelected: sort");
                Toast.makeText(MainActivity.this, "Sorting", Toast.LENGTH_SHORT).show();
                showSortMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSortMenu(){
        PopupMenu dropDownSort = new PopupMenu(MainActivity.this, findViewById(R.id.action_sort));
        dropDownSort.getMenuInflater().inflate(R.menu.sort_menu, dropDownSort.getMenu());
        //This will refer to the default, ascending or descending item.
        dropDownSort.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MenuItem subMenuItem;
                switch (item.getItemId()) {
                    case (R.id.sort_creation):
                        //  subMenuItem = item.getSubMenu().getItem(0);
                        //  subMenuItem.setChecked(!subMenuItem.isChecked());
                        callBack.sortButton(ManageFragmentFromActivity.SORT_CREATION);
                        Toast.makeText(MainActivity.this, "Sorting Creation", Toast.LENGTH_SHORT).show();
                        return true;
                    case (R.id.sort_name):
                        callBack.sortButton(ManageFragmentFromActivity.SORT_NAME);
                        //  subMenuItem = item.getSubMenu().getItem(1);
                        //  subMenuItem.setChecked(!subMenuItem.isChecked());
                        return true;
                    case (R.id.sort_deadline):
                        callBack.sortButton(ManageFragmentFromActivity.SORT_DEADLINE);
                        //  subMenuItem = item.getSubMenu().getItem(2);
                        //  subMenuItem.setChecked(!subMenuItem.isChecked());
                        return true;
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
        final int SORT_NAME = 1;
        final int SORT_DEADLINE = 2;
        final int SORT_CREATION = 3;

        void addButton();
        void refreshButton();
        void sortButton(int sortType);
    }
}


//todo make multiple fragment for landscape mode
//todo should i put FAB on each tab fragments or just on main activity?
//todo add notification
//todo add action. when user click on the deadline, calendar will be opened
// FAB on each fragment is not properly appearing when toolbar is collapsed. FAB on each fragment can have good transition animation. FAB on Main activity requires more code, need to call fragment.onActivityResult for each tab
// todo change all icons to rounded icons
//todo sorting  deadline is not working properly.