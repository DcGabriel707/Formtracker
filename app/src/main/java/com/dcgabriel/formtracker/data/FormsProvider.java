package com.dcgabriel.formtracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dcgabriel.formtracker.data.FormsContract.FormEntryTable;

public class FormsProvider extends ContentProvider {

    private static final String TAG = "FormsProvider";
    private FormsDBHelper formsDBHelper;
    //URI matcher. to be used in a case statement.
    //does not matter what the int is as long as they are unique
    private static final int FORMS = 100; //all rows?
    private static final int FORM_ID = 101; //specific rows??

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH); //creates a UriMatcher object

    //??
    static {
        sUriMatcher.addURI(FormsContract.CONTENT_AUTHORITY, FormsContract.PATH_FORMS, FORMS); //act on the entire table
        sUriMatcher.addURI(FormsContract.CONTENT_AUTHORITY, FormsContract.PATH_FORMS + "/#", FORM_ID); // single specific row
    }


    @Override
    public boolean onCreate() {
        formsDBHelper = new FormsDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = formsDBHelper.getReadableDatabase();

        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FORMS:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = database.query(FormEntryTable.TABLE_NAME, projection, selection, selectionArgs, null, null,
                        sortOrder); //todo change order by??
                break;
            case FORM_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = FormEntryTable._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(FormEntryTable.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FORMS:
                return  FormEntryTable.CONTENT_LIST_TYPE;
            case FORM_ID:
                return  FormEntryTable.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalArgumentException("Invalid URI");
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Cursor cursor;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FORMS:
                SQLiteDatabase database = formsDBHelper.getWritableDatabase();
                long id = database.insert(FormEntryTable.TABLE_NAME, null, contentValues);
                // BURROWED CODE REMOVE LATER If the ID is -1, then the insertion failed. Log an error and return null.
                if (id == -1) {
                    Log.e(TAG, "Failed to insert row for " + uri);
                    return null;
                }
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = formsDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match){
            case FORMS:
                return database.delete(FormEntryTable.TABLE_NAME, selection, selectionArgs);
            case FORM_ID:
                selection = FormEntryTable._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return database.delete(FormEntryTable.TABLE_NAME, selection, selectionArgs);
                default:
                    throw new IllegalArgumentException("Delete failed" + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FORMS:
                return updateHelp(uri, contentValues, selection, selectionArgs);
            case FORM_ID:
                selection = FormEntryTable._ID + "=?";
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                return updateHelp(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unable to update");

        }
    }

    private int updateHelp(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase database = formsDBHelper.getWritableDatabase();
        //todo add data validation. fix what happens if contentValues is null

        if (contentValues.size() == 0) {
            return 0;
        }

        return database.update(FormEntryTable.TABLE_NAME,contentValues, selection, selectionArgs);
    }

}

