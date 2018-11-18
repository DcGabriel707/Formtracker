package com.dcgabriel.formtracker;

import android.view.Window;

import com.dcgabriel.formtracker.data.FormsContract;

public class add_entry_scholarship extends AddEntryActivity {
    private static final String TAG = "add_entry_scholarship";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_entry_scholarship;
    }

    @Override
    protected void initializeFromChildActivity() {
        childContext = add_entry_scholarship.this;
        formType = FormsContract.FormEntryTable.FORM_TYPE_SCHOLARSHIP;

        //changes the status bar color instead of the Primary Color
        Window window = add_entry_scholarship.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.scholarshipColor));
    }

}
