package com.dcgabriel.formtracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dcgabriel.formtracker.data.FormsDBHelper;

import java.util.ArrayList;

public class EmploymentTabFragment extends Fragment {

    private static final String TAG = "EmploymentTabFragment";

    public EmploymentTabFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called88888888888888888888888888888888888888888888888888888888888888888");
      //  View view = inflater.inflate(R.layout.employment_fragment, container, false);
        View view = inflater.inflate(R.layout.temporary_empty_view, container, false);

        return view;
    }

}


//todo when creating the view application xml, use material theme textview to display the details. use small colored cardview to display each required documents??

