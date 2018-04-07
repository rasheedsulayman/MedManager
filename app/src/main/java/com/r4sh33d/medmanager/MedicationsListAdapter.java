package com.r4sh33d.medmanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.r4sh33d.medmanager.models.Medication;

import java.util.ArrayList;

public class MedicationsListAdapter extends RecyclerView.Adapter<MedicationsListAdapter.MedViewHolder> {
    private ArrayList<Medication> medicationArrayList;

    public MedicationsListAdapter(ArrayList<Medication> medicationArrayList) {
        this.medicationArrayList = medicationArrayList;
    }

    @NonNull
    @Override
    public MedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate( R.layout.item_medication_view , parent , false);
        return new MedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return medicationArrayList.size();
    }

    public void updateData(ArrayList<Medication> data) {

    }

    class MedViewHolder extends RecyclerView.ViewHolder {

        public MedViewHolder(View itemView) {
            super(itemView);
        }
    }
}
