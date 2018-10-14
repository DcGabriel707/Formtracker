package com.dcgabriel.formtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dcgabriel.formtracker.data.FormsContract;

public class add_entry_employment extends AddEntryActivity {

    private final String TAG = "add_entry_employment";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_entry_employment;
    }

    @Override
    protected void initializeFromChildActivity() {
        childContext = add_entry_employment.this;
        formType = FormsContract.FormEntryTable.FORM_TYPE_EMPLOYMENT;
    }
}
