package com.dcgabriel.formtracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dcgabriel.formtracker.data.FormsContract.FormEntryTable;

public class FormsDBHelper extends SQLiteOpenHelper {
    private final String TAG = "FormsDBHelper";

    private static final String DATABASE_NAME = "FormsDb.db";
    private static final int DATABASE_VERSION = 2;

    public FormsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: called ************** ");
        String SQL_CREATE_FORMS_TABLE = "CREATE TABLE " + FormEntryTable.TABLE_NAME + "("
                + FormEntryTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FormEntryTable.COLUMN_TYPE + " TEXT NOT NULL, "
                + FormEntryTable.COLUMN_NAME + " TEXT NOT NULL, "
                + FormEntryTable.COLUMN_COMPANY + " TEXT, "
                + FormEntryTable.COLUMN_DEADLINE + " TEXT, "
                + FormEntryTable.COLUMN_DATE_SUBMITTED + " TEXT, "
                + FormEntryTable.COLUMN_STATUS + " TEXT, "
                + FormEntryTable.COLUMN_SCHOLARSHIP_AWARD + " TEXT, "
                + FormEntryTable.COLUMN_COLLEGE_AVE_COST + " TEXT,"
                + FormEntryTable.COLUMN_JOB_TYPE + " TEXT,"
                + FormEntryTable.COLUMN_JOB_SALARY + " TEXT,"
                + FormEntryTable.COLUMN_JOB_POST_DATE + " TEXT,"
                + FormEntryTable.COLUMN_REQUIREMENTS + " TEXT,"
                + FormEntryTable.COLUMN_DESCRIPTION + " TEXT, "
                + FormEntryTable.COLUMN_TODO + " TEXT,"
                + FormEntryTable.COLUMN_NOTES + " TEXT,"
                + FormEntryTable.COLUMN_WEBSITE + " TEXT,"
                + FormEntryTable.COLUMN_CONTACT_EMAIL + " TEXT,"
                + FormEntryTable.COLUMN_CONTACT_NUMBER + " TEXT,"
                + FormEntryTable.COLUMN_LOCATION + " TEXT"
                + "); ";

        db.execSQL(SQL_CREATE_FORMS_TABLE);
        Log.d(TAG, "onCreate: finished **************");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + FormEntryTable.TABLE_NAME);
        onCreate(db);
    }
}
