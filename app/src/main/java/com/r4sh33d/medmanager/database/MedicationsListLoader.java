package com.r4sh33d.medmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.r4sh33d.medmanager.models.Medication;

import java.util.ArrayList;

public class MedicationsListLoader extends WrappedAsyncTaskLoader<ArrayList<Medication>> {
    private String selection;
    private String sortOrder;

    /**
     * Constructor of <code>WrappedAsyncTaskLoader</code>
     *
     * @param context The {@link Context} to use.
     */
    public MedicationsListLoader(Context context) {
        this(context, null, null);
    }

    public MedicationsListLoader(Context context, String selection, String sortOrder) {
        super(context);
        this.selection = selection;
        this.sortOrder = sortOrder;
    }

    @Nullable
    @Override
    public ArrayList<Medication> loadInBackground() {
        MedicationDBHelper medicationDBHelper = new MedicationDBHelper(getContext());
        SQLiteDatabase db = medicationDBHelper.getReadableDatabase();
        ArrayList<Medication> medications =  MedicationLoader.getMedicationsList(db , selection);
        db.close();
        return medications;
    }
}
