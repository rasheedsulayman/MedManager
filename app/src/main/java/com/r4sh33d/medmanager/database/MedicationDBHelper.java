package com.r4sh33d.medmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rasheed on 3/30/18.
 */

public class MedicationDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Medications.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MedicationContract.MedicationEntry.TABLE_NAME + " (" +
                    MedicationContract.MedicationEntry._ID + " INTEGER PRIMARY KEY," +
                    MedicationContract.MedicationEntry.COLUMN_MEDICATION_NAME + " TEXT," +
                    MedicationContract.MedicationEntry.COLUMN_MEDICATION_DESCRIPTION + " TEXT," +
                    MedicationContract.MedicationEntry.COLUMN_MEDICATION_INTERVAL + " INTEGER," +
                    MedicationContract.MedicationEntry.COLUMN_MEDICATION_START_TIME + " INTEGER," +
                    MedicationContract.MedicationEntry.COLUMN_MEDICATION_END_TIME + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MedicationContract.MedicationEntry.TABLE_NAME;

    public MedicationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}