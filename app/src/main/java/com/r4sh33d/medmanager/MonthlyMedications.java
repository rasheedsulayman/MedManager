package com.r4sh33d.medmanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyMedications extends Fragment {


    public MonthlyMedications() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monthly_medications, container, false);
        ButterKnife.bind(this , view);
        return view;
    }

}
