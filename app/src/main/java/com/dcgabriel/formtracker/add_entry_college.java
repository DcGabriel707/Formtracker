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

public class add_entry_college extends AppCompatActivity {
    public static final String TAG = "add_entry_college";

    private EditText nameEditText;
    private EditText aveCostEditText;
    private Spinner statusSpinner;
    private TextView deadlineDate;
    private DatePickerDialog.OnDateSetListener deadlineDateListener;
    private CardView deadlineCardView;
    private String deadlineDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry_college);

        nameEditText = findViewById(R.id.nameEditText);
        aveCostEditText = findViewById(R.id.collegeAveCost);

        statusSpinner = findViewById(R.id.statusSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.statusList));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        setDeadlineDate();

        FloatingActionButton fab = findViewById(R.id.add_entry_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButton();
            }
        });
    }

    private void addButton() {
        Log.d(TAG, "addButton: called**********************");

        String name = nameEditText.getText().toString().trim();
        String aveCost = aveCostEditText.getText().toString().trim();
        String status = statusSpinner.getSelectedItem().toString().trim();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FormEntryTable.COLUMN_NAME, name);
        contentValues.put(FormEntryTable.COLUMN_TYPE, FormEntryTable.FORM_TYPE_COLLEGE);
        contentValues.put(FormEntryTable.COLUMN_COLLEGE_AVE_COST, aveCost);
        contentValues.put(FormEntryTable.COLUMN_STATUS, status);
        contentValues.put(FormEntryTable.COLUMN_DEADLINE, deadlineDateString);

        Uri uri = getContentResolver().insert(FormEntryTable.CONTENT_URI, contentValues);
        Log.d(TAG, "addButton: data inserted to content resolver*************");
        finish();

    }

    private void setDeadlineDate(){
        deadlineDate = (TextView) findViewById(R.id.deadlineDateText);
        deadlineCardView = (CardView) findViewById(R.id.deadlineAddDateCard);
        deadlineCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(add_entry_college.this, android.R.style.Theme_DeviceDefault_Light_Dialog, deadlineDateListener, year, month, day);
                datePickerDialog.getWindow();
                datePickerDialog.show();
            }
        });

        deadlineDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                deadlineDateString = month + "/" + day + "/" + year;
                deadlineDate.setText(deadlineDateString);
            }
        };
    }
}
