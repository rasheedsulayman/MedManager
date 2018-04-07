package com.r4sh33d.medmanager.activeMedications;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.r4sh33d.medmanager.R;
import com.r4sh33d.medmanager.database.MedicationDao;
import com.r4sh33d.medmanager.database.MedicationsListLoader;
import com.r4sh33d.medmanager.models.Medication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveMedicationsFragment extends DialogFragment implements ActiveMedicationsContract.View ,
        LoaderManager.LoaderCallbacks<ArrayList<Medication>>{

    @BindView(R.id.recyclerview)
    RecyclerView medsListRecyclerView;
    ActiveMedicationsListAdapter activeMedicationsListAdapter;

    public ActiveMedicationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(100, null, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_schedule, container, false);
        ButterKnife.bind(this , view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activeMedicationsListAdapter = new ActiveMedicationsListAdapter(new ArrayList<Medication>());
        medsListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        medsListRecyclerView.setAdapter(activeMedicationsListAdapter);
    }


    @NonNull
    @Override
    public Loader<ArrayList<Medication>> onCreateLoader(int id, Bundle args) {
        return new MedicationsListLoader(getContext() , MedicationDao.ACTIVE_MEDICATION_SELECTION , null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Medication>> loader, ArrayList<Medication> data) {
        activeMedicationsListAdapter.updateData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Medication>> loader) {}

    @Override
    public void moveToNextStep() {}
}
