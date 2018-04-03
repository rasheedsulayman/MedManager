package com.r4sh33d.medmanager.addmedication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.r4sh33d.medmanager.models.Medication;

public interface AddMedicationContract {

    interface Presenter  {
        void start();
        void addMedicationToDb(Medication medication ,  SQLiteOpenHelper sqLiteOpenHelper);
        void scheduleNotificationJob(AlarmManager alarmManager, Medication medication , PendingIntent alarmIntent);
    }



    interface View  {
        void onMedicationInsertedToDb(Medication medication);
        void moveToNextStep();
    }
}
