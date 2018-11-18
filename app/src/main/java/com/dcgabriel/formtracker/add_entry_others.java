package com.dcgabriel.formtracker;

import android.view.Window;

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

        //changes the status bar color instead of the Primary Color
        Window window = add_entry_others.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.othersColor));
    }
}
