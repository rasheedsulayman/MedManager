package com.r4sh33d.medmanager.addmedication;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public interface AddMedicationContract {

    interface Presenter  {
        void start();
        void addMedication(String medicationName,
                           String medicationDescription,
                           String medicationQuantity,
                           long startTime, long endTime, long interval , SQLiteOpenHelper sqLiteOpenHelper);
    }

    interface View  {
        void moveToNextStep();
    }
}
