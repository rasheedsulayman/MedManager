package com.r4sh33d.medmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.r4sh33d.medmanager.models.Medication;

import java.util.ArrayList;

public class MedicationLoader {

    public static String[] projection = {
            BaseColumns._ID,
            MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_NAME,
            MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_DESCRIPTION,
            MedicationDBContract.MedicationEntry.COLOMN_MEDICATION_QUANTITY,
            MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_START_TIME,
            MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_INTERVAL,
            MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_END_TIME,
            MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_NEXT_RING_TIME
    };

    public static String ACTIVE_MEDICATION_SELECTION = MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_END_TIME + ">"
            + System.currentTimeMillis();

    public static Medication getMedicationInfoWithId(long rowId, SQLiteDatabase db) {
        String selection = MedicationDBContract.MedicationEntry._ID + " = " + String.valueOf(rowId);
        return getMedicationsListFromCursor(getMedicationCursor(selection, db)).get(0);
    }

    public static ArrayList<Medication> getMedicationsList (SQLiteDatabase db , String selection) {
        return getMedicationsListFromCursor(getMedicationCursor(selection , db));
    }

    public static  int updateNextRingTime(long rowId , long newRIngTime , SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_NEXT_RING_TIME, newRIngTime);
        String selection = MedicationDBContract.MedicationEntry._ID + " = " + String.valueOf(rowId);
        return db.update(
                MedicationDBContract.MedicationEntry.TABLE_NAME,
                values,
                selection,
                null);
    }


    public static int updateMedication(ContentValues contentValues , long rowId , SQLiteDatabase db) {
        String selection = MedicationDBContract.MedicationEntry._ID + " = " + String.valueOf(rowId);
        return db.update(
                MedicationDBContract.MedicationEntry.TABLE_NAME,
                contentValues,
                selection,
                null);
    }

    public static Cursor getMedicationCursor(String selection, SQLiteDatabase db) {

        Cursor cursor = db.query(
                MedicationDBContract.MedicationEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        return cursor;
    }

    public static ContentValues getContentValuesForMedication (Medication medication){
        ContentValues values = new ContentValues();
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_NAME, medication.name);
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_DESCRIPTION, medication.description);
        values.put(MedicationDBContract.MedicationEntry.COLOMN_MEDICATION_QUANTITY , medication.quantity);
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_START_TIME, medication.startTime);
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_END_TIME , medication.endTime);
        values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_INTERVAL , medication.interval);
        if (medication.nextRingTime > 0) {
            values.put(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_NEXT_RING_TIME , medication.nextRingTime);
        }
        return values;
    }

    public static ArrayList<Medication> getMedicationsListFromCursor(Cursor cursor) {
        ArrayList<Medication> medicationArrayList = new ArrayList<>();
        if (cursor != null) {
            int idColumn = cursor.getColumnIndex(MedicationDBContract.MedicationEntry._ID);
            int nameColumn = cursor.getColumnIndex(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_NAME);
            int descriptionColumn = cursor.getColumnIndex(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_DESCRIPTION);
            int quantityColumn = cursor.getColumnIndex(MedicationDBContract.MedicationEntry.COLOMN_MEDICATION_QUANTITY);
            int startTimeColumn = cursor.getColumnIndex(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_START_TIME);
            int endTimeColumn = cursor.getColumnIndex(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_END_TIME);
            int intervalColumn = cursor.getColumnIndex(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_INTERVAL);
            int nextRingColumn = cursor.getColumnIndex(MedicationDBContract.MedicationEntry.COLUMN_MEDICATION_NEXT_RING_TIME);
            while (cursor.moveToNext()) {
                medicationArrayList.add(new Medication(
                        cursor.getString(nameColumn),
                        cursor.getString(descriptionColumn),
                        cursor.getString(quantityColumn),
                        cursor.getLong(startTimeColumn),
                        cursor.getLong(endTimeColumn),
                        cursor.getLong(intervalColumn),
                        cursor.getLong(idColumn),
                        cursor.getLong(nextRingColumn)
                ));
            }
            cursor.close();
        }
        return medicationArrayList;
    }

}