package com.dcgabriel.formtracker;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dcgabriel.formtracker.data.FormsContract.FormEntryTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ScholarshipTabFragment extends Fragment implements RecyclerViewAdapter.EntryClickedInterface, MainActivity.ManageFragmentFromActivity {
    private final String TAG = "ScholarshipTabFragment";

    private ArrayList<Forms> formsArrayList;
    private View view; //originally from onCreateView. I made this into global variable so onStart method can use it. find better option??
    private FloatingActionButton fab;
    private FloatingActionButton fabSort;//todo remove after
    private RecyclerViewAdapter adapter;
    private final int ADD_ITEM_REQUEST = 1; //used for startActivityForResult
    private final int UPDATE_ITEM_REQUEST = 2; //test


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: 88888888888888888888888888888888888888888888888");

        formsArrayList = new ArrayList<>();

        view = inflater.inflate(R.layout.scholarship_fragment, container, false);
        Log.d(TAG, "onCreateView: inflate*************");

        addEntriesIntoList();
        handleRecyclerView();

        if (formsArrayList.isEmpty()) {
            view = inflater.inflate(R.layout.temporary_empty_view, container, false);
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called****************************");


        final int ACTION_UPDATED = 1;
        final int ACTION_DELETED = 2;
        Toast.makeText(getActivity(), "onActivityResult from ScholarshipTabFragment", Toast.LENGTH_SHORT).show();
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ADD_ITEM_REQUEST: //when the addFab was clicked
                    updateLists(); // todo  9-4-18 remove. must use notifyItemInserted instead
                    adapter.notifyItemInserted(adapter.getItemCount());
                    Toast.makeText(getActivity(), "onActivityResult Received. Entry inserted", Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_ITEM_REQUEST: //when a specific CardView entry is clicked from the recyclerview
                    updateLists();
                    int pos = data.getIntExtra("position", -1); // gets the position of the entry being updated/deleted
                    int action = data.getIntExtra("action", -1); //tells if the entry will be updated or deleted
                    if (action == ACTION_UPDATED) {
                        adapter.notifyItemChanged(pos);
                        Toast.makeText(getActivity(), "onActivityResult Received. Entry updated. Position = " + pos, Toast.LENGTH_SHORT).show();
                    } else if (action == ACTION_DELETED) {
                        adapter.notifyItemRemoved(pos);
                        Toast.makeText(getActivity(), "onActivityResult Received. Entry deleted. Position = " + pos, Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    updateLists();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "onActivityResult Received. All entries updated. notifyDataSetChanged called", Toast.LENGTH_SHORT).show();
                    break;
            }

        }

    }


    private void handleRecyclerView() {
        Log.d(TAG, "handleRecyclerView: *********************************");
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewScholarshipFragment);

        //adapter = new RecyclerViewAdapter(ScholarshipTabFragment.this.getActivity(), this, nameList, companyList, detailsList, deadlineList, statusList);
        adapter = new RecyclerViewAdapter(ScholarshipTabFragment.this.getActivity(), this, formsArrayList);

        // RecyclerViewAdapter adapter = new RecyclerViewAdapter(ScholarshipTabFragment.this.getActivity(), nameList, companyList, detailsList, deadlineList, statusList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ScholarshipTabFragment.this.getActivity()));
        //todo create empty view
    }

    //temporary?
    private void addEntriesIntoList() {  //remove view parameter whenever
        Log.d(TAG, "addEntriesIntoList: ************");

        String[] projection = {FormEntryTable.COLUMN_NAME, FormEntryTable.COLUMN_COMPANY, FormEntryTable.COLUMN_SCHOLARSHIP_AWARD, FormEntryTable.COLUMN_STATUS, FormEntryTable.COLUMN_DEADLINE, FormEntryTable._ID}; //temp
        String selection = FormEntryTable.COLUMN_TYPE + "=?";
        String[] selectionArgs = {FormEntryTable.FORM_TYPE_SCHOLARSHIP};

        //TODO fix app is crashing when nothing is added to a list.
        //
        //FormProvider will handle this now
        //FormsDBHelper mDbHelper = new FormsDBHelper(getActivity());
        //SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //Cursor cursor = db.query(FormEntryTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        //new code. context is added on my own
        Cursor cursor = ScholarshipTabFragment.this.getContext().getContentResolver().query(FormEntryTable.CONTENT_URI, projection, selection, selectionArgs, null);
        Log.d(TAG, "addEntriesIntoList: cursor created******************************");
        try {

            for (int i = 0; cursor.moveToNext(); i++) {
                formsArrayList.add(new Forms());
                Log.d(TAG, "addEntriesIntoList: inside loop" + cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_NAME)));
                formsArrayList.get(i).setName(cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_NAME)));
                formsArrayList.get(i).setCompany(cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_COMPANY)));
                formsArrayList.get(i).setDetails(cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_SCHOLARSHIP_AWARD)));
                formsArrayList.get(i).setDeadline(cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_DEADLINE)));
                formsArrayList.get(i).setStatus(cursor.getString(cursor.getColumnIndex(FormEntryTable.COLUMN_STATUS)));
                formsArrayList.get(i).setId(cursor.getInt(cursor.getColumnIndex(FormEntryTable._ID)));
                formsArrayList.get(i).setUri(ContentUris.withAppendedId(FormEntryTable.CONTENT_URI, cursor.getInt(cursor.getColumnIndex(FormEntryTable._ID))));
            }

        } finally {
            cursor.close();
            Log.d(TAG, "addEntriesIntoList: cursor closed**********************************");
        }
    }

    //test remove whenever
    public void updateLists() {
        formsArrayList.clear();

        addEntriesIntoList();
        handleRecyclerView();
        adapter.notifyDataSetChanged();
        //adapter.notifyItemInserted(adapter.getItemCount());
    }

    //from EditEntryInterface
    @Override
    public void editEntry(int pos) {
        Intent edit = new Intent(ScholarshipTabFragment.this.getActivity(), add_entry_scholarship.class);

        edit.setData(formsArrayList.get(pos).getUri());
        edit.putExtra("_id", Integer.toString(formsArrayList.get(pos).getId()));//passes the id to the add_entry.
        edit.putExtra("position", pos); //todo try this. change to int??
        startActivityForResult(edit, UPDATE_ITEM_REQUEST);
    }

    //from ManageFragmentFromActivity interface
    @Override
    public void addButton() {
        Log.d(TAG, "addButton: ******************");
        Intent addEntry = new Intent(ScholarshipTabFragment.this.getActivity(), com.dcgabriel.formtracker.add_entry_scholarship.class);
        //returns a result when the new activity starts.
        startActivityForResult(addEntry, ADD_ITEM_REQUEST);
    }

    @Override
    public void sortButton() {
        //sort by name
        Collections.sort(formsArrayList, new Comparator<Forms>() {
            @Override
            public int compare(Forms o1, Forms o2) {
                Log.d(TAG, "compare: ***************");
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });


        adapter.notifyDataSetChanged();
    }


    //todo make the status CardView in the entry CardView a spinner. This would make it easier for users to update.
    //todo backgroundTint only works with 5.1+. Replace with app:backgroundTint?
    //todo if date is somehow empty, stop the app from crashing 9-26-18
}
