package com.dcgabriel.formtracker;

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
    }

}
