package com.dcgabriel.formtracker;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dcgabriel.formtracker.data.FormsContract;
import com.dcgabriel.formtracker.data.FormsContract.FormEntryTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class TabFragment extends Fragment implements RecyclerViewAdapter.EntryClickedInterface, MainActivity.ManageFragmentFromActivity {
    private static final String TAG = "TabFragment";
    private ArrayList<Forms> formsArrayList;
    private View view;
    private RecyclerViewAdapter adapter;
    private final int ADD_ITEM_REQUEST = 1; //used for startActivityForResult
    private final int UPDATE_ITEM_REQUEST = 2; //test
    protected Context childContext;
    protected String formType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parentContainer, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: 88888888888888888888888888888888888888888888888");
        view = getFragmentView(inflater, parentContainer, savedInstanceState);
        formsArrayList = new ArrayList<>();
        addEntriesIntoList();
        handleRecyclerView();
        setEmptyView();
        return view;
    }

    protected abstract View getFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    protected void handleRecyclerView() {
        Log.d(TAG, "handleRecyclerView: *********************************");
        RecyclerView recyclerView;
        System.out.println(childContext.toString());
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(childContext, this, formsArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(childContext));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called****************************");

        final int ACTION_UPDATED = 1;
        final int ACTION_DELETED = 2;
        if (resultCode == Activity.RESULT_OK) {
            updateLists();
            switch (requestCode) {
                case ADD_ITEM_REQUEST: //when the addFab was clicked
                    adapter.notifyItemInserted(adapter.getItemCount());
                    break;
                case UPDATE_ITEM_REQUEST: //when a specific CardView entry is clicked from the recyclerview
                    int pos = data.getIntExtra("position", -1); // gets the position of the entry being updated/deleted
                    int action = data.getIntExtra("action", -1); //tells if the entry will be updated or deleted
                    if (action == ACTION_UPDATED) {
                        formsArrayList.clear();
                        addEntriesIntoList();
                        adapter.notifyItemChanged(pos);
                    } else if (action == ACTION_DELETED) {
                        adapter.notifyItemRemoved(pos);
                    } else
                        adapter.notifyDataSetChanged();
                    break;
                default:
                    adapter.notifyDataSetChanged();
                    break;
            }
            setEmptyView();
        }
    }

    //check if list is empty and applies the empty view
    private void setEmptyView() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        View emptyLayoutView = view.findViewById(R.id.emptyView);
        if (formsArrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyLayoutView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyLayoutView.setVisibility(View.GONE);
        }

    }

    protected void addEntriesIntoList() {
        Log.d(TAG, "addEntriesIntoList: ************");
        String shortDetail;
        String companyOrLocation; //college tab uses location in place of company

        //detail textview is different for each form type
        if (formType.equals(FormEntryTable.FORM_TYPE_SCHOLARSHIP)) {
            shortDetail = FormEntryTable.COLUMN_SCHOLARSHIP_AWARD;
        } else if (formType.equals(FormEntryTable.FORM_TYPE_COLLEGE)) {
            shortDetail = FormEntryTable.COLUMN_COLLEGE_AVE_COST;
        } else if (formType.equals(FormEntryTable.FORM_TYPE_EMPLOYMENT)) {
            shortDetail = FormEntryTable.COLUMN_JOB_TYPE;
        } else if (formType.equals(FormEntryTable.FORM_TYPE_OTHERS)) {
            shortDetail = FormEntryTable.COLUMN_LOCATION;
        } else {
            shortDetail = "..."; //for debugging
        }

        if (formType.equals(FormEntryTable.FORM_TYPE_COLLEGE)) {
            companyOrLocation = FormEntryTable.COLUMN_LOCATION;
        } else {
            companyOrLocation = FormsContract.FormEntryTable.COLUMN_COMPANY;
        }

        String[] projection = new String[]{FormsContract.FormEntryTable.COLUMN_NAME, companyOrLocation,
                shortDetail, FormsContract.FormEntryTable.COLUMN_STATUS,
                FormsContract.FormEntryTable.COLUMN_DEADLINE, FormsContract.FormEntryTable._ID};
        String selection = FormsContract.FormEntryTable.COLUMN_TYPE + "=?";
        String[] selectionArgs = {formType};

        //new code. context is added on my own
        Cursor cursor = childContext.getContentResolver().query(FormsContract.FormEntryTable.CONTENT_URI, projection, selection, selectionArgs, null);
        Log.d(TAG, "addEntriesIntoList: cursor created******************************");
        try {

            for (int i = 0; cursor.moveToNext(); i++) {
                formsArrayList.add(new Forms());
                Log.d(TAG, "addEntriesIntoList: inside loop" + cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_NAME)));
                formsArrayList.get(i).setName(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_NAME)));
                formsArrayList.get(i).setCompany(cursor.getString(cursor.getColumnIndex(companyOrLocation)));
                formsArrayList.get(i).setDetails(cursor.getString(cursor.getColumnIndex(shortDetail)));
                formsArrayList.get(i).setDeadline(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_DEADLINE)));
                formsArrayList.get(i).setStatus(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_STATUS)));
                formsArrayList.get(i).setId(cursor.getInt(cursor.getColumnIndex(FormsContract.FormEntryTable._ID)));
                formsArrayList.get(i).setUri(ContentUris.withAppendedId(FormsContract.FormEntryTable.CONTENT_URI, cursor.getInt(cursor.getColumnIndex(FormsContract.FormEntryTable._ID))));
            }

        } finally {
            cursor.close();
            Log.d(TAG, "addEntriesIntoList: cursor closed**********************************");
        }
    }

    public void updateLists() {
        formsArrayList.clear();
        addEntriesIntoList();
        // handleRecyclerView();
    }


    //from ManageFragmentFromActivity interface
    @Override
    public void addButton() {
        Log.d(TAG, "addButton: ******************");

        Intent addEntry;
        if (formType.equals(FormEntryTable.FORM_TYPE_SCHOLARSHIP)) {
            addEntry = new Intent(childContext, com.dcgabriel.formtracker.add_entry_scholarship.class);
        } else if (formType.equals(FormEntryTable.FORM_TYPE_COLLEGE)) {
            addEntry = new Intent(childContext, com.dcgabriel.formtracker.add_entry_college.class);
        } else if (formType.equals(FormEntryTable.FORM_TYPE_EMPLOYMENT)) {
            addEntry = new Intent(childContext, com.dcgabriel.formtracker.add_entry_employment.class);
        } else if (formType.equals(FormEntryTable.FORM_TYPE_OTHERS)) {
            addEntry = new Intent(childContext, com.dcgabriel.formtracker.add_entry_others.class);
        } else {
            addEntry = new Intent(childContext, com.dcgabriel.formtracker.add_entry_scholarship.class);
        }
        //returns a result when the new activity starts.
        startActivityForResult(addEntry, ADD_ITEM_REQUEST);
    }

    //from EditEntryInterface
    @Override
    public void editEntry(int pos) {
        Intent editEntry;
        if (formType.equals(FormEntryTable.FORM_TYPE_SCHOLARSHIP)) {
            editEntry = new Intent(childContext, com.dcgabriel.formtracker.add_entry_scholarship.class);
        } else if (formType.equals(FormEntryTable.FORM_TYPE_COLLEGE)) {
            editEntry = new Intent(childContext, com.dcgabriel.formtracker.add_entry_college.class);
        } else if (formType.equals(FormEntryTable.FORM_TYPE_EMPLOYMENT)) {
            editEntry = new Intent(childContext, com.dcgabriel.formtracker.add_entry_employment.class);
        } else if (formType.equals(FormEntryTable.FORM_TYPE_OTHERS)) {
            editEntry = new Intent(childContext, com.dcgabriel.formtracker.add_entry_others.class);
        } else {
            editEntry = new Intent(childContext, com.dcgabriel.formtracker.add_entry_scholarship.class);
        }
        editEntry.setData(formsArrayList.get(pos).getUri());
        editEntry.putExtra("_id", Integer.toString(formsArrayList.get(pos).getId()));//passes the id to the add_entry.
        editEntry.putExtra("position", pos);
        startActivityForResult(editEntry, UPDATE_ITEM_REQUEST);

    }


    @Override
    public void refreshButton() {
        updateLists();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void sortButton(final int sortType) {
        //sort by name
        Collections.sort(formsArrayList, new Comparator<Forms>() {
            @Override
            public int compare(Forms o1, Forms o2) {
                Log.d(TAG, "compare: ***************");
                if (sortType == MainActivity.ManageFragmentFromActivity.SORT_NAME) { //sort by name
                    Log.d(TAG, "compare: Name***********");

                    return o1.getName().compareToIgnoreCase(o2.getName());
                } else if (sortType == MainActivity.ManageFragmentFromActivity.SORT_DEADLINE) { //sort by deadline
                    Log.d(TAG, "compare: Deadline***********");
                    if (o1.getDeadline() == null && !(o2.getDeadline() == null)) {  //if deadline1 is empty and deadline2 is not
                        return 1;
                    } else if (o2.getDeadline() == null && !(o1.getDeadline() == null)) { //if deadline1 is empty and deadline2 is not
                        return -1;
                    } else if (o1.getDeadline() == null && o2.getDeadline() == null) { // if both are empty

                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }//sort by alphabetical instead
                    else {
                        try {
                            //year must be sorted first before month and day
                            return new SimpleDateFormat("dd/mm/yyyy").parse(o1.getDeadline()).compareTo(new SimpleDateFormat("dd/mm/yyyy").parse(o2.getDeadline()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(childContext, "ParceException catched", Toast.LENGTH_SHORT).show();
                            return o1.getDeadline().compareTo(o2.getDeadline());

                        }

                    }
                } else if (sortType == MainActivity.ManageFragmentFromActivity.SORT_CREATION) {//sort by date created
                    Log.d(TAG, "compare: Creation***********");
                    return o1.getId().compareTo(o2.getId());
                } else {
                    Log.d(TAG, "compare: Default***********");
                    return o1.getId().compareTo(o2.getId());
                }

            }
        });

        adapter.notifyDataSetChanged();
    }

}
