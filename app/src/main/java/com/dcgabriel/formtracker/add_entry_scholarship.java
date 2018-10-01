package com.dcgabriel.formtracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dcgabriel.formtracker.data.FormsContract.FormEntryTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class add_entry_scholarship extends AppCompatActivity {

    private EditText nameEditText;
    private EditText companyEditText;
    private EditText awardEditText;
    private Spinner statusSpinner;
    private TextView deadlineDateTextView;
    private TextView dateSubmittedTextView;
    private DatePickerDialog.OnDateSetListener deadlineDateListener;
    private DatePickerDialog.OnDateSetListener dateSubmittedListener;

    private EditText requirementEditText;
    private EditText descriptionEditText;
    private EditText todoEditText;
    private EditText notesEditText;
    private EditText websiteEditText;
    private EditText contactEmailEditText;
    private EditText contactNumberEditText;

    private String deadlineDateString;
    private String dateSubmittedString;
    private static final String TAG = "add_entry_scholarship";
    private Intent intent;
    private Uri currentUri;
    private ContentValues values;
    private DatePickerDialog deadlineDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry_scholarship);

        nameEditText = findViewById(R.id.nameEditText);
        companyEditText = findViewById(R.id.companyEditText);
        awardEditText = findViewById(R.id.scholarshipAwardEditText);
        requirementEditText = findViewById(R.id.requirementEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        todoEditText = findViewById(R.id.todoEditText);
        notesEditText = findViewById(R.id.notesEditText);
        websiteEditText = findViewById(R.id.websiteEditText);
        contactEmailEditText = findViewById(R.id.emailEditText);
        contactNumberEditText = findViewById(R.id.numberEditText);

        deadlineDateTextView = findViewById(R.id.deadlineDateText);
        dateSubmittedTextView = findViewById(R.id.dateSubmittedText);

        FloatingActionButton fabAdd = findViewById(R.id.add_entry_add_fab);
        CardView deleteButton = findViewById(R.id.deleteButton);
        CardView shareButton = findViewById(R.id.shareButton);

        //handles the status spinner. string arrays found in strings.xml todo maybe put handle all spinner in a separate class
        statusSpinner = (Spinner) findViewById(R.id.statusSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.statusList));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        //gets the intent from RecyclerViewAdapter
        intent = getIntent();
        currentUri = intent.getData();


        //handle status spinner and date submitted
        handleStatusSpinner();

        //if an entry is being edited, or when the user clicked on the cardView
        if (currentUri != null) {
            //fills the empty fields with existing data
            fillExistingData();

          /**  fabDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteEntryButton();
                }
            }); */


            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteEntryButton();
                }
            });


            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateEntryButton();
                }
            });

        } else {
            deleteButton.setVisibility(View.INVISIBLE);
            shareButton.setVisibility(View.INVISIBLE);
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addButton();
                }
            });
        }

        //handles the deadline date. called again to update with existing date
        setDeadlineDate();
        //handles the date submitted
        setDateSubmitted();

    }

    //retrieves the data from the editTexts and other fields
    private void retrieveData() {
        Log.d(TAG, "retrieveData: ***************");
        String name = nameEditText.getText().toString().trim();
        String company = companyEditText.getText().toString().trim();
        String award = awardEditText.getText().toString().trim();
        String status = statusSpinner.getSelectedItem().toString().trim();
        String requirements = requirementEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String todo = todoEditText.getText().toString().trim();
        String notes = notesEditText.getText().toString().trim();
        String website = websiteEditText.getText().toString().trim();
        String email = contactEmailEditText.getText().toString().trim();
        String phone = contactNumberEditText.getText().toString().trim();

        values = new ContentValues();
        values.put(FormEntryTable.COLUMN_NAME, name);
        values.put(FormEntryTable.COLUMN_TYPE, FormEntryTable.FORM_TYPE_SCHOLARSHIP);
        values.put(FormEntryTable.COLUMN_COMPANY, company);
        values.put(FormEntryTable.COLUMN_SCHOLARSHIP_AWARD, award);
        values.put(FormEntryTable.COLUMN_STATUS, status);
        values.put(FormEntryTable.COLUMN_DEADLINE, deadlineDateString);
        values.put(FormEntryTable.COLUMN_DATE_SUBMITTED, dateSubmittedString);
        values.put(FormEntryTable.COLUMN_REQUIREMENTS, requirements);
        values.put(FormEntryTable.COLUMN_DESCRIPTION, description);
        values.put(FormEntryTable.COLUMN_TODO, todo);
        values.put(FormEntryTable.COLUMN_NOTES, notes);
        values.put(FormEntryTable.COLUMN_WEBSITE, website);
        values.put(FormEntryTable.COLUMN_CONTACT_EMAIL, email);
        values.put(FormEntryTable.COLUMN_CONTACT_NUMBER, phone);

        Log.d(TAG, "retrieveData: deadlineFinal = " + deadlineDateString + "////////////////////////////////////////////////////////////");
    }

    //change name to addEntryButton or doneButton
    //addButton shares an FAB with the update button.
    public void addButton() {
        Log.d(TAG, "addButton: called*************");

        //retrieves the data from the editTexts and other fields
        retrieveData();

        //FormsProvider will handle this??
        //FormsDBHelper formsDBHelper = new FormsDBHelper(this);
        //SQLiteDatabase db = formsDBHelper.getWritableDatabase();

        //instead of this long newRowId = db.insert(FormEntryTable.TABLE_NAME, null, values);
        Uri uri = getContentResolver().insert(FormEntryTable.CONTENT_URI, values);
        Log.d(TAG, "addButton: data inserted****************");

        Intent result = new Intent();
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    //share FAB with the addButton()
    private void updateEntryButton() {
        Log.d(TAG, "updateEntryButton: ***********************");
        int rowsUpdated = 0;

        //retrieves the data from the editTexts and other fields then store them into ContentValues
        retrieveData();

        // from https://developer.android.com/guide/topics/providers/content-provider-basics
        //Sets the updated value and updates the selected words.
        //values.putNull(UserDictionary.Words.LOCALE);
        //
        rowsUpdated = getContentResolver().update(currentUri, values, null, null);

        int pos = intent.getIntExtra("position", -999);

        Intent result = new Intent();
        result.putExtra("position", pos); //position not accurate. only for testign
        result.putExtra("action", 1); //specifies that the action is deleted not updated
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    private void deleteEntryButton() {

        //Burrowed from https://classroom.udacity.com/courses/ud845/lessons/9baef157-4f66-4513-b612-90c5f6975c21/concepts/5bcf3ea1-e2fe-4259-85d6-8f43b745bd35#
        // CHANGE***************************
        // Only perform the delete if this is an existing pet.
        if (currentUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(currentUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, "Delete Failed",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, "Delete Successful",
                        Toast.LENGTH_SHORT).show();
            }
        }

        Intent result = new Intent();
        setResult(Activity.RESULT_OK, result);
        int pos = intent.getIntExtra("position", -999); //position received from RecyclerViewAdapter
        result.putExtra("position", pos);
        result.putExtra("action", 2); //specifies that the action is deleted not updated
        finish();

    }

    private void handleStatusSpinner() {
        Log.d(TAG, "handleStatusSpinner: **********************");
        final RelativeLayout dateSubmittedLayout = findViewById(R.id.dateSubmittedLayout);
        final LinearLayout outerLayout = findViewById(R.id.outerLayoutStatusDeadlineSubmitted);
        Log.d(TAG, "handleStatusSpinner: wdfawfwf-**********************---------");

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isRemoved = false;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d(TAG, "handleStatusSSpinner: onItemClick: **************************");

                if (((position == 1) || (position == 2) || (position == 3)) && isRemoved) {
                    Log.d(TAG, "handleStatusSpinner: onItemClick: add**************");
                    outerLayout.addView(dateSubmittedLayout);
                    isRemoved = false;
                } else if (position == 0) { //remove dateSubmitted views if the status is "incomplete
                    outerLayout.removeView(dateSubmittedLayout);
                    isRemoved = true;
                    dateSubmittedString = null;
                    dateSubmittedTextView.setText(R.string.choose_date);
                    Log.d(TAG, "handleStatusSpinner: onItemClick: remove*****************");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void removeDeadline(View view) {
        Toast.makeText(add_entry_scholarship.this, "Remove Deadline", Toast.LENGTH_SHORT).show();
        deadlineDateString = null;
        deadlineDateTextView.setText(R.string.choose_date);
    }
    public void removeDateSubmitted(View view) {
        Toast.makeText(add_entry_scholarship.this, "Remove Deadline", Toast.LENGTH_SHORT).show();
        dateSubmittedString = null;
        dateSubmittedTextView.setText(R.string.choose_date);
    }

    private void setDeadlineDate() { //todo create separate class for handling calendar dialog
        CardView deadlineCardView = findViewById(R.id.deadlineAddDateCard);
        Calendar calendar = Calendar.getInstance();

        //todo add option to change date format
        deadlineDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                m += 1;
                deadlineDateString = m + "/" + d + "/" + y;
                deadlineDateTextView.setText(deadlineDateString);
            }
        };

        //if the entry is being updated and there is a deadline
        if (currentUri != null && deadlineDateString != null) {//todo fix, enhance
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                Date date = simpleDateFormat.parse(deadlineDateString);
                calendar.setTime(date);
                //deadlineDatePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                deadlineDatePickerDialog = new DatePickerDialog(add_entry_scholarship.this, android.R.style.Theme_DeviceDefault_Light_Dialog, deadlineDateListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else { //if there is no deadline.
            deadlineDatePickerDialog = new DatePickerDialog(add_entry_scholarship.this, android.R.style.Theme_DeviceDefault_Light_Dialog, deadlineDateListener,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }

        deadlineCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deadlineDatePickerDialog.getWindow();
                deadlineDatePickerDialog.show();
            }
        });
        Log.d(TAG, "setDeadlineDate bottom: ************///////////////// deadlineDateString=" + deadlineDateString);
    }

    private void setDateSubmitted() {
        CardView dateSubmittedCardView = (CardView) findViewById(R.id.dateSubmittedCardView);
        //todo add option to change date format

        dateSubmittedListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                m += 1;
                dateSubmittedString = m + "/" + d + "/" + y;
                dateSubmittedTextView.setText(dateSubmittedString);
            }
        };

        MyCalendarDialog myCalendarDialog = new MyCalendarDialog(add_entry_scholarship.this, dateSubmittedCardView, dateSubmittedString, dateSubmittedTextView, currentUri, dateSubmittedListener);
    }

    //fills the fields with existing data from the database?
    private void fillExistingData() {
        Log.d(TAG, "fillExistingData: ********************");

        String[] projection = {FormEntryTable.COLUMN_NAME, FormEntryTable.COLUMN_COMPANY, FormEntryTable.COLUMN_SCHOLARSHIP_AWARD,
                FormEntryTable.COLUMN_STATUS, FormEntryTable.COLUMN_DEADLINE, FormEntryTable.COLUMN_DATE_SUBMITTED, FormEntryTable.COLUMN_REQUIREMENTS,
                FormEntryTable.COLUMN_DESCRIPTION, FormEntryTable.COLUMN_TODO, FormEntryTable.COLUMN_NOTES,
                FormEntryTable.COLUMN_WEBSITE, FormEntryTable.COLUMN_CONTACT_EMAIL, FormEntryTable.COLUMN_CONTACT_NUMBER}; //temp
        String selection = FormEntryTable._ID + "=?";
        String[] selectionArgs = {intent.getStringExtra("_id")};

        Log.d(TAG, "fillExistingData: before cursor*****************");
        Cursor cursor = add_entry_scholarship.this.getContentResolver().query(FormEntryTable.CONTENT_URI, projection, selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) { //does it need to have cursor != null?
            nameEditText.setText(cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_NAME)));
            companyEditText.setText(cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_COMPANY)));
            awardEditText.setText(cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_SCHOLARSHIP_AWARD)));

            deadlineDateString = cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_DEADLINE));
            if (deadlineDateString != null) {
                Log.d(TAG, "fillExistingData: has deadline ****************///////////deadlineDateString=" + deadlineDateString);
                deadlineDateTextView.setText(deadlineDateString);
            } else {
                Log.d(TAG, "fillExistingData: no deadlineDateString***** = " + deadlineDateString);
                deadlineDateTextView.setText(R.string.noDeadline);
            }

            dateSubmittedString = cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_DATE_SUBMITTED));
            if (dateSubmittedString != null) {
                Log.d(TAG, "fillExistingData: has dateSubmitted ****************///////////dateSubmittedString=" + dateSubmittedString);
                dateSubmittedTextView.setText(dateSubmittedString);
            } else {
                Log.d(TAG, "fillExistingData: no deadlineDateString***** = " + deadlineDateString);
                dateSubmittedTextView.setText(R.string.choose_date);
            }

            Log.d(TAG, "fillExistingData: before filling status********");
            switch (cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_STATUS))) { //fills in the status
                case FormEntryTable.STATUS_INCOMPLETE:
                    statusSpinner.setSelection(0);
                    break;
                case FormEntryTable.STATUS_SUBMITTED:
                    statusSpinner.setSelection(1);
                    break;
                case FormEntryTable.STATUS_ACCEPTED:
                    statusSpinner.setSelection(2);
                    break;
                case FormEntryTable.STATUS_REJECTED:
                    statusSpinner.setSelection(3);
                    break;
                default:
                    statusSpinner.setSelection(0);
                    break;
            }

            requirementEditText.setText(cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_REQUIREMENTS)));
            descriptionEditText.setText(cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_DESCRIPTION)));
            todoEditText.setText(cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_TODO)));
            notesEditText.setText(cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_NOTES)));
            websiteEditText.setText(cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_WEBSITE)));
            contactEmailEditText.setText(cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_CONTACT_EMAIL)));
            contactNumberEditText.setText(cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_CONTACT_NUMBER)));
        }

        cursor.close();
        Log.d(TAG, "fillExistingData: cursor closed*********************");
    }

    //todo fix recyclerview refresh from editing and deleting

}
