package com.dcgabriel.formtracker;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dcgabriel.formtracker.data.FormsContract;
import com.dcgabriel.formtracker.data.FormsContract.FormEntryTable;

import java.util.Calendar;

public class add_entry_college extends AddEntryActivity {
    public static final String TAG = "add_entry_college";

    private EditText nameEditText;
    private Spinner statusSpinner;
    private TextView deadlineDate;
    private DatePickerDialog.OnDateSetListener deadlineDateListener;
    private CardView deadlineCardView;
    private String deadlineDateString;


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
