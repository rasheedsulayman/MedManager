package com.r4sh33d.medmanager.recycleradapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.r4sh33d.medmanager.R;
import com.r4sh33d.medmanager.activities.MainActivity;
import com.r4sh33d.medmanager.addmedication.AddMedicationFragment;
import com.r4sh33d.medmanager.models.Medication;
import com.r4sh33d.medmanager.utility.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicationsListAdapter extends RecyclerView.Adapter<MedicationsListAdapter.MedViewHolder> {
    private ArrayList<Medication> medicationArrayList;
    private boolean isFromSearchActivity;

    public MedicationsListAdapter(ArrayList<Medication> medicationArrayList, boolean isFromSearchActivity) {
        this.medicationArrayList = medicationArrayList;
        this.isFromSearchActivity = isFromSearchActivity;
    }

    @NonNull
    @Override
    public MedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medication_view, parent, false);
        return new MedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedViewHolder holder, int position) {
        Medication medication = medicationArrayList.get(position);
        SimpleDateFormat month_date = new SimpleDateFormat("hh:mm a, MMMM d, yyyy", Locale.getDefault());
        Date date = new Date(medication.startTime);
        holder.medicationName.setText(medication.name);
        holder.medicationStartDate.setText(String.format("Start date: %s", month_date.format(date)));
        date.setTime(medication.endTime);
        month_date.applyPattern("MMMM d, yyyy");
        holder.medicationEndDate.setText(String.format("End date: %s", month_date.format(date)));
    }

    @Override
    public int getItemCount() {
        return medicationArrayList.size();
    }

    public void updateData(ArrayList<Medication> data) {
        medicationArrayList = data;
        notifyDataSetChanged();
    }

    public void clearData() {
        medicationArrayList.clear();
        notifyDataSetChanged();
    }

    class MedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.medication_name)
        TextView medicationName;
        @BindView(R.id.medication_start_date)
        TextView medicationStartDate;
        @BindView(R.id.medication_end_date)
        TextView medicationEndDate;

        public MedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Medication medication = medicationArrayList.get(getAdapterPosition());
            if (isFromSearchActivity) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setAction(Constants.ACTION_NAVIGATE_TO_ADD_MEDICATION);
                intent.putExtra(Constants.KEY_MEDICATION_BUNDLE, medication);
                context.startActivity(intent);
            } else {
                FragmentTransaction transaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, AddMedicationFragment.newInstance(medication));
                transaction.addToBackStack(null).commit();
            }
        }
    }


}
