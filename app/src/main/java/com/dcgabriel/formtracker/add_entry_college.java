package com.dcgabriel.formtracker;

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
    }


}
