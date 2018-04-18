package com.r4sh33d.medmanager.monthlymedications;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.r4sh33d.medmanager.base.BaseFragment;
import com.r4sh33d.medmanager.R;
import com.r4sh33d.medmanager.activities.MainActivity;
import com.r4sh33d.medmanager.database.MedicationDBContract;
import com.r4sh33d.medmanager.database.MedicationsListLoader;
import com.r4sh33d.medmanager.datepickers.MonthOnlyPickerDialog;
import com.r4sh33d.medmanager.models.Medication;
import com.r4sh33d.medmanager.recycleradapters.MedicationsListAdapter;
import com.r4sh33d.medmanager.utility.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyMedicationsFragment extends BaseFragment implements
        LoaderManager.LoaderCallbacks<ArrayList<Medication>> {
    @BindView(R.id.fab)
    FloatingActionButton changeMonthFab;
    @BindView(R.id.recyclerview)
    RecyclerView monthlyMedicationsRecyclerView;
    @BindView(R.id.month_label)
    TextView monthLabel;
    @BindView(R.id.empty_med_textView)
    TextView emptyMedTextView;
    MedicationsListAdapter medicationsListAdapter;
    private String MONTHLY_INTERVAL_SELECTION = "";
    Calendar calendar;
    private static final String TAG = MonthlyMedicationsFragment.class.getSimpleName();

    public MonthlyMedicationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monthly_medications, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbarTitle("Monthly Medications");
        ((MainActivity) getActivity()).setDrawerIconToHome();
        medicationsListAdapter = new MedicationsListAdapter(new ArrayList<Medication>(), false);
        monthlyMedicationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        monthlyMedicationsRecyclerView.setAdapter(medicationsListAdapter);
        getLoaderManager().restartLoader(100, null, this);
        calendar = Calendar.getInstance();
        Utils.setCalenderDefault(calendar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Calendar thisMonthCalender = Calendar.getInstance();
        calendar.set(Calendar.YEAR, thisMonthCalender.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, thisMonthCalender.get(Calendar.MONTH));
        monthLabel.setText(getFormattedTextForMonthLabel(calendar));
        MONTHLY_INTERVAL_SELECTION = getMonthIntervalSelection(calendar);
        getLoaderManager().initLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<ArrayList<Medication>> onCreateLoader(int id, Bundle args) {
        return new MedicationsListLoader(getContext(), MONTHLY_INTERVAL_SELECTION, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Medication>> loader, ArrayList<Medication> data) {
        Log.d(TAG, "Data size is " + data.size());
        Log.d(TAG, "" + data);
        if (!(data.size() > 0)) {
            emptyMedTextView.setVisibility(View.VISIBLE);
            monthlyMedicationsRecyclerView.setVisibility(View.GONE);
        }else {
            emptyMedTextView.setVisibility(View.GONE);
            monthlyMedicationsRecyclerView.setVisibility(View.VISIBLE);
        }
        medicationsListAdapter.updateData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Medication>> loader) {
    }

    public void onDatePicked(int year, int month) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        monthLabel.setText(getFormattedTextForMonthLabel(calendar));
        MONTHLY_INTERVAL_SELECTION = getMonthIntervalSelection(calendar);
        getLoaderManager().restartLoader(0, null, this);
    }

    String getFormattedTextForMonthLabel(Calendar calendar) {
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM , yyyy", Locale.getDefault());
        return "Medications intake for " + month_date.format(calendar.getTime());
    }

    String getMonthIntervalSelection(Calendar calendar) {
        long startInterval = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 1);
        long endInterval = calendar.getTimeInMillis();
        return MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_START_TIME + " >= " + startInterval
                + " AND " + MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_END_TIME + " <= " + endInterval;
    }

    @OnClick(R.id.fab)
    void onCLickFab() {
        MonthOnlyPickerDialog pickerDialog = new MonthOnlyPickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                onDatePicked(year, month);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.show();
    }
}
