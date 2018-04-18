package com.r4sh33d.medmanager.addmedication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.database.sqlite.SQLiteDatabase;

import com.r4sh33d.medmanager.database.MedicationDao;
import com.r4sh33d.medmanager.models.Medication;
import com.r4sh33d.medmanager.utility.Utils;

/**
 * Created by rasheed on 3/1/18.
 */

public class AddMedicationPresenter implements AddMedicationContract.Presenter {
    private AddMedicationContract.View view;
    private static final String TAG = AddMedicationPresenter.class.getSimpleName();

    AddMedicationPresenter(AddMedicationContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
    }

    @Override
    public void addMedicationToDb(Medication medication, SQLiteDatabase db) {
        // Gets the data repository in write mode
        medication.dbRowId = MedicationDao.addMedication(medication, db);
        view.onMedicationInsertedToDb(medication);
    }

    @Override
    public void scheduleNotificationJob(AlarmManager alarmManager, Medication medication, PendingIntent alarmIntent) {
        if (Utils.isKitKatAndAbove()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, medication.startTime, alarmIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, medication.startTime, alarmIntent);
        }
    }

    @Override
    public void updateMedication(Medication medication, SQLiteDatabase db) {
        MedicationDao.updateMedication(medication, db);
        view.onMedicationUpdated();
    }
}
