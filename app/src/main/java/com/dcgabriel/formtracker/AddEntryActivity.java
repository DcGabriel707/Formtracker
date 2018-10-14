package com.dcgabriel.formtracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dcgabriel.formtracker.data.FormsContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class AddEntryActivity extends AppCompatActivity {
    private static String TAG = "AddEntry";
    private EditText nameEditText;
    private EditText companyEditText;
    private Spinner statusSpinner;
    private TextView deadlineDateTextView;
    private TextView dateSubmittedTextView;
    private EditText requirementEditText;
    private EditText descriptionEditText;
    private EditText todoEditText;
    private EditText notesEditText;
    private EditText websiteEditText;
    private EditText locationEditText;
    private EditText contactEmailEditText;
    private EditText contactNumberEditText;

    private EditText awardEditText;
    private EditText aveCostEditText;
    private EditText jobTypeEditText;
    private EditText salaryEditText;
    private EditText jobPostDateEditText;

    private DatePickerDialog.OnDateSetListener deadlineDateListener;
    private DatePickerDialog.OnDateSetListener dateSubmittedListener;
    private String deadlineDateString;
    private String dateSubmittedString;

    protected Context childContext;
    protected String formType;
    private Intent intent;
    private Uri currentUri;
    private ContentValues values;
    private DatePickerDialog deadlineDatePickerDialog;

    protected abstract int getLayoutId();
    protected abstract void initializeFromChildActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        initializeFromChildActivity();
        handleFindViewByIds();


        //handles the status spinner. string arrays found in strings.xml todo maybe put handle all spinner in a separate class
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(childContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.statusList));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        //gets the intent from RecyclerViewAdapter
        intent = getIntent();
        currentUri = intent.getData();

        //handle status spinner and date submitted
        handleStatusSpinner();

        //if an entry is being edited, or when the user clicked on the cardView. Not working if i put these findviewbyids on another method
        FloatingActionButton fabAdd = findViewById(R.id.add_entry_add_fab);
        CardView deleteButton = findViewById(R.id.deleteButton);
        CardView shareButton = findViewById(R.id.shareButton);
        if (currentUri != null) {
            //fills the empty fields with existing data
            fillExistingData();

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

    protected void handleFindViewByIds() {
        nameEditText = findViewById(R.id.nameEditText);
        companyEditText = findViewById(R.id.companyEditText);
        statusSpinner =  findViewById(R.id.statusSpinner);
        deadlineDateTextView = findViewById(R.id.deadlineDateText);
        dateSubmittedTextView = findViewById(R.id.dateSubmittedText);
        requirementEditText = findViewById(R.id.requirementEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        todoEditText = findViewById(R.id.todoEditText);
        notesEditText = findViewById(R.id.notesEditText);
        websiteEditText = findViewById(R.id.websiteEditText);
        locationEditText = findViewById(R.id.locationEditText);
        contactEmailEditText = findViewById(R.id.emailEditText);
        contactNumberEditText = findViewById(R.id.numberEditText);

        if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_SCHOLARSHIP)) {
            awardEditText = findViewById(R.id.scholarshipAwardEditText);
        } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_COLLEGE)) {
            aveCostEditText = findViewById(R.id.collegeAveCost);
        } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_EMPLOYMENT)) {
            jobTypeEditText = findViewById(R.id.jobTypeEditText);
            salaryEditText = findViewById(R.id.salaryEditText);
            jobPostDateEditText = findViewById(R.id.jobPostDateEditText);
        } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_OTHERS)) {

        }
    }

    //retrieves the data from the editTexts and other fields
    private void retrieveData() {
        Log.d(TAG, "retrieveData: ***************");

        String name = nameEditText.getText().toString().trim();
        String status = statusSpinner.getSelectedItem().toString().trim();
        String requirements = requirementEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String todo = todoEditText.getText().toString().trim();
        String notes = notesEditText.getText().toString().trim();
        String website = websiteEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String email = contactEmailEditText.getText().toString().trim();
        String phone = contactNumberEditText.getText().toString().trim();

        values = new ContentValues();
        values.put(FormsContract.FormEntryTable.COLUMN_NAME, name);
        values.put(FormsContract.FormEntryTable.COLUMN_STATUS, status);
        values.put(FormsContract.FormEntryTable.COLUMN_DEADLINE, deadlineDateString);
        values.put(FormsContract.FormEntryTable.COLUMN_DATE_SUBMITTED, dateSubmittedString);
        values.put(FormsContract.FormEntryTable.COLUMN_REQUIREMENTS, requirements);
        values.put(FormsContract.FormEntryTable.COLUMN_DESCRIPTION, description);
        values.put(FormsContract.FormEntryTable.COLUMN_TODO, todo);
        values.put(FormsContract.FormEntryTable.COLUMN_NOTES, notes);
        values.put(FormsContract.FormEntryTable.COLUMN_WEBSITE, website);
        values.put(FormsContract.FormEntryTable.COLUMN_LOCATION, location);
        values.put(FormsContract.FormEntryTable.COLUMN_CONTACT_EMAIL, email);
        values.put(FormsContract.FormEntryTable.COLUMN_CONTACT_NUMBER, phone);

        if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_SCHOLARSHIP)) {
            String award = awardEditText.getText().toString().trim();
            String company = companyEditText.getText().toString().trim();

            values.put(FormsContract.FormEntryTable.COLUMN_COMPANY, company);
            values.put(FormsContract.FormEntryTable.COLUMN_SCHOLARSHIP_AWARD, award);
            values.put(FormsContract.FormEntryTable.COLUMN_TYPE, FormsContract.FormEntryTable.FORM_TYPE_SCHOLARSHIP);
        } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_COLLEGE)) {
            String aveCost = aveCostEditText.getText().toString().trim();
            values.put(FormsContract.FormEntryTable.COLUMN_COLLEGE_AVE_COST, aveCost);
            values.put(FormsContract.FormEntryTable.COLUMN_TYPE, FormsContract.FormEntryTable.FORM_TYPE_COLLEGE);
        } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_EMPLOYMENT)) {
            String company = companyEditText.getText().toString().trim();
            String jobType = jobTypeEditText.getText().toString().trim();
            String salary = salaryEditText.getText().toString().trim();
            String jobPostDate = jobPostDateEditText.getText().toString().trim();


            values.put(FormsContract.FormEntryTable.COLUMN_COMPANY, company);
            values.put(FormsContract.FormEntryTable.COLUMN_JOB_TYPE, jobType);
            values.put(FormsContract.FormEntryTable.COLUMN_JOB_SALARY, salary);
            values.put(FormsContract.FormEntryTable.COLUMN_JOB_POST_DATE, jobPostDate);
            values.put(FormsContract.FormEntryTable.COLUMN_TYPE, FormsContract.FormEntryTable.FORM_TYPE_EMPLOYMENT);
        } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_OTHERS)) {
            String company = companyEditText.getText().toString().trim();

            values.put(FormsContract.FormEntryTable.COLUMN_COMPANY, company);
            values.put(FormsContract.FormEntryTable.COLUMN_TYPE, FormsContract.FormEntryTable.FORM_TYPE_OTHERS);
        }

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
        Uri uri = getContentResolver().insert(FormsContract.FormEntryTable.CONTENT_URI, values);
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
            if (rowsDeleted == 0) {
                Toast.makeText(childContext, "Delete Failed",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(childContext, "Delete Successful",
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
        Toast.makeText(childContext, "Remove Deadline", Toast.LENGTH_SHORT).show();
        deadlineDateString = null;
        deadlineDateTextView.setText(R.string.choose_date);
    }

    public void removeDateSubmitted(View view) {
        Toast.makeText(childContext, "Remove Deadline", Toast.LENGTH_SHORT).show();
        dateSubmittedString = null;
        dateSubmittedTextView.setText(R.string.choose_date);
    }

    private void setDeadlineDate() { //todo create separate class for handling calendar dialog
        CardView deadlineCardView = findViewById(R.id.deadlineAddDateCard);
        Calendar calendar = Calendar.getInstance();

        //this is the key to converting date format ?? give option in settings to change format. may change format in the databasse as well
        //maybe just change the format when displaying the dates, not when saving the dates into the database
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.dateFormat), Locale.US);

        //todo add option to change date format
        deadlineDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) { //todo add leading zeroes to months and days. no leading zeroes causes improper sorting
                m += 1;
                String temporaryDate = m + "/" + d + "/" + y;
                try { //todo try to not use try catch
                    Date tDate = simpleDateFormat.parse(temporaryDate); //converts into "MM/dd/yyyy" format first. maintains the leading zeroes
                    deadlineDateString = simpleDateFormat.format(tDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(childContext, "ParceException catched", Toast.LENGTH_SHORT).show();
                    System.exit(1);
                }
                Toast.makeText(childContext, deadlineDateString, Toast.LENGTH_SHORT).show();
                deadlineDateTextView.setText(deadlineDateString);
            }
        };

        //if the entry is being updated and there is a deadline
        if (currentUri != null && deadlineDateString != null) {//todo fix, enhance
            try {
                Date date = simpleDateFormat.parse(deadlineDateString);
                calendar.setTime(date);
                //deadlineDatePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                deadlineDatePickerDialog = new DatePickerDialog(childContext, android.R.style.Theme_DeviceDefault_Light_Dialog, deadlineDateListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else { //if there is no deadline.
            deadlineDatePickerDialog = new DatePickerDialog(childContext, android.R.style.Theme_DeviceDefault_Light_Dialog, deadlineDateListener,
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

        MyCalendarDialog myCalendarDialog = new MyCalendarDialog(childContext, dateSubmittedCardView, dateSubmittedString, dateSubmittedTextView, currentUri, dateSubmittedListener);
    }


    //fills the fields with existing data from the database?
    private void fillExistingData() {
        Log.d(TAG, "fillExistingData: ********************");

        //maybe add all columns here. Would it slow down anything?
        String[] projection = {FormsContract.FormEntryTable.COLUMN_NAME, FormsContract.FormEntryTable.COLUMN_COMPANY, FormsContract.FormEntryTable.COLUMN_SCHOLARSHIP_AWARD,
                FormsContract.FormEntryTable.COLUMN_COLLEGE_AVE_COST,
                FormsContract.FormEntryTable.COLUMN_JOB_TYPE, FormsContract.FormEntryTable.COLUMN_JOB_SALARY, FormsContract.FormEntryTable.COLUMN_JOB_POST_DATE,
                FormsContract.FormEntryTable.COLUMN_STATUS, FormsContract.FormEntryTable.COLUMN_DEADLINE, FormsContract.FormEntryTable.COLUMN_DATE_SUBMITTED, FormsContract.FormEntryTable.COLUMN_REQUIREMENTS,
                FormsContract.FormEntryTable.COLUMN_DESCRIPTION, FormsContract.FormEntryTable.COLUMN_TODO, FormsContract.FormEntryTable.COLUMN_NOTES,
                FormsContract.FormEntryTable.COLUMN_WEBSITE, FormsContract.FormEntryTable.COLUMN_LOCATION, FormsContract.FormEntryTable.COLUMN_CONTACT_EMAIL, FormsContract.FormEntryTable.COLUMN_CONTACT_NUMBER};

        String selection = FormsContract.FormEntryTable._ID + "=?";
        String[] selectionArgs = {intent.getStringExtra("_id")};

        Log.d(TAG, "fillExistingData: before cursor*****************");
        Cursor cursor = childContext.getContentResolver().query(FormsContract.FormEntryTable.CONTENT_URI, projection, selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) { //does it need to have cursor != null?
            nameEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_NAME)));

            deadlineDateString = cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_DEADLINE));
            if (deadlineDateString != null) {
                Log.d(TAG, "fillExistingData: has deadline ****************///////////deadlineDateString=" + deadlineDateString);
                deadlineDateTextView.setText(deadlineDateString);
            } else {
                Log.d(TAG, "fillExistingData: no deadlineDateString***** = " + deadlineDateString);
                deadlineDateTextView.setText(R.string.noDeadline);
            }

            dateSubmittedString = cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_DATE_SUBMITTED));
            if (dateSubmittedString != null) {
                Log.d(TAG, "fillExistingData: has dateSubmitted ****************///////////dateSubmittedString=" + dateSubmittedString);
                dateSubmittedTextView.setText(dateSubmittedString);
            } else {
                Log.d(TAG, "fillExistingData: no deadlineDateString***** = " + deadlineDateString);
                dateSubmittedTextView.setText(R.string.choose_date);
            }

            Log.d(TAG, "fillExistingData: before filling status********");
            switch (cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_STATUS))) { //fills in the status
                case FormsContract.FormEntryTable.STATUS_INCOMPLETE:
                    statusSpinner.setSelection(0);
                    break;
                case FormsContract.FormEntryTable.STATUS_SUBMITTED:
                    statusSpinner.setSelection(1);
                    break;
                case FormsContract.FormEntryTable.STATUS_ACCEPTED:
                    statusSpinner.setSelection(2);
                    break;
                case FormsContract.FormEntryTable.STATUS_REJECTED:
                    statusSpinner.setSelection(3);
                    break;
                default:
                    statusSpinner.setSelection(0);
                    break;
            }

            requirementEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_REQUIREMENTS)));
            descriptionEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_DESCRIPTION)));
            todoEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_TODO)));
            notesEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_NOTES)));
            websiteEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_WEBSITE)));
            locationEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_LOCATION)));
            contactEmailEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_CONTACT_EMAIL)));
            contactNumberEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_CONTACT_NUMBER)));

            if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_SCHOLARSHIP)) {

                companyEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_COMPANY)));
                awardEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_SCHOLARSHIP_AWARD)));
            } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_COLLEGE)) {
                aveCostEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_COLLEGE_AVE_COST)));
            } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_EMPLOYMENT)) {

                companyEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_COMPANY)));
                jobTypeEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_JOB_TYPE)));
                salaryEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_JOB_SALARY)));
                jobPostDateEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_JOB_POST_DATE)));
            } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_OTHERS)) {
                companyEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_COMPANY)));
            }

        }

        cursor.close();
        Log.d(TAG, "fillExistingData: cursor closed*********************");
    }

    //no leading zeroes causes improper sorting
    //hide keyboard when clicked outside edittext
}
