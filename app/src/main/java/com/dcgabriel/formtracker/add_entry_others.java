package com.dcgabriel.formtracker;

import com.dcgabriel.formtracker.data.FormsContract;

public class add_entry_others extends AddEntryActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.actvity_add_entry_others;
    }

    @Override
    protected void initializeFromChildActivity() {
        childContext = add_entry_others.this;
        formType = FormsContract.FormEntryTable.FORM_TYPE_OTHERS;
    }
}
