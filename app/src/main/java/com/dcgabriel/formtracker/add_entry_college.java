package com.dcgabriel.formtracker;

import android.view.Window;

import com.dcgabriel.formtracker.data.FormsContract.FormEntryTable;

public class add_entry_college extends AddEntryActivity {
    public static final String TAG = "add_entry_college";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_entry_college;
    }

    @Override
    protected void initializeFromChildActivity() {
        childContext = add_entry_college.this;
        formType = FormEntryTable.FORM_TYPE_COLLEGE;

        //changes the status bar color instead of the Primary Color
        Window window = add_entry_college.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.collegeColor));
    }


}
