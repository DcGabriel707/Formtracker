package com.dcgabriel.formtracker;

import android.view.View;
import android.view.Window;
import android.widget.Toast;

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

        //changes the status bar color instead of the Primary Color
        Window window = add_entry_employment.this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.employmentColor));
    }

    public void removeJobPostDate(View view) {
        Toast.makeText(childContext, "Remove Deadline", Toast.LENGTH_SHORT).show();
        jobPostDateString = null;
        jobPostDateTextView.setText(R.string.choose_date);

    }

}
