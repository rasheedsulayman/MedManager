package com.r4sh33d.medmanager.addmedication;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.r4sh33d.medmanager.database.MedicationDBContract;

/**
 * Created by rasheed on 3/1/18.
 */

public class AddMedicationPresenter implements AddMedicationContract.Presenter {
    private AddMedicationContract.View view;


    AddMedicationPresenter(AddMedicationContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {}

    @Override
    public void addMedication(String medicationName, String medicationDescription, String medicationQuantity,
                              long startTime, long endTime, long interval, SQLiteOpenHelper sqLiteOpenHelper) {
        // Gets the data repository in write mode
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_NAME, medicationName);
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_DESCRIPTION, medicationDescription);
        values.put(MedicationDBContract.MedicationEntry.COLOMN_MEDICATION_QUANTITY , medicationQuantity);
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_START_TIME, startTime);
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_END_TIME , endTime);
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_INTERVAL , interval);

        long newRowId = db.insert(MedicationDBContract.MedicationEntry.TABLE_NAME, null, values);

    }


    void scheduleNotificationJob(){
        //TODO come and implement this
    }

}
