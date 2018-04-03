package com.r4sh33d.medmanager.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.r4sh33d.medmanager.models.Medication;

public class MedicationDao {

    public static Medication getMedicationInfoFromDb(String id, Context context) {
        MedicationDBHelper medicationDBHelper = new MedicationDBHelper(context);
        SQLiteDatabase db = medicationDBHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_NAME,
                MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_DESCRIPTION,
                MedicationDBContract.MedicationEntry.COLOMN_MEDICATION_QUANTITY,
                MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_START_TIME,
                MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_INTERVAL,
                MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_END_TIME,
        };
        String selection = MedicationDBContract.MedicationEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(
                MedicationDBContract.MedicationEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        Medication medication = null;
        if (cursor != null && cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(MedicationDBContract.MedicationEntry._ID);
            int nameColumn = cursor.getColumnIndex(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_NAME);
            int descriptionColumn = cursor.getColumnIndex(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_DESCRIPTION);
            int quantityColumn = cursor.getColumnIndex(MedicationDBContract.MedicationEntry.COLOMN_MEDICATION_QUANTITY);
            int startTimeColumn = cursor.getColumnIndex(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_START_TIME);
            int endTimeColumn = cursor.getColumnIndex(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_END_TIME);
            int intervalColumn = cursor.getColumnIndex(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_INTERVAL);
            medication = new Medication(
                    cursor.getString(nameColumn),
                    cursor.getString(descriptionColumn),
                    cursor.getString(quantityColumn),
                    cursor.getLong(startTimeColumn),
                    cursor.getLong(endTimeColumn),
                    cursor.getLong(intervalColumn),
                    cursor.getLong(idColumn)
            );
            cursor.close();
        }
        db.close();
        return medication;

    }


}
