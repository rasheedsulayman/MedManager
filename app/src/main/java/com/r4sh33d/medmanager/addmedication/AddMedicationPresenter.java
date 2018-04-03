package com.r4sh33d.medmanager.addmedication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.r4sh33d.medmanager.database.MedicationDBContract;
import com.r4sh33d.medmanager.models.Medication;
import com.r4sh33d.medmanager.utility.Utils;

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
    public void addMedicationToDb(Medication medication , SQLiteOpenHelper sqLiteOpenHelper) {
        // Gets the data repository in write mode
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_NAME, medication.name);
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_DESCRIPTION, medication.description);
        values.put(MedicationDBContract.MedicationEntry.COLOMN_MEDICATION_QUANTITY , medication.quantity);
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_START_TIME, medication.startTime);
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_END_TIME , medication.endTime);
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_INTERVAL , medication.interval);
        medication.dbRowId = db.insert(MedicationDBContract.MedicationEntry.TABLE_NAME, null, values);
        view.onMedicationInsertedToDb(medication);
    }

    @Override
    public void scheduleNotificationJob(AlarmManager alarmManager, Medication medication, PendingIntent alarmIntent) {
        if (Utils.isKitKatAndAbove()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, medication.startTime, alarmIntent);
        }else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, medication.startTime, alarmIntent);
        }
    }
}
